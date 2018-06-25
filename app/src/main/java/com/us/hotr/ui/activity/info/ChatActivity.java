package com.us.hotr.ui.activity.info;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ChatSoftInputLayout;
import com.us.hotr.customview.PullRefreshLayout;
import com.us.hotr.customview.TipItem;
import com.us.hotr.customview.TipView;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.CameraActivity;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.rxjava.ApiException;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.ProgressUpdateCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liaobo on 2018/3/2.
 */

public class ChatActivity extends BaseActivity {
    private final int MESSAGE_PER_PAGE = 15;

    private ChatSoftInputLayout chatSoftInputLayout;
    private RecyclerView recyclerView;
    private PullRefreshLayout refreshLayout;
    private RelativeLayout rootView;

    private User user, me;
    private String meId, userId;
    private List<Message> messageList = new ArrayList<>();

    private MyAdapter mAdapter;
    private Conversation mConversation;


    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        userId = "user"+user.getUserId();
        if(Tools.isUserLogin(getApplicationContext())) {
            me = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
            meId = "user"+me.getUserId();
        }else{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        initStaticView();
        JMessageLogin();
        JMessageClient.registerEventReceiver(this);
    }


    private void initStaticView(){
        chatSoftInputLayout = (ChatSoftInputLayout) findViewById(R.id.chat_soft_input_layout);
        recyclerView = (RecyclerView) chatSoftInputLayout.findViewById(R.id.recyclerview);
        refreshLayout = (PullRefreshLayout) chatSoftInputLayout.findViewById(R.id.refreshLayout);
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        chatSoftInputLayout.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        chatSoftInputLayout.setOnPhotoClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallary();
            }
        });
        chatSoftInputLayout.setOnSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage();
            }
        });

        refreshLayout.setRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void refreshFinished() {
                loadData(mConversation.getMessagesFromNewest(messageList.size(), MESSAGE_PER_PAGE));
                refreshLayout.refreshFinished();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
//        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JMessageClient.enterSingleConversation(userId, "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        JMessageClient.exitConversation();
    }

    private void JMessageLogin(){
        SubscriberListener mListener = new SubscriberListener<List<Message>>() {
            @Override
            public void onNext(List<Message> result) {
                loadData(result);
            }
        };
        Observable.create(new ObservableOnSubscribe<List<Message>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Message>> emitter) throws Exception {
                if(JMessageClient.getMyInfo()!=null) {
                    mConversation = JMessageClient.getSingleConversation(userId);
                    if(mConversation == null)
                        mConversation =  Conversation.createSingleConversation(userId, "");
                    List<Message> list = mConversation.getMessagesFromNewest(messageList.size(), MESSAGE_PER_PAGE);
                    emitter.onNext(list);
                    emitter.onComplete();
//                    JMessageClient.getUserInfo(((UserInfo)mConversation.getTargetInfo()).getUserName(), new GetUserInfoCallback() {
//                        @Override
//                        public void gotResult(int i, String s, UserInfo userInfo) {
//                            user = new User();
//                            user.setUserId(getIntent().getExtras().getLong(Constants.PARAM_DATA));
//                            String name = userInfo.getNickname();
//                            if(name == null || name.isEmpty())
//                                name = userInfo.getUserName();
//                            user.setNickname(name);
//                            user.setHead_portrait(userInfo.getAddress());
//                            List<Message> list = mConversation.getMessagesFromNewest(messageList.size(), MESSAGE_PER_PAGE);
//                            emitter.onNext(list);
//                            emitter.onComplete();
//                        }
//                    });

                }else {
                    JMessageClient.login(meId, "123456", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i != 0) {
                                emitter.onError(new ApiException(i, getString(R.string.network_error)));
                            } else {
                                mConversation = JMessageClient.getSingleConversation(userId);
                                if (mConversation == null)
                                    mConversation = Conversation.createSingleConversation(userId, "");
                                List<Message> list = mConversation.getMessagesFromNewest(messageList.size(), MESSAGE_PER_PAGE);
                                emitter.onNext(list);
                                emitter.onComplete();
//                                JMessageClient.getUserInfo(((UserInfo)mConversation.getTargetInfo()).getUserName(), new GetUserInfoCallback() {
//                                    @Override
//                                    public void gotResult(int i, String s, UserInfo userInfo) {
//                                        user = new User();
//                                        user.setUserId(getIntent().getExtras().getLong(Constants.PARAM_DATA));
//                                        user.setNickname(userInfo.getNickname());
//                                        user.setHead_portrait(userInfo.getAddress());
//                                        List<Message> list = mConversation.getMessagesFromNewest(messageList.size(), MESSAGE_PER_PAGE);
//                                        emitter.onNext(list);
//                                        emitter.onComplete();
//                                    }
//                                });
                            }
                        }
                    });
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber(mListener, this));

    }

    private void loadData(List<Message> list){
        setMyTitle(user.getNickname());
        if(list!=null && list.size()>0) {
            messageList.addAll(list);
        }else
            refreshLayout.enableRefresh(false);
        if(mAdapter==null ) {
            mAdapter = new MyAdapter();
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(0);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void takePhoto(){
        startActivityForResult(new Intent(this, CameraActivity.class), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            sendPhotoMessage(data.getExtras().getString(Constants.PARAM_DATA));
        }
    }

    private void showGallary(){
        RxGalleryFinal
                .with(ChatActivity.this)
                .image()
                .radio()
                .hideCamera()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        sendPhotoMessage(imageRadioResultEvent.getResult().getOriginalPath());
                    }
                })
                .openGallery();
        RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
    }

    private void sendPhotoMessage(final String filePath){
        File fromPic = new File(filePath);
        try {
            final File toFile = new Compressor(ChatActivity.this)
                    .setDestinationDirectoryPath(Tools.getZipFileName(fromPic.getName()))
                    .compressToFile(fromPic);
            ImageContent.createImageContentAsync(toFile, new ImageContent.CreateImageContentCallback() {
                @Override
                public void gotResult(int status, String desc, ImageContent imageContent) {
                    if (status == 0) {
                        imageContent.setStringExtra(Constants.PARAM_DATA, toFile.getAbsolutePath());
                        final Message message = mConversation.createSendMessage(imageContent);
                        messageList.add(0, message);
                        mAdapter.notifyItemInserted(0);
                        recyclerView.scrollToPosition(0);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendTextMessage(){
        final Message message = mConversation.createSendMessage(new TextContent(chatSoftInputLayout.getEdittextContent()));
        chatSoftInputLayout.clearEdittextContent();
        messageList.add(0, message);
        mAdapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    public void onEvent(MessageEvent event) {
        final Message message = event.getMessage();
        mConversation.resetUnreadCount();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(0, message);
                mAdapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
            }
        });

    }

    public void onEvent(OfflineMessageEvent event) {
        mConversation.resetUnreadCount();
        String userName = ((UserInfo)event.getConversation().getTargetInfo()).getUserName();
        if(userId.equals(userName)){
            for(Message m:event.getOfflineMessageList()){
                messageList.add(m);
                mAdapter.notifyItemInserted(0);
            }
            recyclerView.scrollToPosition(0);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int TYPE_RECEIVE_TEXT = 1;
        private final int TYPE_SEND_TEXT= 2;
        private final int TYPE_RECEIVE_PHOTO = 3;
        private final int TYPE_SEND_PHOTO = 4;

        private boolean isFirstOne = true;

        public class BaseHolder extends RecyclerView.ViewHolder {
            TextView tvTime;
            public BaseHolder(View view) {
                super(view);
                tvTime = (TextView) view.findViewById(R.id.tv_date);
            }
        }

        public class Text1Holder extends BaseHolder {
            TextView tvContent;
            ImageView ivAvatar;
            public Text1Holder(View view) {
                super(view);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
            }
        }

        public class Text2Holder extends BaseHolder {
            TextView tvContent;
            ImageView ivAvatar, ivError;
            CircularProgressView progressView;
            public Text2Holder(View view) {
                super(view);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
                ivError = (ImageView) view.findViewById(R.id.iv_error);
                progressView = (CircularProgressView) view.findViewById(R.id.progressBar);
            }
        }

        public class Photo1Holder extends BaseHolder {
            ImageView ivAvatar, ivContent;
            public Photo1Holder(View view) {
                super(view);
                ivContent = (ImageView) view.findViewById(R.id.iv_content);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
            }
        }

        public class Photo2Holder extends BaseHolder {
            ImageView ivAvatar, ivContent, ivError, ivDim;
            CircularProgressView progressView;
            TextView tvProgress;
            public Photo2Holder(View view) {
                super(view);
                ivContent = (ImageView) view.findViewById(R.id.iv_content);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
                ivError = (ImageView) view.findViewById(R.id.iv_error);
                ivDim = (ImageView) view.findViewById(R.id.iv_dim);
                progressView = (CircularProgressView) view.findViewById(R.id.progressBar);
                tvProgress = (TextView) view.findViewById(R.id.tv_progress);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_RECEIVE_TEXT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text1, parent, false);
                    return new Text1Holder(view);
                case TYPE_SEND_TEXT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text2, parent, false);
                    return new Text2Holder(view);
                case TYPE_RECEIVE_PHOTO:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_photo1, parent, false);
                    return new Photo1Holder(view);
                case TYPE_SEND_PHOTO:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_photo2, parent, false);
                    return new Photo2Holder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final Message message = messageList.get(position);
            switch (holder.getItemViewType()) {
                case TYPE_RECEIVE_TEXT:
                    Text1Holder text1Holder = (Text1Holder)holder;
                    Glide.with(ChatActivity.this).load(user.getHead_portrait()).dontAnimate().error(R.drawable.placeholder_chat).placeholder(R.drawable.placeholder_chat).into(text1Holder.ivAvatar);
                    text1Holder.tvContent.setText(((TextContent)message.getContent()).getText());
                    text1Holder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            float OldListY = (float) location[1];
                            float OldListX = (float) location[0];
                            new TipView.Builder(ChatActivity.this, rootView, (int) OldListX + view.getWidth() / 2, (int) OldListY + view.getHeight())
                                    .addItem(new TipItem(getString(R.string.copy)))
                                    .addItem(new TipItem(getString(R.string.delete)))
                                    .setOnItemClickListener(new TipView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(String str, final int p) {
                                            if (p == 0) {
                                                final String content = ((TextContent) message.getContent()).getText();
                                                if (Build.VERSION.SDK_INT > 11) {
                                                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext()
                                                            .getSystemService(Context.CLIPBOARD_SERVICE);
                                                    ClipData clip = ClipData.newPlainText("Simple text", content);
                                                    clipboard.setPrimaryClip(clip);
                                                } else {
                                                    android.text.ClipboardManager clip = (android.text.ClipboardManager) getApplicationContext()
                                                            .getSystemService(Context.CLIPBOARD_SERVICE);
                                                    if (clip.hasText()) {
                                                        clip.getText();
                                                    }
                                                }
                                                Tools.Toast(ChatActivity.this, getString(R.string.copied));
                                            } else {
                                                //删除
                                                mConversation.deleteMessage(message.getId());
                                                messageList.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    })
                                    .create();
                            return true;
                        }
                    });
                    break;
                case TYPE_SEND_TEXT:
                    final Text2Holder text2Holder = (Text2Holder)holder;
                    Glide.with(ChatActivity.this).load(me.getHead_portrait()).dontAnimate().error(R.drawable.placeholder_chat).placeholder(R.drawable.placeholder_chat).into(text2Holder.ivAvatar);
                    text2Holder.tvContent.setText(((TextContent)message.getContent()).getText());
                    if(message.getStatus() == MessageStatus.send_success){
                        text2Holder.progressView.setVisibility(View.GONE);
                        text2Holder.ivError.setVisibility(View.GONE);
                    }else if (message.getStatus() == MessageStatus.send_fail){
                        text2Holder.progressView.setVisibility(View.GONE);
                        text2Holder.ivError.setVisibility(View.VISIBLE);
                        text2Holder.ivError.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                text2Holder.progressView.setVisibility(View.VISIBLE);
                                text2Holder.ivError.setVisibility(View.GONE);
                                message.setOnSendCompleteCallback(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                                JMessageClient.sendMessage(message);
                            }
                        });
                    }else{
                        text2Holder.progressView.setVisibility(View.VISIBLE);
                        text2Holder.ivError.setVisibility(View.GONE);
                        message.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                notifyItemChanged(position);
                            }
                        });
                        JMessageClient.sendMessage(message);
                    }
                    text2Holder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            float OldListY = (float) location[1];
                            float OldListX = (float) location[0];
                            new TipView.Builder(ChatActivity.this, rootView, (int) OldListX + view.getWidth() / 2, (int) OldListY + view.getHeight())
                                    .addItem(new TipItem(getString(R.string.copy)))
                                    .addItem(new TipItem(getString(R.string.delete)))
                                    .setOnItemClickListener(new TipView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(String str, final int p) {
                                            if (p == 0) {
                                                final String content = ((TextContent) message.getContent()).getText();
                                                if (Build.VERSION.SDK_INT > 11) {
                                                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext()
                                                            .getSystemService(Context.CLIPBOARD_SERVICE);
                                                    ClipData clip = ClipData.newPlainText("Simple text", content);
                                                    clipboard.setPrimaryClip(clip);
                                                } else {
                                                    android.text.ClipboardManager clip = (android.text.ClipboardManager) getApplicationContext()
                                                            .getSystemService(Context.CLIPBOARD_SERVICE);
                                                    if (clip.hasText()) {
                                                        clip.getText();
                                                    }
                                                }
                                                Tools.Toast(ChatActivity.this, getString(R.string.copied));
                                            } else {
                                                //删除
                                                mConversation.deleteMessage(message.getId());
                                                messageList.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    })
                                    .create();
                            return true;
                        }
                    });
                    break;
                case TYPE_RECEIVE_PHOTO:
                    final Photo1Holder photo1Holder = (Photo1Holder)holder;
                    final ImageContent imageContent = (ImageContent) message.getContent();
                    Glide.with(ChatActivity.this).load(user.getHead_portrait()).dontAnimate().error(R.drawable.placeholder_chat).placeholder(R.drawable.placeholder_chat).into(photo1Holder.ivAvatar);
                    photo1Holder.ivContent.setImageResource(R.drawable.placeholder_chat);
                    if(imageContent.getLocalThumbnailPath()!=null && !imageContent.getLocalThumbnailPath().isEmpty()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageContent.getLocalThumbnailPath());
                        photo1Holder.ivContent.setImageBitmap(bitmap);
                    }else{
                        imageContent.downloadThumbnailImage(message, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int status, String desc, File file) {
                                if (status == 0) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    photo1Holder.ivContent.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                    photo1Holder.ivContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<Integer> imageMessageList = new ArrayList<>();
                            for(int i=0;i<messageList.size();i++){
                                if(messageList.get(i).getContentType() == ContentType.image)
                                    imageMessageList.add(0, messageList.get(i).getId());
                            }
                            Intent i = new Intent(ChatActivity.this, ImageViewerActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.PARAM_DATA, (Serializable) imageMessageList);
                            b.putInt(Constants.PARAM_ID, message.getId());
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CHAT);
                            b.putString(Constants.PARAM_NAME, userId);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    photo1Holder.ivContent.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            float OldListY = (float) location[1];
                            float OldListX = (float) location[0];
                            new TipView.Builder(ChatActivity.this, rootView, (int) OldListX + view.getWidth() / 2, (int) OldListY + view.getHeight()/2)
                                    .addItem(new TipItem(getString(R.string.delete)))
                                    .setOnItemClickListener(new TipView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(String str, final int p) {
                                            mConversation.deleteMessage(message.getId());
                                            messageList.remove(position);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    })
                                    .create();
                            return true;
                        }
                    });
                    break;
                case TYPE_SEND_PHOTO:
                    final Photo2Holder photo2Holder = (Photo2Holder)holder;
                    final ImageContent imageContent2 = (ImageContent) message.getContent();
                    Glide.with(ChatActivity.this).load(me.getHead_portrait()).dontAnimate().error(R.drawable.placeholder_chat).placeholder(R.drawable.placeholder_chat).into(photo2Holder.ivAvatar);
                    photo2Holder.ivContent.setImageResource(R.drawable.placeholder_chat);
                    if(imageContent2.getLocalThumbnailPath()!=null && !imageContent2.getLocalThumbnailPath().isEmpty()) {
                        Bitmap bitmap = Tools.decodeFile(imageContent2.getLocalThumbnailPath(), 300, 300);
                        photo2Holder.ivContent.setImageBitmap(bitmap);
                    }else{
                        imageContent2.downloadThumbnailImage(message, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int status, String desc, File file) {
                                if (status == 0) {
                                    Bitmap bitmap = Tools.decodeFile(file.getAbsolutePath(), 300, 300);
                                    photo2Holder.ivContent.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                    photo2Holder.ivContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<Integer> imageMessageList = new ArrayList<>();
                            for(int i=0;i<messageList.size();i++){
                                if(messageList.get(i).getContentType() == ContentType.image)
                                    imageMessageList.add(0, messageList.get(i).getId());
                            }
                            Intent i = new Intent(ChatActivity.this, ImageViewerActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.PARAM_DATA, (Serializable) imageMessageList);
                            b.putInt(Constants.PARAM_ID, message.getId());
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CHAT);
                            b.putString(Constants.PARAM_NAME, userId);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    if(message.getStatus() == MessageStatus.send_success){
                        photo2Holder.progressView.setVisibility(View.GONE);
                        photo2Holder.tvProgress.setVisibility(View.GONE);
                        photo2Holder.ivDim.setVisibility(View.GONE);
                        photo2Holder.ivError.setVisibility(View.GONE);
                    }else if (message.getStatus() == MessageStatus.send_fail){
                        photo2Holder.progressView.setVisibility(View.GONE);
                        photo2Holder.tvProgress.setVisibility(View.GONE);
                        photo2Holder.ivDim.setVisibility(View.GONE);
                        photo2Holder.ivError.setVisibility(View.VISIBLE);
                        photo2Holder.ivError.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                photo2Holder.progressView.setVisibility(View.VISIBLE);
                                photo2Holder.tvProgress.setVisibility(View.VISIBLE);
                                photo2Holder.ivDim.setVisibility(View.VISIBLE);
                                photo2Holder.ivError.setVisibility(View.GONE);
                                message.setOnSendCompleteCallback(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                                JMessageClient.sendMessage(message);
                            }
                        });
                    }else {
                        photo2Holder.progressView.setVisibility(View.VISIBLE);
                        photo2Holder.tvProgress.setVisibility(View.VISIBLE);
                        photo2Holder.tvProgress.setText("0%");
                        photo2Holder.ivDim.setVisibility(View.VISIBLE);
                        photo2Holder.ivError.setVisibility(View.GONE);
                        message.setOnContentUploadProgressCallback(new ProgressUpdateCallback() {
                            @Override
                            public void onProgressUpdate(final double progress) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String progressStr = (int) (progress * 100) + "%";
                                        photo2Holder.tvProgress.setText(progressStr);
                                    }
                                });
                            }
                        });
                        message.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                File f = new File(imageContent2.getStringExtra(Constants.PARAM_DATA));
                                f.delete();
                                notifyItemChanged(position);
                            }
                        });
                        JMessageClient.sendMessage(message);
                    }
                    photo2Holder.ivContent.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            float OldListY = (float) location[1];
                            float OldListX = (float) location[0];
                            new TipView.Builder(ChatActivity.this, rootView, (int) OldListX + view.getWidth() / 2, (int) OldListY + view.getHeight()/2)
                                    .addItem(new TipItem(getString(R.string.delete)))
                                    .setOnItemClickListener(new TipView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(String str, final int p) {
                                            mConversation.deleteMessage(message.getId());
                                            messageList.remove(position);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    })
                                    .create();
                            return true;
                        }
                    });
                    break;
            }

            BaseHolder baseHolder = (BaseHolder)holder;
            long nowDate = message.getCreateTime();
            if(message.getContent().getBooleanExtra(Constants.PARAM_DATA)!=null && message.getContent().getBooleanExtra(Constants.PARAM_DATA)) {
                baseHolder.tvTime.setText(Tools.getChatTime(getApplicationContext(), nowDate));
                baseHolder.tvTime.setVisibility(View.VISIBLE);
            }else {
                if (position != 0) {
                    long beforeDate = messageList.get(position - 1).getCreateTime();
                    if (beforeDate - nowDate > 300000) {
                        baseHolder.tvTime.setText(Tools.getChatTime(getApplicationContext(), nowDate));
                        baseHolder.tvTime.setVisibility(View.VISIBLE);
                    } else {
                        baseHolder.tvTime.setVisibility(View.GONE);
                    }
                } else {
                    if (isFirstOne) {
                        baseHolder.tvTime.setText(Tools.getChatTime(getApplicationContext(), nowDate));
                        baseHolder.tvTime.setVisibility(View.VISIBLE);
                        isFirstOne = false;
                        message.getContent().setBooleanExtra(Constants.PARAM_DATA, true);
                    } else {
                        if (messageList.size() > 1) {
                            long lastDate = messageList.get(position + 1).getCreateTime();
                            if (nowDate - lastDate > 300000) {
                                baseHolder.tvTime.setText(Tools.getChatTime(getApplicationContext(), nowDate));
                                baseHolder.tvTime.setVisibility(View.VISIBLE);
                            } else {
                                baseHolder.tvTime.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

        }

        @Override
        public int getItemViewType(int position) {
            Message message = messageList.get(position);
            if(message.getDirect() == MessageDirect.receive){
                if(message.getContentType() == ContentType.text)
                    return TYPE_RECEIVE_TEXT;
                if(message.getContentType() == ContentType.image)
                    return TYPE_RECEIVE_PHOTO;
            }
            if(message.getDirect() == MessageDirect.send){
                if(message.getContentType() == ContentType.text)
                    return TYPE_SEND_TEXT;
                if(message.getContentType() == ContentType.image)
                    return TYPE_SEND_PHOTO;
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            if(messageList == null)
                return 0;
            return messageList.size();
        }
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

//    boolean isLastVisible() {
//        LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
//        int pos = layoutManager.findLastVisibleItemPosition();
//        int numItems = recyclerView.getAdapter().getItemCount();
//        return (pos >= numItems-2);
//    }
}
