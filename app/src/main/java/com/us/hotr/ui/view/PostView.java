package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.customview.ScrollThroughRecyclerView;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.PostOld;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liaobo on 2017/12/27.
 */

public class PostView extends FrameLayout {
    private RecyclerView recyclerView;
    private ImageView ivDelete, ivUserAvatar, ivPic;
    private TextView tvTitle, tvUserName, tvCertified, tvPostTime, tvFollowUser, tvContent, tvSubject, tvRead, tvComment, tvLike;
    private ConstraintLayout clInterestedSubject;
    private PicGridAdapter picAdapter;

    private Post post;
    private boolean isLiked;
    private ItemSelectedListener itemSelectedListener;

    public PostView(Context context) {
        super(context);
        init();
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_post,this);
        recyclerView = (ScrollThroughRecyclerView) findViewById(R.id.recyclerview);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivUserAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCertified = (TextView) findViewById(R.id.tv_certified);
        tvPostTime = (TextView) findViewById(R.id.tv_time);
        tvFollowUser = (TextView) findViewById(R.id.tv_follow_user);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvUserName = (TextView) findViewById(R.id.tv_name);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvRead = (TextView) findViewById(R.id.tv_read);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvLike = (TextView) findViewById(R.id.tv_like);
        clInterestedSubject = (ConstraintLayout) findViewById(R.id.cl_subject);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
    }

    public void setData(final Post post){
        this.post = post;
        isLiked = post.getIs_like()==1?true:false;
        Glide.with(getContext()).load(post.getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivUserAvatar);
        tvTitle.setText(post.getTitle());
        switch (post.getUser_type()) {
            case 1:
            case 7:
                tvCertified.setVisibility(GONE);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                tvCertified.setVisibility(VISIBLE);
                tvCertified.setText(R.string.certify);
                break;
            case 6:
                tvCertified.setVisibility(VISIBLE);
                tvCertified.setText(R.string.official);
                break;
        }
        if(post.getCreate_time()!=null)
            tvPostTime.setText(Tools.getPostTime(getContext(), post.getCreate_time()));
//        if(post.getIs_attention() == 1){
//            tvFollowUser.setVisibility(VISIBLE);
//            tvFollowUser.setText(R.string.fav_ed);
//            tvFollowUser.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
//        }else{
//            if(HOTRSharePreference.getInstance(getContext()).getUserInfo()!= null
//                    && post.getUser_id() == HOTRSharePreference.getInstance(getContext()).getUserInfo().getUserId())
//                tvFollowUser.setVisibility(GONE);
//            else {
//                tvFollowUser.setVisibility(VISIBLE);
//                tvFollowUser.setText(R.string.guanzhu);
//                tvFollowUser.setTextColor(getContext().getResources().getColor(R.color.text_black));
//                tvFollowUser.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        followUser(post.getUser_id());
//                    }
//                });
//            }
//        }

        List<String> photoes = new ArrayList<>();
        if(post.getIs_new() == 1){
            if(post.getIsOfficial() != 1){
                if (post.getContentImg() != null)
                    photoes = Arrays.asList(Tools.validatePhotoString(post.getContentImg()).split("\\s*,\\s*"));
            }
        }else{
            if(post.getUser_type() != 6)
                post.setIsOfficial(0);
            String content = post.getContent();
            content = content.replace("&quot;", "\"").replace("<p>", "").replace("</p>", "");
            List<PostOld> postOldList = new Gson().fromJson(content, new TypeToken<List<PostOld>>() {}.getType());
            boolean found = false;
            for (PostOld postOld : postOldList) {
                if (postOld.getStatus() == 1 && postOld.getType() == 1 && postOld.getImageURL() != null)
                    photoes.add(postOld.getImageURL());
                if(postOld.getStatus() == 1 && postOld.getType() == 0 && !postOld.getEditContent().isEmpty() && !found){
                    try {
                        post.setContentWord(URLDecoder.decode(postOld.getEditContent(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    found = true;
                }
            }
            if(photoes.size()>0) {
                if (post.getIsOfficial() == 1) {
                    post.setShow_img(photoes.get(0));
                }
            }
        }

        if(post.getIsOfficial() != 1){
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(post.getContentWord());
        }
        else
            tvContent.setVisibility(GONE);
        tvUserName.setText(post.getNick_name());
        if(post.getIsOfficial() != 1 && post.getListCoshow()!= null && post.getListCoshow().size()>0){
            clInterestedSubject.setVisibility(VISIBLE);
            tvSubject.setText(post.getListCoshow().get(0).getCoshow_name());
            tvSubject.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), GroupDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, post.getListCoshow().get(0).getId());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
        else
            clInterestedSubject.setVisibility(GONE);
        tvRead.setText(post.getRead_cnt()+"");
        tvComment.setText(post.getComment_cnt()+"");
        tvLike.setText(post.getLike_cnt()+"");
        if(post.getIs_like() == 1) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan_ed);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
        }
        else {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
        }
        tvLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLiked)
                    likePost();
                else
                    Tools.Toast(getContext(), getContext().getString(R.string.already_liked));
            }
        });
        if(post.getIsOfficial() == 1){
            recyclerView.setVisibility(GONE);
            ivPic.setVisibility(VISIBLE);
            if(post.getShow_img()!=null){
                Glide.with(getContext()).load(post.getShow_img()).dontAnimate().error(R.drawable.placeholder_post1).placeholder(R.drawable.placeholder_post1).into(ivPic);
            }else
                ivPic.setVisibility(GONE);
        }else {
            recyclerView.setVisibility(GONE);
            ivPic.setVisibility(GONE);
            if (photoes != null && photoes.size() > 0) {
                int column;
                if (photoes.size() == 1)
                    column = 1;
                else if (photoes.size() == 2 || photoes.size() == 4)
                    column = 2;
                else
                    column = 3;
                recyclerView.setVisibility(VISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), column));
                picAdapter = new PicGridAdapter(getContext(), photoes, false);
                recyclerView.setAdapter(picAdapter);
            } else
                recyclerView.setVisibility(GONE);
        }
        if(picAdapter!=null)
            picAdapter.notifyDataSetChanged();
//        setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gotoPost();
//            }
//        });
//        if(post.getIsOfficial() == 1){
//            recyclerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    readPost();
//                    Intent i = new Intent(getContext(), CaseActivity.class);
//                    Bundle b = new Bundle();
//                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
//                    b.putLong(Constants.PARAM_ID, post.getId());
//                    i.putExtras(b);
//                    getContext().startActivity(i);
//                }
//            });
//        }
    }

    private void gotoPost(){
        readPost();
        Intent i = new Intent(getContext(), CaseActivity.class);
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
        b.putLong(Constants.PARAM_ID, post.getId());
        i.putExtras(b);
        getContext().startActivity(i);
    }

    private void clickFav(){
        if ((boolean)PostView.this.getTag()) {
            ivDelete.setImageResource(R.mipmap.ic_delete_order);
            PostView.this.setTag(false);

        } else {
            ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
            PostView.this.setTag(true);
        }
        if(itemSelectedListener!=null)
            itemSelectedListener.onItemSelected((boolean)getTag());
    }

    private void followUser(long userId){

    }

    public void setItemSelectedListener(ItemSelectedListener listener){
        this.itemSelectedListener = listener;
    }

    private void likePost(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                isLiked = true;
                Tools.Toast(getContext(), getContext().getString(R.string.like_success));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan_ed);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvLike.setCompoundDrawables(drawable, null, null, null);
                tvLike.setText((Integer.parseInt(tvLike.getText().toString())+1)+"");
            }
        };
        ServiceClient.getInstance().likePost(new ProgressSubscriber(mListener, getContext()),
                HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), post.getId());
    }

    private void readPost(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
            }
        };
        ServiceClient.getInstance().readPost(new SilentSubscriber(mListener, getContext(), null), post.getId());
    }

    public void enableEdit(boolean isEdit){
        if (isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setImageResource(R.mipmap.ic_delete_order);
            setTag(false);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickFav();
                }
            });
        } else {
            ivDelete.setVisibility(View.GONE);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoPost();
                }
            });
        }
        if(picAdapter!=null)
            picAdapter.setEdit(isEdit);
    }

    public class PicGridAdapter extends RecyclerView.Adapter<PicGridAdapter.ViewHolder> {

        private Context mContext;
        private List<String> photoes;
        private boolean isOffical;
        private boolean iEdit;

        public PicGridAdapter(Context mContext, List<String> photoes, boolean isOffical) {
            this.photoes = photoes;
            this.mContext = getContext();
            this.isOffical = isOffical;
            this.iEdit = false;
        }

        public void setEdit(boolean iEdit){
            this.iEdit = iEdit;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if(photoes.size() == 1)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image1, parent, false);
            else
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(mContext).load(photoes.get(position)).dontAnimate().error(R.drawable.placeholder_post_2).placeholder(R.drawable.placeholder_post_2).into(holder.mImageView);
            if(!iEdit)
                holder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isOffical) {
                            readPost();
                            Intent i = new Intent(getContext(), ImageViewerActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.PARAM_DATA, (Serializable) photoes);
                            b.putInt(Constants.PARAM_ID, position);
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                            i.putExtras(b);
                            getContext().startActivity(i);
                        }
                        else
                            gotoPost();
                    }
                });
            else
                holder.mImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickFav();
                    }
                });

        }

        @Override
        public int getItemCount() {
            return photoes.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }
}
