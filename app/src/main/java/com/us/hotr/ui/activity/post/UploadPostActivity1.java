package com.us.hotr.ui.activity.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.UploadPostResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import id.zelory.compressor.Compressor;

/**
 * Created by Mloong on 2017/10/10.
 */

public class UploadPostActivity1 extends BaseActivity {

    private TextView tvPost;
    private EditText etContent, etTitle;
    private RecyclerView mRecyclerView;
//    private ImageView ivAddPic, ivAddVideo;
//    private ConstraintLayout clBar;
    private long groupId = -1;

    private List<MediaBean> photoList = new ArrayList<>();
    private MyAdapter mAdapter;

    private View.OnClickListener postListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(etTitle.getText().toString().trim().length()<4)
                Tools.Toast(getApplicationContext(), getString(R.string.min_4));
            else if(etContent.getText().toString().trim().length()<20)
                Tools.Toast(getApplicationContext(), getString(R.string.min_20));
            else if(photoList == null || photoList.size() == 0)
                Tools.Toast(getApplicationContext(), getString(R.string.at_least_1_pic));
            else {
                if(groupId == -1)
                    startActivityForResult(new Intent(UploadPostActivity1.this, UploadPostActivity2.class), 0);
                else
                    uploadPost(groupId);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            uploadPost(data.getExtras().getLong(Constants.PARAM_ID));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras()!=null)
            groupId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        setMyTitle(R.string.upload_post);
        initStaticView();
    }

    private void uploadPost(final long groupId){
        final List<String>  list = new ArrayList<>();
        if(photoList!=null && photoList.size()>0) {
            for (MediaBean mb : photoList) {
                File fromPic = new File(mb.getOriginalPath());
                try {
                    File toFile = new Compressor(UploadPostActivity1.this)
                            .setDestinationDirectoryPath(Tools.getZipFileName(fromPic.getName()))
                            .compressToFile(fromPic);
                    list.add(toFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SubscriberListener mListener = new SubscriberListener<UploadPostResponse>() {
                @Override
                public void onNext(UploadPostResponse result) {
                    for(String s:list){
                        File file = new File(s);
                        file.delete();
                    }
                    Tools.Toast(UploadPostActivity1.this, getString(R.string.post_success));
                    Intent i = new Intent(UploadPostActivity1.this, CaseActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                    b.putLong(Constants.PARAM_ID, result.getTopicId());
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
            };
            Post post = new Post();
            post.setTitle(etTitle.getText().toString().trim());
            post.setTopicType(0);
            post.setContentWord(etContent.getText().toString().trim());
            post.setIsOfficial(0);

            ServiceClient.getInstance().uploadPost(new ProgressSubscriber(mListener, UploadPostActivity1.this),
                    HOTRSharePreference.getInstance(UploadPostActivity1.this.getApplicationContext()).getUserID(), list, post, new ArrayList<Long>(){{add((long)groupId);}});
        }
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
//        ivAddPic = (ImageView) findViewById(R.id.iv_add_pic);
//        ivAddVideo = (ImageView) findViewById(R.id.iv_add_video);
//        clBar = (ConstraintLayout) findViewById(R.id.cl_bar);

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

//        ivAddPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openMulti();
//            }
//        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(photoList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void openMulti(){
        RxGalleryFinal rxGalleryFinal = RxGalleryFinal
                .with(UploadPostActivity1.this)
                .image()
                .crop(false)
                .multiple();
        if (photoList != null && !photoList.isEmpty()) {
            rxGalleryFinal
                    .selected(photoList);
        }
        rxGalleryFinal.maxSize(9)
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {

                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        photoList = imageMultipleResultEvent.getResult();
                        mAdapter = new MyAdapter(photoList);
                        mRecyclerView.setAdapter(mAdapter);
//                        if(photoList.size() == 9){
//                            clBar.setVisibility(View.GONE);
//                        }else{
//                            clBar.setVisibility(View.VISIBLE);
//                        }
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
                || etTitle.getText().toString().trim().isEmpty()) {
            tvPost.setTextColor(getResources().getColor(R.color.text_grey2));
            tvPost.setOnClickListener(null);
        }else{
            tvPost.setTextColor(getResources().getColor(R.color.cyan));
            tvPost.setOnClickListener(postListener);
        }
    }

    @Override
    public void onBackPressed() {
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.cancel_draft));
        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UploadPostActivity1.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
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
            if(position<photoList.size()) {
                final MediaBean mediaBean = photoList.get(position);
                Bitmap bitmap = Tools.decodeFile(mediaBean.getOriginalPath(), 226, 226);
                holder.ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.ivPic.setImageBitmap(bitmap);
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        photoList.remove(position);
                        notifyDataSetChanged();
//                        if(photoList.size() == 9){
//                            clBar.setVisibility(View.GONE);
//                        }else{
//                            clBar.setVisibility(View.VISIBLE);
//                        }
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
            if(picList!=null && picList.size()>=0){
                if(picList.size() == 9)
                    return 9;
                else
                    return picList.size() + 1;
            }else
                return 0;
        }
    }
}
