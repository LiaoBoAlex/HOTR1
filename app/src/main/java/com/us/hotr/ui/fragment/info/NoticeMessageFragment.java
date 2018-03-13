package com.us.hotr.ui.fragment.info;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.info.ChatActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.rxjava.ApiException;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Mloong on 2017/9/20.
 */

public class NoticeMessageFragment extends BaseLoadingFragment {
    private RecyclerView mRecyclerView;
    private ConstraintLayout mEmptyView;
    private MyAdapter mAdapter;

    private String meId;

    public static NoticeMessageFragment newInstance() {
        NoticeMessageFragment noticeMessageFragment = new NoticeMessageFragment();
        return noticeMessageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        meId = "user"+ HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserInfo().getUserId();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mEmptyView = (ConstraintLayout) view.findViewById(R.id.container);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setEnableLoadmore(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter == null)
            loadData(Constants.LOAD_PAGE);
        else
            loadData(Constants.LOAD_PULL_REFRESH);
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    public void onEvent(MessageEvent event) {
        loadData(Constants.LOAD_PULL_REFRESH);

    }

    public void onEvent(OfflineMessageEvent event) {
        loadData(Constants.LOAD_PULL_REFRESH);
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<List<Conversation>>() {
            @Override
            public void onNext(List<Conversation> result) {
                int total = 0;
                if(result!=null && result.size()>0) {
                    for(Conversation conversation:result)
                        total = total + conversation.getUnReadMsgCnt();
                    mEmptyView.setVisibility(View.GONE);
                    if (mAdapter == null) {
                        mAdapter = new MyAdapter(result);
                        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                        myBaseAdapter.setFooterView();
                        mRecyclerView.setAdapter(myBaseAdapter);
                    } else {
                        mAdapter.setData(result);
                    }
                }else
                    mEmptyView.setVisibility(View.VISIBLE);
                GlobalBus.getBus().post(new Events.GetNoticeCount(total, 0));
            }
        };
        ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe<List<Conversation>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Conversation>> emitter) throws Exception {
                if(JMessageClient.getMyInfo()!=null) {
                    emitter.onNext(JMessageClient.getConversationList());
                    emitter.onComplete();
                }else {
                    JMessageClient.login(meId, "123456", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i != 0) {
                                emitter.onError(new ApiException(i, s));
                            } else {
                                emitter.onNext(JMessageClient.getConversationList());
                                emitter.onComplete();
                            }
                        }
                    });
                }
            }
        };
        if(type == Constants.LOAD_PAGE)
            Observable.create(observableOnSubscribe)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LoadingSubscriber(mListener, this));
        if(type == Constants.LOAD_PULL_REFRESH)
            Observable.create(observableOnSubscribe)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SilentSubscriber(mListener, getActivity(), refreshLayout));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Conversation> conversationList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvName, tvTime, tvContent;
            QBadgeView mBadgeView;
            public MyViewHolder(View view) {
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTime = (TextView) view.findViewById(R.id.tv_time);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
                mBadgeView = new QBadgeView(getContext());
                mBadgeView.bindTarget(ivAvatar)
                        .setBadgeGravity(Gravity.TOP | Gravity.END)
                        .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                        .setShowShadow(false);
            }
        }

        public MyAdapter(List<Conversation> conversationList) {
            this.conversationList = conversationList;
        }

        public void setData(List<Conversation> conversationList) {
            this.conversationList = conversationList;
            notifyDataSetChanged();
        }

        public void removeData(int position){
            conversationList.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notice_message, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Conversation conversation = conversationList.get(position);
            final UserInfo userInfo = (UserInfo)conversation.getTargetInfo();
            Glide.with(NoticeMessageFragment.this).load(userInfo.getAddress()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(holder.ivAvatar);
            String name = userInfo.getNickname();
            if(name == null || name.isEmpty())
                name = userInfo.getUserName();
            holder.tvName.setText(name);
            Message message = conversation.getLatestMessage();
            holder.tvTime.setText(Tools.getChatTime1(getActivity().getApplicationContext(), message.getCreateTime()));
            if (message.getContentType() == ContentType.text)
                holder.tvContent.setText(((TextContent) message.getContent()).getText());
            if (message.getContentType() == ContentType.image)
                holder.tvContent.setText(getString(R.string.image));
            if(conversation.getUnReadMsgCnt()>0) {
                holder.mBadgeView.setBadgeNumber(conversation.getUnReadMsgCnt());
            }else
                holder.mBadgeView.hide(false);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conversation.resetUnreadCount();
                    Intent i = new Intent(getActivity(), ChatActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_DATA, Long.parseLong(userInfo.getUserName().replace("user", "")));
                    i.putExtras(b);
                    startActivity(i);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(userInfo.getUserName(), position);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            if(conversationList!=null)
                return conversationList.size();
            else
                return 0;
        }
    }

    private void showDialog(final String username, final int position){
        final Dialog dialog = new Dialog(getActivity(), R.style.default_dialog_style);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.menu_conversation, null);
        dialog.setContentView(v);
        final LinearLayout deleteLl = (LinearLayout) v.findViewById(R.id.ll_delete);

        deleteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JMessageClient.deleteSingleConversation(username, "");
                mAdapter.removeData(position);
                if(mAdapter.getItemCount() == 0)
                    mEmptyView.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
