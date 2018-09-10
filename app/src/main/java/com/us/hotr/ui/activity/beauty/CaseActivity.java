package com.us.hotr.ui.activity.beauty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.receiver.Share;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Comment;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.PostOld;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Reply;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.info.FriendActivity;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.ui.dialog.CommentDialogFragment;
import com.us.hotr.ui.dialog.ShareDialogFragment;
import com.us.hotr.ui.view.CaseDetailView;
import com.us.hotr.ui.view.DoctorView;
import com.us.hotr.ui.view.HospitalView;
import com.us.hotr.ui.view.MassageView;
import com.us.hotr.ui.view.MasseurBigView;
import com.us.hotr.ui.view.PostDetailView;
import com.us.hotr.ui.view.ProductView;
import com.us.hotr.ui.view.SpaBigView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.UploadReplyRequest;
import com.us.hotr.webservice.response.GetCaseDetailResponse;
import com.us.hotr.webservice.response.GetPostDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Mloong on 2017/9/15.
 */

public class CaseActivity extends BaseLoadingActivity {
    public static final String PARAM_IS_NEW = "PARAM_IS_NEW";
    private int PRODUCT_OFFSET;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView tvCommentHint, tvPrice, tvPriceBefore, tvTitle;
    private ConstraintLayout clBanner;
    private ImageView ivFav, ivAvatar;

    private int type;
    private long id;
    private boolean isBannerShown = false;
    private int offset = 0;
    private boolean isFav, isNew = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        if(type == Constants.TYPE_POST)
            setMyTitle(R.string.content_title);
        else {
            setMyTitle(R.string.compare_title);
            ivShare.setVisibility(View.GONE);
        }
        id = getIntent().getExtras().getLong(Constants.PARAM_ID);
        isNew = getIntent().getExtras().getBoolean(PARAM_IS_NEW, false);
        PRODUCT_OFFSET = (int)Tools.dpToPx(this, 365);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(type == Constants.TYPE_POST)
            StatService.trackCustomKVEvent(this, Constants.MTA_ID_POST_SCREEN, new Properties());
        if(type == Constants.TYPE_CASE)
            StatService.trackCustomKVEvent(this, Constants.MTA_ID_CASE_SCREEN, new Properties());
    }

    @Override
    protected void loadData(int loadType) {
        if(type == Constants.TYPE_POST) {
            SubscriberListener mListener;
            mListener = new SubscriberListener<GetPostDetailResponse>() {
                @Override
                public void onNext(final GetPostDetailResponse result) {
                    isFav = (result.getHotTopic().getIs_collect() == 1)?true:false;
                    if(isFav)
                        ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                    else
                        ivFav.setImageResource(R.mipmap.ic_fav_text);
                    if(result.getHotTopic().getIs_comment() == 1) {
                        tvCommentHint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CommentDialogFragment.Builder alertDialogBuilder = new CommentDialogFragment.Builder(CaseActivity.this);
                                alertDialogBuilder.setPostButton(new CommentDialogFragment.PostListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, String content) {
                                        dialog.dismiss();
                                        uploadComment(content);
                                    }
                                });
                                alertDialogBuilder.setCancelButton(new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialogBuilder.create().show();
                            }
                        });
                    }else {
                        tvCommentHint.setGravity(Gravity.CENTER);
                        tvCommentHint.setText(R.string.no_comment_allowed);
                    }
                    ivShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Share share = new Share();
                            share.setDescription(getString(R.string.share_post));
                            if(result.getHotTopic().getIsOfficial()==1)
                                share.setImageUrl(result.getHotTopic().getShow_img());
                            else
                                share.setImageUrl(Tools.validatePhotoString(result.getHotTopic().getContentImg()).split("\\s*,\\s*")[0]);
                            share.setTitle(result.getHotTopic().getTitle());
                            share.setUrl(Constants.SHARE_URL + "#/invitation?id="+result.getHotTopic().getId());
                            share.setSinaContent(getString(R.string.share_post));
                            share.setType(Share.TYPE_NORMAL);
                            ShareDialogFragment.newInstance(share).show(getSupportFragmentManager(), "dialog");
                        }
                    });
                    mAdapter = new MyAdapter(CaseActivity.this, result);
                    MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                    myBaseAdapter.setFooterView();
                    mRecyclerView.setAdapter(myBaseAdapter);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getPostDetail(new LoadingSubscriber(mListener, this),
                        id, HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getPostDetail(new ProgressSubscriber(mListener, this),
                        id, HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getPostDetail(new SilentSubscriber(mListener, this, refreshLayout),
                        id, HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID());
        }
        if(type == Constants.TYPE_CASE) {
            SubscriberListener mListener;
            mListener = new SubscriberListener<GetCaseDetailResponse>() {
                @Override
                public void onNext(GetCaseDetailResponse result) {
                    tvTitle.setText(getString(R.string.bracket_left)+result.getProduct().getProduct_name()+getString(R.string.bracket_right)+result.getProduct().getProduct_usp());
                    tvPrice.setText(new DecimalFormat("0.00").format(result.getProduct().getOnline_price()));
                    tvPriceBefore.setText(getString(R.string.money)+new DecimalFormat("0.00").format(result.getProduct().getShop_price()));
                    tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                    Glide.with(CaseActivity.this).load(result.getProduct().getProduct_main_img()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
                    isFav = (result.getIs_collect_contrast_photo() == 1)?true:false;
                    if(isFav)
                        ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                    else
                        ivFav.setImageResource(R.mipmap.ic_fav_text);
                    if(result.getYmContrastPhoto().getIsComment() == 1) {
                        tvCommentHint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CommentDialogFragment.Builder alertDialogBuilder = new CommentDialogFragment.Builder(CaseActivity.this);
                                alertDialogBuilder.setPostButton(new CommentDialogFragment.PostListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, String content) {
                                        dialog.dismiss();
                                        uploadComment(content);
                                    }
                                });
                                alertDialogBuilder.setCancelButton(new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialogBuilder.create().show();
                            }
                        });
                    }else {
                        tvCommentHint.setGravity(Gravity.CENTER);
                        tvCommentHint.setText(R.string.no_comment_allowed);
                    }
                    mAdapter = new MyAdapter(CaseActivity.this, result);
                    MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                    myBaseAdapter.setFooterView();
                    mRecyclerView.setAdapter(myBaseAdapter);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getCaseDetail(new LoadingSubscriber(mListener, this),
                        id, HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getCaseDetail(new ProgressSubscriber(mListener, this),
                        id, HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCaseDetail(new SilentSubscriber(mListener, this, refreshLayout),
                        id, HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_compare;
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvCommentHint = (TextView) findViewById(R.id.tv_comment);
        clBanner = (ConstraintLayout) findViewById(R.id.cl_banner);
        ivFav = (ImageView) findViewById(R.id.iv_fav);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPrice = (TextView) findViewById(R.id.tv_amount);
        tvPriceBefore = (TextView) findViewById(R.id.tv_price_before);
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteIt();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offset = offset + dy;
                if(offset >= PRODUCT_OFFSET && !isBannerShown)
                    showBanner(true);
                if(offset < PRODUCT_OFFSET && isBannerShown)
                    showBanner(false);
            }
        });

        enableLoadMore(false);
    }

    private void favoriteIt(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                isFav = !isFav;
                if(isFav) {
                    ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                    Tools.Toast(CaseActivity.this, getString(R.string.fav_item_success));
                }
                else {
                    ivFav.setImageResource(R.mipmap.ic_fav_text);
                    Tools.Toast(CaseActivity.this, getString(R.string.remove_fav_item_success));
                }
            }
        };
        if(type == Constants.TYPE_POST) {
            if (isFav)
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), Arrays.asList(id), 8);
            else
                ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id, 8);
        }
        if(type == Constants.TYPE_CASE) {
            if (isFav)
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), Arrays.asList(id), 7);
            else
                ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id, 7);
        }

    }

    private void uploadComment(String content){
        if(type == Constants.TYPE_POST){
            SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<GetPostDetailResponse>() {
                @Override
                public void onNext(GetPostDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.comment_success));
                    mAdapter.setData(result);
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                }

                @Override
                public void reload() {
                    loadData(Constants.LOAD_DIALOG);
                }
            };
            ServiceClient.getInstance().uploadPostComment(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id, content);
        }
        if(type == Constants.TYPE_CASE){
            SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<GetCaseDetailResponse>() {
                @Override
                public void onNext(GetCaseDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.comment_success));
                    mAdapter.setData(result);
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                }

                @Override
                public void reload() {
                    loadData(Constants.LOAD_DIALOG);
                }
            };
            ServiceClient.getInstance().uploadCaseComment(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id, content);
        }
    }

    private void deleteComment(long commentId){
        if(type == Constants.TYPE_POST) {
            SubscriberListener mListener = new SubscriberListener<GetPostDetailResponse>() {
                @Override
                public void onNext(GetPostDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.delete_success));
                    mAdapter.setData(result);
                }
            };
            ServiceClient.getInstance().deletePostComment(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), commentId, id);
        }
        if(type == Constants.TYPE_CASE) {
            SubscriberListener mListener = new SubscriberListener<GetCaseDetailResponse>() {
                @Override
                public void onNext(GetCaseDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.delete_success));
                    mAdapter.setData(result);
                }
            };
            ServiceClient.getInstance().deleteCaseComment(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), commentId, id);
        }
    }

    private void likeComment(final Comment comment, final TextView tvNumber){
        SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
            @Override
            public void onNext(String result) {
                Tools.Toast(CaseActivity.this, getString(R.string.like_success));
                comment.setIs_like(1);
                tvNumber.setText((Integer.parseInt(tvNumber.getText().toString())+1)+"");
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan_ed);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvNumber.setCompoundDrawables(null, null, drawable, null);
                tvNumber.setOnClickListener(null);
            }

            @Override
            public void reload() {
                loadData(Constants.LOAD_DIALOG);
            }
        };
        if(type == Constants.TYPE_POST)
            ServiceClient.getInstance().likePostComment(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), comment.getId());
        if(type == Constants.TYPE_CASE)
            ServiceClient.getInstance().likeCaseComment(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), comment.getId());
    }

    private void uploadReply(UploadReplyRequest replyRequest){
        if(type == Constants.TYPE_POST) {
            SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<GetPostDetailResponse>() {
                @Override
                public void onNext(GetPostDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.comment_success));
                    mAdapter.setData(result);
                }

                @Override
                public void reload() {
                    loadData(Constants.LOAD_DIALOG);
                }
            };
            ServiceClient.getInstance().uploadPostReply(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), replyRequest);
        }
        if(type == Constants.TYPE_CASE) {
            SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<GetCaseDetailResponse>() {
                @Override
                public void onNext(GetCaseDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.comment_success));
                    mAdapter.setData(result);
                }

                @Override
                public void reload() {
                    loadData(Constants.LOAD_DIALOG);
                }
            };
            ServiceClient.getInstance().uploadCaseReply(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), replyRequest);
        }
    }

    private void deleteReply(long replyId){
        if(type == Constants.TYPE_POST) {
            SubscriberListener mListener = new SubscriberListener<GetPostDetailResponse>() {
                @Override
                public void onNext(GetPostDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.delete_success));
                    mAdapter.setData(result);
                }
            };
            ServiceClient.getInstance().deletePostReply(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), replyId, id);
        }
        if(type == Constants.TYPE_CASE) {
            SubscriberListener mListener = new SubscriberListener<GetCaseDetailResponse>() {
                @Override
                public void onNext(GetCaseDetailResponse result) {
                    Tools.Toast(CaseActivity.this, getString(R.string.delete_success));
                    mAdapter.setData(result);
                }
            };
            ServiceClient.getInstance().deletePostReply(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), replyId, id);
        }
    }

    private void showBanner(boolean isShow){
        if(type != Constants.TYPE_CASE)
            return;
        if(isShow) {
            isBannerShown = true;
            Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_menu_in);
            animIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            clBanner.setVisibility(View.VISIBLE);
            clBanner.startAnimation(animIn);
        }
        else {
            isBannerShown = false;
            Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_menu_out);
            animOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clBanner.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            clBanner.startAnimation(animOut);
        }
        clBanner.setAlpha(0.94F);
    }

    @Override
    public void onBackPressed() {
        if(type == Constants.TYPE_CASE && isNew){
            Intent intent = new Intent(CaseActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else
            super.onBackPressed();
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int TYPE_CASE = 101;
        private final int TYPE_POST = 102;
        private final int TYPE_POST_TEXT = 121;
        private final int TYPE_POST_IMAGE = 122;
        private final int TYPE_INTERVIEW= 113;
        private final int TYPE_COMMENT = 104;
        private final int TYPE_DOCTOR = 105;
        private final int TYPE_PRODUCT = 106;
        private final int TYPE_HOSPITAL = 107;
        private final int TYPE_SPA = 108;
        private final int TYPE_MASSAGE = 109;
        private final int TYPE_MASSEUR = 110;
        private final int TYPE_DOCTOR_HEADER =111;
        private final int TYPE_HOSPITAL_HEADER =115;
        private final int TYPE_PRODUCT_HEADER =116;
        private final int TYPE_MASSAGE_HEADER =117;
        private final int TYPE_MASSEUR_HEADER =118;
        private final int TYPE_SPA_HEADER =119;
        private final int TYPE_COMMENT_HEADER = 112;
        private final int TYPE_NO_COMMENT = 114;

        private List<Item> itemList = new ArrayList<>();
        private Context mContext;

        public MyAdapter(Context mContext, GetPostDetailResponse response) {
            this.mContext = mContext;
            initData(response);
        }

        public MyAdapter(Context mContext, GetCaseDetailResponse response){
            this.mContext = mContext;
            initData(response);
        }

        public void setData(GetPostDetailResponse response){
            initData(response);
            notifyDataSetChanged();
        }

        public void setData(GetCaseDetailResponse response){
            initData(response);
            notifyDataSetChanged();
        }

        private void initData(GetPostDetailResponse response){
            itemList.clear();
            if (response.getHotTopic() != null) {
                itemList.add(new Item(TYPE_POST, response.getHotTopic()));
//                if(response.getHotTopic().getIs_new() == 0){
//                    String content = response.getHotTopic().getContent();
//                    content = content.replace("&quot;", "\"").replace("<p>", "").replace("</p>", "");
//                    List<PostOld> postOldList = new Gson().fromJson(content, new TypeToken<List<PostOld>>(){}.getType());
//                    for(PostOld postOld:postOldList) {
//                        if (postOld.getStatus() == 1) {
//                            if(postOld.getType() == 1)
//                                itemList.add(new Item(TYPE_POST_IMAGE, postOld));
//                            if(postOld.getType() == 0)
//                                itemList.add(new Item(TYPE_POST_TEXT, postOld));
//                        }
//                    }
//                }
            }
            if(response.getLink_ammerchant_list()!=null && response.getLink_ammerchant_list().size()>0){
                itemList.add(new Item(TYPE_SPA_HEADER));
                for(Spa s:response.getLink_ammerchant_list())
                    itemList.add(new Item(TYPE_SPA, s));
            }
            if(response.getLink_amproduct_list()!=null && response.getLink_amproduct_list().size()>0){
                itemList.add(new Item(TYPE_MASSAGE_HEADER));
                for(Massage s:response.getLink_amproduct_list())
                    itemList.add(new Item(TYPE_MASSAGE, s));
            }
            if(response.getLink_technician_list()!=null && response.getLink_technician_list().size()>0){
                itemList.add(new Item(TYPE_MASSEUR_HEADER));
                for(Masseur s:response.getLink_technician_list())
                    itemList.add(new Item(TYPE_MASSEUR, s));
            }
            if(response.getLink_doctor_list()!=null && response.getLink_doctor_list().size()>0){
                itemList.add(new Item(TYPE_DOCTOR_HEADER));
                for(Doctor s:response.getLink_doctor_list())
                    itemList.add(new Item(TYPE_DOCTOR, s));
            }
            if(response.getLink_hospital_list()!=null && response.getLink_hospital_list().size()>0){
                itemList.add(new Item(TYPE_HOSPITAL_HEADER));
                for(Hospital s:response.getLink_hospital_list())
                    itemList.add(new Item(TYPE_HOSPITAL, s));
            }
            if(response.getLink_ymproduct_list()!=null && response.getLink_ymproduct_list().size()>0){
                itemList.add(new Item(TYPE_PRODUCT_HEADER));
                for(Product s:response.getLink_ymproduct_list())
                    itemList.add(new Item(TYPE_PRODUCT, s));
            }
            if(response.getHotTopic()!=null && response.getHotTopic().getIsOfficial() == 1)
                itemList.add(new Item(TYPE_COMMENT_HEADER));
            if(response.getListComment()!=null && response.getListComment().size()>0){
                for(Comment s:response.getListComment())
                    itemList.add(new Item(TYPE_COMMENT, s));
            }else
                itemList.add(new Item(TYPE_NO_COMMENT));
        }

        private void initData(GetCaseDetailResponse response){
            itemList.clear();
            if (response.getYmContrastPhoto() != null)
                itemList.add(new Item(TYPE_CASE, response));
            if(response.getYmContrastPhotoComment()!=null && response.getYmContrastPhotoComment().size()>0){
                for(Comment s:response.getYmContrastPhotoComment())
                    itemList.add(new Item(TYPE_COMMENT, s));
            }else
                itemList.add(new Item(TYPE_NO_COMMENT));
        }

        public class CaseDetailHolder extends RecyclerView.ViewHolder {
            CaseDetailView caseDetailView;
            public CaseDetailHolder(View view){
                super(view);
                caseDetailView = (CaseDetailView) view;
            }
        }

        public class PostHolder extends RecyclerView.ViewHolder {
            PostDetailView postDetailView;
            public PostHolder(View view) {
                super(view);
                postDetailView = (PostDetailView) view;
            }
        }

        public class PostTextHolder extends RecyclerView.ViewHolder {
            TextView tvText;
            public PostTextHolder(View itemView) {
                super(itemView);
                tvText = (TextView) itemView.findViewById(R.id.tv_content);
            }
        }

        public class PostImageHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            public PostImageHolder(View itemView) {
                super(itemView);
                ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            }
        }

        public class WebHolder extends RecyclerView.ViewHolder {
            WebView wvContent;
            public WebHolder(View itemView) {
                super(itemView);
                wvContent = (WebView) itemView.findViewById(R.id.wv_content);
            }
        }

        public class CommentHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;
            TextView tvContent, tvName, tvTime, tvDelete, tvLike;
            ImageView ivAvatar, ivReply;
            ConstraintLayout clUser;
            public CommentHolder(View view){
                super(view);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTime = (TextView) view.findViewById(R.id.tv_time);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                ivReply = (ImageView) view.findViewById(R.id.iv_reply);
                tvDelete = (TextView) view.findViewById(R.id.tv_delete);
                tvLike = (TextView) view.findViewById(R.id.tv_like);
                clUser =(ConstraintLayout) view.findViewById(R.id.cl_user);
            }
        }

        public class ProductHolder extends RecyclerView.ViewHolder {
            ProductView productView;
            public ProductHolder(View view) {
                super(view);
                productView = (ProductView) view;
            }
        }

        public class MassageHolder extends RecyclerView.ViewHolder {
            MassageView massageView;
            public MassageHolder(View view) {
                super(view);
                massageView = (MassageView) view;
            }
        }

        public class DoctorHolder extends RecyclerView.ViewHolder {
            DoctorView doctorView;
            public DoctorHolder(View view) {
                super(view);
                doctorView = (DoctorView) view;
            }
        }

        public class HospitalHolder extends RecyclerView.ViewHolder {
            HospitalView hospitalView;
            public HospitalHolder(View view) {
                super(view);
                hospitalView = (HospitalView) view;
            }
        }

        public class MasseurHolder extends RecyclerView.ViewHolder {
            MasseurBigView masseurBigView;
            public MasseurHolder(View view) {
                super(view);
                masseurBigView = (MasseurBigView) view;
            }
        }

        public class SpaHolder extends RecyclerView.ViewHolder {
            SpaBigView spaBigView;
            public SpaHolder(View view) {
                super(view);
                spaBigView = (SpaBigView) view;
            }
        }

        public class PromoHeaderHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public PromoHeaderHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public class CommentHeaderHolder extends RecyclerView.ViewHolder {

            public CommentHeaderHolder(View view) {
                super(view);
            }
        }

        public class NoCommentHolder extends RecyclerView.ViewHolder {

            public NoCommentHolder(View view) {
                super(view);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_CASE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case_detail, parent, false);
                    return new CaseDetailHolder(view);
                case TYPE_COMMENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                    return new CommentHolder(view);
                case TYPE_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_detail, parent, false);
                    return new PostHolder(view);
                case TYPE_POST_IMAGE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_image, parent, false);
                    return new PostImageHolder(view);
                case TYPE_POST_TEXT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_text, parent, false);
                    return new PostTextHolder(view);
                case TYPE_INTERVIEW:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web, parent, false);
                    return new WebHolder(view);
                case TYPE_PRODUCT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                    return new ProductHolder(view);
                case TYPE_MASSAGE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_massage, parent, false);
                    return new MassageHolder(view);
                case TYPE_DOCTOR:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
                    return new DoctorHolder(view);
                case TYPE_HOSPITAL:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
                    return new HospitalHolder(view);
                case TYPE_MASSEUR:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur_big, parent, false);
                    return new MasseurHolder(view);
                case TYPE_SPA:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa_big, parent, false);
                    return new SpaHolder(view);
                case TYPE_PRODUCT_HEADER:
                case TYPE_MASSAGE_HEADER:
                case TYPE_MASSEUR_HEADER:
                case TYPE_HOSPITAL_HEADER:
                case TYPE_DOCTOR_HEADER:
                case TYPE_SPA_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_text, parent, false);
                    return new PromoHeaderHolder(view);
                case TYPE_NO_COMMENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_comment, parent, false);
                    return new NoCommentHolder(view);
                case TYPE_COMMENT_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_title, parent, false);
                    return new CommentHeaderHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case TYPE_CASE:
                    CaseDetailHolder caseDetailHolder = (CaseDetailHolder) holder;
                    final GetCaseDetailResponse response = (GetCaseDetailResponse)itemList.get(position).getContent();
                    caseDetailHolder.caseDetailView.setData(response);
                    break;
                case TYPE_INTERVIEW:
//                    WebView postHolder = (PostHolder) holder;
//                    WebSettings settings = web.getSettings();
//                    settings.setJavaScriptEnabled(true);
//                    settings.setDomStorageEnabled(true);
//                    web.loadUrl(url);
                    break;
                case TYPE_POST:
                    PostHolder postHolder = (PostHolder) holder;
                    final Post post = (Post)itemList.get(position).getContent();
                    postHolder.postDetailView.setData(post);
                    break;
                case TYPE_POST_IMAGE:
                    PostImageHolder postImageHolder = (PostImageHolder) holder;
                    final PostOld postOld = (PostOld)itemList.get(position).getContent();
                    Glide.with(CaseActivity.this).load(postOld.getImageURL()).into(postImageHolder.ivImage);
                    break;
                case TYPE_POST_TEXT:
                    PostTextHolder postTextHolder = (PostTextHolder) holder;
                    final PostOld postOld2 = (PostOld)itemList.get(position).getContent();
                    postTextHolder.tvText.setText(postOld2.getEditContent());
                    break;
                case TYPE_COMMENT:
                    final CommentHolder commentHolder = (CommentHolder) holder;
                    final Comment comment = (Comment) itemList.get(position).getContent();
                    commentHolder.tvContent.setText(comment.getContent());
                    commentHolder.tvName.setText(comment.getNick_name());
                    commentHolder.tvTime.setText(Tools.getPostTime(CaseActivity.this, comment.getCreate_time()));
                    Glide.with(CaseActivity.this).load(comment.getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(commentHolder.ivAvatar);
                    User user = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
                    if(user != null && comment.getUser_id() == user.getUserId()){
                        commentHolder.tvDelete.setVisibility(View.VISIBLE);
                        commentHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteComment(comment.getId());
                            }
                        });
                    }else
                        commentHolder.tvDelete.setVisibility(View.GONE);
                    commentHolder.tvLike.setText(comment.getLike_cnt()+"");
                    if(comment.getIs_like() == 1){
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan_ed);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        commentHolder.tvLike.setCompoundDrawables(null, null, drawable, null);
                    }else{
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        commentHolder.tvLike.setCompoundDrawables(null, null, drawable, null);
                        commentHolder.tvLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(comment.getIs_like()==1)
                                    Tools.Toast(CaseActivity.this, getString(R.string.already_liked));
                                else
                                    likeComment(comment, commentHolder.tvLike);
                            }
                        });
                    }
                    commentHolder.clUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(comment.getU_user_type() == Constants.USER_TYPE_MASSEUR && comment.getMassagist_id() > 0
                                    && HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo() != null
                                    && comment.getUser_id() != HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getUserId()){
                                Intent i = new Intent(mContext, MasseurActivity.class);
                                Bundle b = new Bundle();
                                b.putLong(Constants.PARAM_ID, comment.getMassagist_id());
                                i.putExtras(b);
                                mContext.startActivity(i);
                            }else {
                                Intent i = new Intent(mContext, FriendActivity.class);
                                Bundle b = new Bundle();
                                b.putLong(Constants.PARAM_ID, comment.getUser_id());
                                i.putExtras(b);
                                mContext.startActivity(i);
                            }
                        }
                    });
                    if(comment.getListReply()!=null && comment.getListReply().size()>0) {
                        commentHolder.recyclerView.setVisibility(View.VISIBLE);
                        commentHolder.ivReply.setVisibility(View.VISIBLE);
                        commentHolder.recyclerView.setLayoutManager(new LinearLayoutManager(CaseActivity.this));
                        commentHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                        ReplyAdapter replyAdapter = new ReplyAdapter(comment.getListReply());
                        commentHolder.recyclerView.setAdapter(replyAdapter);
                    }else {
                        commentHolder.recyclerView.setVisibility(View.GONE);
                        commentHolder.ivReply.setVisibility(View.GONE);
                    }
                    commentHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CommentDialogFragment.Builder alertDialogBuilder = new CommentDialogFragment.Builder(CaseActivity.this, comment.getNick_name());
                            alertDialogBuilder.setPostButton(new CommentDialogFragment.PostListener() {
                                @Override
                                public void onClick(DialogInterface dialog, String content) {
                                    dialog.dismiss();
                                    UploadReplyRequest replyRequest = new UploadReplyRequest();
                                    replyRequest.setCommentId(comment.getId());
                                    if(type ==  Constants.TYPE_POST)
                                        replyRequest.setHotTopicId(id);
                                    if(type == Constants.TYPE_CASE)
                                        replyRequest.setContrastPhotoId(id);
                                    replyRequest.setReplyToUserId(comment.getUser_id());
                                    replyRequest.setContent(content);
                                    uploadReply(replyRequest);
                                }
                            });
                            alertDialogBuilder.setCancelButton(new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialogBuilder.create().show();
                        }
                    });
                    break;
                case TYPE_HOSPITAL:
                    final Hospital hospital = (Hospital) itemList.get(position).getContent();
                    HospitalHolder hospitalHolder = (HospitalHolder) holder;
                    hospitalHolder.hospitalView.setData(hospital);
                    break;
                case TYPE_DOCTOR:
                    final Doctor doctor = (Doctor) itemList.get(position).getContent();
                    DoctorHolder doctorHolder = (DoctorHolder) holder;
                    doctorHolder.doctorView.setData(doctor);
                    break;
                case TYPE_PRODUCT:
                    final Product product = (Product) itemList.get(position).getContent();
                    ProductHolder productHolder = (ProductHolder) holder;
                    productHolder.productView.setData(product);
                    break;
                case TYPE_MASSAGE:
                    final Massage massage = (Massage) itemList.get(position).getContent();
                    MassageHolder massageHolder = (MassageHolder) holder;
                    massageHolder.massageView.setData(massage, -1);
                    break;
                case TYPE_MASSEUR:
                    final Masseur masseur = (Masseur) itemList.get(position).getContent();
                    MasseurHolder masseurHolder = (MasseurHolder) holder;
                    masseurHolder.masseurBigView.setData(masseur, -1);
                    break;
                case TYPE_SPA:
                    final Spa spa = (Spa) itemList.get(position).getContent();
                    SpaHolder spaHolder = (SpaHolder) holder;
                    spaHolder.spaBigView.setData(spa);
                    spaHolder.spaBigView.ShowQuery(false);
                    break;
                case TYPE_MASSAGE_HEADER:
                    ((PromoHeaderHolder) holder).textView.setText(getString(R.string.recommend_massage));
                    break;
                case TYPE_MASSEUR_HEADER:
                    ((PromoHeaderHolder) holder).textView.setText(getString(R.string.recommend_masseur));
                    break;
                case TYPE_DOCTOR_HEADER:
                    ((PromoHeaderHolder) holder).textView.setText(getString(R.string.recommend_doctor));
                    break;
                case TYPE_SPA_HEADER:
                    ((PromoHeaderHolder) holder).textView.setText(getString(R.string.recommend_spa));
                    break;
                case TYPE_HOSPITAL_HEADER:
                    ((PromoHeaderHolder) holder).textView.setText(getString(R.string.recommend_hospital));
                    break;
                case TYPE_PRODUCT_HEADER:
                    ((PromoHeaderHolder) holder).textView.setText(getString(R.string.recommend_product));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position).getId();
        }

        public class Item{
            private int id;
            private Object content;

            public Item(int id){
                this.id = id;
            }

            public Item(int id, Object content){
                this.id = id;
                this.content = content;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }
        }
    }

    public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyHolder> {
        private List<Reply> replyList;

        public class ReplyHolder extends RecyclerView.ViewHolder {
            TextView tvContent, tvDelete;
            public ReplyHolder(View view){
                super(view);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
                tvDelete = (TextView) view.findViewById(R.id.tv_delete);
            }
        }

        public ReplyAdapter(List<Reply> replyList) {
            this.replyList = replyList;
        }

        @Override
        public ReplyAdapter.ReplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment_comment, parent, false);

            return new ReplyHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ReplyHolder holder, final int position) {
            final Reply reply = replyList.get(position);
            String part1, part2;
            part1 = reply.getNick_name() + "：";
            if(reply.getReply_to_user_nick_name()!=null && !reply.getReply_to_user_nick_name().isEmpty())
                part2 = getString(R.string.reply1) + " " + reply.getReply_to_user_nick_name() + "：" + reply.getContent();
            else
                part2 = reply.getContent();
            SpannableString ss = new SpannableString(part1 + part2);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    if(reply.getU_user_type() == Constants.USER_TYPE_MASSEUR && reply.getMassagist_id() > 0
                            && HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo() != null
                            && reply.getUser_id() != HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getUserId()){
                        Intent i = new Intent(CaseActivity.this, MasseurActivity.class);
                        Bundle b = new Bundle();
                        b.putLong(Constants.PARAM_ID, reply.getMassagist_id());
                        i.putExtras(b);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(CaseActivity.this, FriendActivity.class);
                        Bundle b = new Bundle();
                        b.putLong(Constants.PARAM_ID, reply.getUser_id());
                        i.putExtras(b);
                        startActivity(i);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, 0, part1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_black)), 0, part1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    CommentDialogFragment.Builder alertDialogBuilder = new CommentDialogFragment.Builder(CaseActivity.this, reply.getNick_name());
                    alertDialogBuilder.setPostButton(new CommentDialogFragment.PostListener() {
                        @Override
                        public void onClick(DialogInterface dialog, String content) {
                            dialog.dismiss();
                            UploadReplyRequest replyRequest = new UploadReplyRequest();
                            replyRequest.setCommentId(reply.getComment_id());
                            if(type ==  Constants.TYPE_POST)
                                replyRequest.setHotTopicId(id);
                            if(type == Constants.TYPE_CASE)
                                replyRequest.setContrastPhotoId(id);
                            replyRequest.setReplyToUserId(reply.getUser_id());
                            replyRequest.setReplyToId(reply.getId());
                            replyRequest.setContent(content);
                            uploadReply(replyRequest);
                        }
                    });
                    alertDialogBuilder.setCancelButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.create().show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, part1.length(), part1.length()+part2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_grey2)), part1.length(), part1.length()+part2.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvContent.setText(ss);
            holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvContent.setHighlightColor(Color.TRANSPARENT);
            User user = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
            if(user != null && reply.getUser_id() == user.getUserId()){
                holder.tvDelete.setVisibility(View.VISIBLE);
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteReply(reply.getId());
                    }
                });
            }else
                holder.tvDelete.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            if(replyList == null)
                return  0;
            else
                return replyList.size();
        }
    }
}
