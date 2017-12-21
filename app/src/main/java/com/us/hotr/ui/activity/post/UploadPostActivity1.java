package com.us.hotr.ui.activity.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.CompareActivity;
import com.us.hotr.ui.dialog.CancelPostDialogFragment;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;

/**
 * Created by Mloong on 2017/10/10.
 */

public class UploadPostActivity1 extends BaseActivity {

    private TextView tvPost;
    private EditText etContent, etTitle;
    private RecyclerView mRecyclerView;
    private ImageView ivAddPic, ivAddVideo;
    private ConstraintLayout clBar;

    private List<MediaBean> list = new ArrayList<>();
    private MyAdapter mAdapter;

    private View.OnClickListener postListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(etTitle.getText().toString().trim().length()<4)
                Tools.Toast(getBaseContext(), getString(R.string.min_4));
            else if(etContent.getText().toString().trim().length()<20)
                Tools.Toast(getBaseContext(), getString(R.string.min_20));
            else {
                Intent i = new Intent(UploadPostActivity1.this, CompareActivity.class);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_POST);
                startActivity(i);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.upload_post);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_upload_post_1;
    }

    private void initStaticView() {

        tvPost = (TextView) findViewById(R.id.tv_edit);
        etContent = (EditText) findViewById(R.id.et_content);
        etTitle = (EditText) findViewById(R.id.et_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ivAddPic = (ImageView) findViewById(R.id.iv_add_pic);
        ivAddVideo = (ImageView) findViewById(R.id.iv_add_video);
        clBar = (ConstraintLayout) findViewById(R.id.cl_bar);

        tvPost.setText(R.string.post);
        tvPost.setTextColor(getResources().getColor(R.color.text_grey2));

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateSubmitButton();

            }
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateSubmitButton();

            }
        });

        ivAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMulti();
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void openMulti(){
        RxGalleryFinal rxGalleryFinal = RxGalleryFinal
                .with(UploadPostActivity1.this)
                .image()
                .multiple();
        if (list != null && !list.isEmpty()) {
            rxGalleryFinal
                    .selected(list);
        }
        rxGalleryFinal.maxSize(9)
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {

                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        list = imageMultipleResultEvent.getResult();
                        mAdapter = new MyAdapter(list);
                        mRecyclerView.setAdapter(mAdapter);
                        if(list.size() == 9){
                            clBar.setVisibility(View.GONE);
                        }else{
                            clBar.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                })
                .openGallery();
    }

    private void validateSubmitButton(){
        if(etContent.getText().toString().trim().isEmpty()
                || etTitle.getText().toString().trim().isEmpty()
                || list.size()>0) {
            tvPost.setTextColor(getResources().getColor(R.color.text_grey2));
            tvPost.setOnClickListener(null);
        }else{
            tvPost.setTextColor(getResources().getColor(R.color.cyan));
            tvPost.setOnClickListener(postListener);
        }
    }

    @Override
    public void onBackPressed() {
//        new CancelPostDialogFragment().show(getSupportFragmentManager(), "dialog");
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.cancel_draft));
        alertDialogBuilder.setPositiveButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UploadPostActivity1.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<MediaBean> picList;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPic, ivDelete;

            public MyViewHolder(View view) {
                super(view);
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            }
        }

        public MyAdapter(List<MediaBean> picList) {
            this.picList = picList;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_post_image, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if(position<list.size()) {
                final MediaBean mediaBean = list.get(position);
                Bitmap bitmap = Tools.decodeFile(mediaBean.getOriginalPath(), 226, 226);
                holder.ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.ivPic.setImageBitmap(bitmap);
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.remove(position);
                        notifyDataSetChanged();
                        if(list.size() == 9){
                            clBar.setVisibility(View.GONE);
                        }else{
                            clBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }else {
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivPic.setImageResource(R.mipmap.holder_post);
                holder.ivPic.setScaleType(ImageView.ScaleType.CENTER);
                holder.ivPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openMulti();
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            if(picList!=null && picList.size()>0){
                if(picList.size() == 9)
                    return 9;
                else
                    return picList.size() + 1;
            }else
                return 0;
        }
    }
}
