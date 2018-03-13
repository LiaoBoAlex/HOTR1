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
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ScrollThroughRecyclerView;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.ui.activity.info.FriendActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liaobo on 2018/1/11.
 */

public class PostDetailView extends FrameLayout {
    private RecyclerView recyclerView;
    private ImageView ivDelete, ivUserAvatar;
    private TextView tvTitle, tvUserName, tvCertified, tvPostTime, tvFollowUser, tvContent, tvSubject, tvRead, tvComment, tvLike, tvIntro;
    private WebView wvContent;
    private ConstraintLayout clUser;
    private PicGridAdapter picAdapter;

    private boolean isEdit = false;
    private boolean isFav, isLiked;

    public PostDetailView(Context context) {
        super(context);
        init();
    }

    public PostDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_post_detail,this);
        recyclerView = (ScrollThroughRecyclerView) findViewById(R.id.recyclerview);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivUserAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
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
        wvContent = (WebView) findViewById(R.id.wv_content);
        clUser = (ConstraintLayout) findViewById(R.id.cl_user);
        wvContent.getSettings().setJavaScriptEnabled(true);
    }

    public void setData(final Post post){
        isFav = post.getIs_collect()==1?true:false;
        isLiked = post.getIs_like()==1?true:false;
        Glide.with(getContext()).load(post.getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivUserAvatar);
        if(post.getIsOfficial() == 1)
            tvIntro.setText(post.getNick_name() + "  " + String.format(getContext().getString(R.string.publish_when), Tools.getPostTime(getContext(), post.getCreate_time())));
        else{
            if(post.getGender()!=null && post.getAge()!=null)
                tvIntro.setText(getResources().getStringArray(R.array.gender)[post.getGender()] + " | " + String.format(getContext().getString(R.string.age_number), post.getAge()) + " | " + post.getProvince_name());
        }

        clUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FriendActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, post.getUser_id());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });

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
        if(post.getIs_attention() == 1){
            tvFollowUser.setText(R.string.fav_ed);
            tvFollowUser.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
        }else{
            if(HOTRSharePreference.getInstance(getContext()).getUserInfo()!= null
                    && post.getUser_id() == HOTRSharePreference.getInstance(getContext()).getUserInfo().getUserId())
                tvFollowUser.setVisibility(GONE);
            else {
                tvFollowUser.setText(R.string.guanzhu);
                tvFollowUser.setTextColor(getContext().getResources().getColor(R.color.text_black));
            }
        }
        tvFollowUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser(post.getUser_id());
            }
        });
        tvTitle.setText(post.getTitle());
        if(post.getIsOfficial() != 1) {
            tvContent.setVisibility(VISIBLE);
            tvTitle.setVisibility(VISIBLE);
            tvPostTime.setVisibility(VISIBLE);
            wvContent.setVisibility(GONE);
            tvContent.setText(post.getContentWord());
            if(post.getCreate_time()!=null)
                tvPostTime.setText(Tools.getPostTime(getContext(), post.getCreate_time()));
        }else{
            tvContent.setVisibility(GONE);
            tvTitle.setVisibility(VISIBLE);
            tvPostTime.setVisibility(GONE);
            wvContent.setVisibility(VISIBLE);
            wvContent.loadData(Tools.getHtmlData(post.getContent()), "text/html; charset=UTF-8", null);
        }
        tvUserName.setText(post.getNick_name());
        if(post.getIsOfficial() != 1 && post.getListCoshow()!= null && post.getListCoshow().size()>0){
            tvSubject.setVisibility(VISIBLE);
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
            tvSubject.setVisibility(GONE);
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
                    likePost(post.getId());
                else
                    Tools.Toast(getContext(), getContext().getString(R.string.already_liked));
            }
        });
        if(post.getIsOfficial() != 1){
            List<String> photoes = new ArrayList<>();
            if(post.getContentImg()!=null)
                photoes = Arrays.asList(Tools.validatePhotoString(post.getContentImg()).split("\\s*,\\s*"));
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
                picAdapter = new PicGridAdapter(getContext(), photoes);
                recyclerView.setAdapter(picAdapter);
            } else
                recyclerView.setVisibility(GONE);
        }
    }

    private void followUser(final long userId){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                isFav = !isFav;
                if(isFav){
                    Tools.Toast(getContext(), getContext().getString(R.string.fav_people_success));
                    tvFollowUser.setText(R.string.fav_ed);
                    tvFollowUser.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
                }else{
                    Tools.Toast(getContext(), getContext().getString(R.string.remove_fav_people_success));
                    tvFollowUser.setText(R.string.guanzhu);
                    tvFollowUser.setTextColor(getContext().getResources().getColor(R.color.text_black));
                }
            }
        };
        if(isFav)
            ServiceClient.getInstance().deleteFavoritePeople(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), userId);
        else
            ServiceClient.getInstance().favoritePeople(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), userId);
    }

    private void likePost(long postId){
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
                HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), postId);
    }

    public class PicGridAdapter extends RecyclerView.Adapter<PicGridAdapter.ViewHolder> {

        private Context mContext;
        private List<String> photoes;

        public PicGridAdapter(Context mContext, List<String> photoes) {
            this.photoes = photoes;
            this.mContext = mContext;
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
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, ImageViewerActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, (Serializable)photoes);
                    b.putInt(Constants.PARAM_ID, position);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                    i.putExtras(b);
                    mContext.startActivity(i);
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

