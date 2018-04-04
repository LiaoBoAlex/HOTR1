package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.view.PostView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetPartyDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class PartyActivity extends BaseLoadingActivity{

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageView ivFav, ivBackHome;
    private TextView tvPurchase;

    private float offset = 0;
    private long partyId;
    private GetPartyDetailResponse getPartyDetailResponse;
    private boolean isCollected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.party_detail);
        partyId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        increasePartyInterest();
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    private void increasePartyInterest(){
        SubscriberListener mListener = new SubscriberListener() {
            @Override
            public void onNext(Object o) {

            }
        };
        ServiceClient.getInstance().increasePartyInterest(new SilentSubscriber(mListener, this, null), partyId);
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetPartyDetailResponse>() {
            @Override
            public void onNext(final GetPartyDetailResponse result) {
                if(result == null || result.getTravel() == null){
                    showErrorPage();
                    return;
                }
                getPartyDetailResponse = result;
                isCollected = result.getIs_collected()==1?true:false;
                if(isCollected)
                    ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                else
                    ivFav.setImageResource(R.mipmap.ic_fav_text);
                ivFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isCollected) {
                            SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
                                @Override
                                public void onNext(String result) {
                                    isCollected = false;
                                    Tools.Toast(PartyActivity.this, getString(R.string.remove_fav_item_success));
                                    ivFav.setImageResource(R.mipmap.ic_fav_text);
                                }
                                @Override
                                public void reload() {
                                    loadData(Constants.LOAD_DIALOG);
                                }
                            };
                            ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, PartyActivity.this),
                                    HOTRSharePreference.getInstance(PartyActivity.this.getApplicationContext()).getUserID(), Arrays.asList(result.getTravel().getId()), 0);
                        }else{
                            SubscriberListener mListener = new SubscriberListener<String>() {
                                @Override
                                public void onNext(String result) {
                                    Tools.Toast(PartyActivity.this, getString(R.string.fav_item_success));
                                    isCollected = true;
                                    ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                                }
                            };
                            ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, PartyActivity.this),
                                    HOTRSharePreference.getInstance(PartyActivity.this.getApplicationContext()).getUserID(), result.getTravel().getId(), 0);
                        }
                    }
                });
                switch (result.getTravel().getSale_ticket_status()) {
                    case 0:
                        tvPurchase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                purchase();
                            }
                        });
                        break;
                    case 1:
                        tvPurchase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                purchase();
                            }
                        });
                        break;
                    case 2:
                        if(result.getIs_reservation() == 1){
                            tvPurchase.setText(R.string.turned_on_notification);
                            tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        }else {
                            tvPurchase.setText(R.string.turn_on_notification);
                            tvPurchase.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    turnOnNotification(result.getTravel().getId());
                                }
                            });
                        }
                        break;
                    case 3:
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setText(R.string.no_ticket_for_recommended_party);
                        break;
                    case 4:
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setText(R.string.no_ticket_for_promoted_party);
                        break;
                    case 5:
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setText(R.string.no_ticket_for_ended_party);
                        break;
                }
                mAdapter = new MyAdapter(result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                recyclerView.setAdapter(myBaseAdapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getPartyDetail(new LoadingSubscriber(mListener, this),
                    partyId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getPartyDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    partyId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    public void purchase(){
        if(Tools.isUserLogin(getApplicationContext())){
            purchaseCheckCount();
        }else{
            LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                @Override
                public void onLoginSuccess() {
                    purchaseCheckCount();
                }
            });
            startActivityForResult(new Intent(PartyActivity.this, LoginActivity.class), 0);
        }

    }

    public void purchaseCheckCount(){
            SubscriberListener mListener = new SubscriberListener<Boolean>() {
                @Override
                public void onNext(Boolean result) {
                    if(!result){
                        Tools.Toast(PartyActivity.this, getString(R.string.ticket_sold_out));
                        tvPurchase.setText(R.string.sold_out);
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setOnClickListener(null);
                    }else {
                        Intent i = new Intent(PartyActivity.this, PartyPayNumberActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.PARAM_DATA, getPartyDetailResponse);
                        i.putExtras(b);
                        startActivity(i);
                    }
                }
            };
            ServiceClient.getInstance().checkPartyOrderCount(new ProgressSubscriber(mListener, PartyActivity.this), getPartyDetailResponse.getTicket());
    }

    private void turnOnNotification(final long id){
                    SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    Set<String> set = new HashSet<String>();
                    set.add(id+"");
                    JPushInterface.addTags(getApplicationContext(), (int)id, set);
                }
            };
            ServiceClient.getInstance().reserveParty(new ProgressSubscriber(mListener, PartyActivity.this),
                    id, HOTRSharePreference.getInstance(PartyActivity.this.getApplicationContext()).getUserID());

    }

    private void initStaticView(){

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ivFav = (ImageView) findViewById(R.id.iv_fav);
        ivBackHome = (ImageView) findViewById(R.id.iv_homepage);
        tvPurchase = (TextView) findViewById(R.id.tv_purchase);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PartyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        enableLoadMore(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offset = offset + dy;
                if(offset > 300){
                    ivBack.setImageResource(R.mipmap.ic_back);
                    ivShare.setImageResource(R.mipmap.ic_share);
                    findViewById(R.id.v_divider).setAlpha(1);
                    findViewById(R.id.tb_title).setAlpha(1);
                }else{
                    findViewById(R.id.tb_title).setAlpha(offset / 300);
                    ivBack.setImageResource(R.mipmap.ic_back_dark);
                    ivShare.setImageResource(R.mipmap.ic_share_dark);
                    findViewById(R.id.v_divider).setAlpha(offset / 300);
                }
            }
        });
    }

//    public int getScollYDistance() {
//        int position = mLayoutManager.findFirstVisibleItemPosition();
//        View firstVisiableChildView = mLayoutManager.findViewByPosition(position);
//        int itemHeight = firstVisiableChildView.getHeight();
//        return (position) * itemHeight - firstVisiableChildView.getTop();
//    }

    @Override
    protected int getLayout() {
        return R.layout.activity_masseur;
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getMessage(final Events.JPushSetTag jPushSetTag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(jPushSetTag.isResult()){
                    tvPurchase.setText(R.string.turned_on_notification);
                    tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                }else{
                    Tools.Toast(PartyActivity.this, getString(R.string.turn_on_notification_failed));
                }
            }
        });


    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private GetPartyDetailResponse response;

        private static final int VIEW_TYPE_POST = 100;
        private static final int VIEW_TYPE_HEADER = 101;
        private static final int VIEW_WEB = 102;

        private boolean isEdit = false;

        public class MyPostHolder extends RecyclerView.ViewHolder {
            PostView postView;
            public MyPostHolder(View view) {
                super(view);
                postView = (PostView) view;
            }
        }

        public class WebHolder extends RecyclerView.ViewHolder {
            WebView wvContent;
            public WebHolder(View itemView) {
                super(itemView);
                wvContent = (WebView) itemView.findViewById(R.id.wv_content);
            }
        }

        public class MyHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivCity, ivAvatar;
            TextView tvTitle, tvPeople, tvPrice, tvShare, tvStatus, tvAddress, tvMoney, tvNoPrice;
            ConstraintLayout clShare;
            NiceVideoPlayer vvVideo;
            TxVideoPlayerController mController;

            public MyHeaderHolder(View view) {
                super(view);
                ivCity = (ImageView) view.findViewById(R.id.iv_city);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvPeople = (TextView) view.findViewById(R.id.tv_people);
                tvAddress = (TextView) view.findViewById(R.id.tv_address);
                tvPrice = (TextView) view.findViewById(R.id.tv_amount);
                tvMoney = (TextView) view.findViewById(R.id.tv_money);
                tvNoPrice = (TextView) view.findViewById(R.id.tv_no_price);
                tvShare = (TextView) view.findViewById(R.id.tv_share);
                tvStatus = (TextView) view.findViewById(R.id.tv_status);
                clShare = (ConstraintLayout) view.findViewById(R.id.cl_post_title);
                vvVideo = (NiceVideoPlayer) view.findViewById(R.id.vv_video);
            }

            public void setController(TxVideoPlayerController controller) {
                mController = controller;
                vvVideo.setController(mController);
                vvVideo.setPlayerType(NiceVideoPlayer.TYPE_NATIVE);
            }
        }

        public MyAdapter(GetPartyDetailResponse response) {
            this.response = response;
        }

        public void setEnableEdit(boolean enable) {
            isEdit = enable;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                    return new MyPostHolder(view);
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_party_header, parent, false);
                    MyHeaderHolder headerHolder = new MyHeaderHolder(view);
                    TxVideoPlayerController controller = new TxVideoPlayerController(PartyActivity.this);
                    headerHolder.setController(controller);
                    return headerHolder;
                case VIEW_WEB:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web, parent, false);
                    return new WebHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_POST:
                    MyPostHolder postHolder = (MyPostHolder) holder;
                    postHolder.postView.setData(new Post());
                    postHolder.postView.enableEdit(isEdit);
                    break;
                case VIEW_WEB:
                    ((WebHolder)holder).wvContent.loadData(Tools.getHtmlData(response.getTravel().getTravelInfo()), "text/html; charset=UTF-8", null);
                    break;
                case VIEW_TYPE_HEADER:
                    final MyHeaderHolder headerHolder = (MyHeaderHolder) holder;
                    headerHolder.tvAddress.setText(response.getTravel().getTravelAddress());
                    headerHolder.tvTitle.setText(response.getTravel().getTravel_name());
                    headerHolder.tvPrice.setText(new DecimalFormat("#").format(response.getTravel().getPriceRangeLow()) + "-" + new DecimalFormat("#").format(response.getTravel().getPriceRangeHigh()));
                    Glide.with(PartyActivity.this).load(response.getTravel().getCityStrategyImg()).error(R.drawable.holder_city).placeholder(R.drawable.holder_city).into(headerHolder.ivCity);
                    headerHolder.ivCity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(PartyActivity.this, WebViewActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.city_map));
                            b.putInt(Constants.PARAM_TYPE, WebViewActivity.TYPE_DATA);
                            b.putString(Constants.PARAM_DATA, response.getTravel().getCityStrategyDetail());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    switch (response.getTravel().getSale_ticket_status()) {
                        case 0:
                            headerHolder.tvStatus.setText(getString(R.string.status_on_sale));
                            headerHolder.tvPeople.setText(String.format(getString(R.string.party_sale_number), response.getOrderNum()));
                            headerHolder.tvNoPrice.setVisibility(View.INVISIBLE);
                            headerHolder.tvMoney.setVisibility(View.VISIBLE);
                            headerHolder.tvPrice.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            headerHolder.tvStatus.setText(getString(R.string.status_on_going));
                            headerHolder.tvPeople.setText(String.format(getString(R.string.party_sale_number), response.getOrderNum()));
                            headerHolder.tvNoPrice.setVisibility(View.INVISIBLE);
                            headerHolder.tvMoney.setVisibility(View.VISIBLE);
                            headerHolder.tvPrice.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            headerHolder.tvStatus.setText(Tools.getSaleTime(PartyActivity.this, response.getTravel().getSale_ticket_time()));
                            headerHolder.tvPeople.setText(String.format(getString(R.string.party_interested_number), response.getTravel().getAccess_count()));
                            headerHolder.tvNoPrice.setVisibility(View.INVISIBLE);
                            headerHolder.tvMoney.setVisibility(View.VISIBLE);
                            headerHolder.tvPrice.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            headerHolder.tvStatus.setText(getString(R.string.status_promo));
                            headerHolder.tvPeople.setText(String.format(getString(R.string.party_interested_number), response.getTravel().getAccess_count()));
                            headerHolder.tvNoPrice.setVisibility(View.VISIBLE);
                            headerHolder.tvMoney.setVisibility(View.INVISIBLE);
                            headerHolder.tvPrice.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            headerHolder.tvStatus.setText(getString(R.string.status_pre_view));
                            headerHolder.tvPeople.setText(String.format(getString(R.string.party_interested_number), response.getTravel().getAccess_count()));
                            headerHolder.tvNoPrice.setVisibility(View.VISIBLE);
                            headerHolder.tvMoney.setVisibility(View.INVISIBLE);
                            headerHolder.tvPrice.setVisibility(View.INVISIBLE);
                            break;
                        case 5:
                            headerHolder.tvStatus.setText(getString(R.string.status_end));
                            headerHolder.tvPeople.setVisibility(View.GONE);
                            headerHolder.tvNoPrice.setVisibility(View.INVISIBLE);
                            headerHolder.tvMoney.setVisibility(View.VISIBLE);
                            headerHolder.tvPrice.setVisibility(View.VISIBLE);
                            break;
                    }
                    if(response.getTravel().getVideoUrl()!=null) {
                        headerHolder.vvVideo.setVisibility(View.VISIBLE);
                        headerHolder.ivAvatar.setVisibility(View.GONE);
                        Glide.with(PartyActivity.this).load(response.getTravel().getPartyDetailImg()).error(R.color.black).placeholder(R.color.black).into(headerHolder.mController.imageView());
                        headerHolder.vvVideo.setUp(response.getTravel().getVideoUrl(), null);
                    }else{
                        headerHolder.vvVideo.setVisibility(View.GONE);
                        headerHolder.ivAvatar.setVisibility(View.VISIBLE);
                        Glide.with(PartyActivity.this).load(response.getTravel().getPartyDetailImg()).error(R.drawable.holder_video).placeholder(R.drawable.holder_video).into(headerHolder.ivAvatar);
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0)
                return VIEW_TYPE_HEADER;
            else if (position == 1)
                return VIEW_WEB;
            else
                return VIEW_TYPE_POST;

        }
    }

    public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvAddress, tvDate;

            public MyViewHolder(View view) {
                super(view);
                tvAddress = (TextView) view.findViewById(R.id.tv_place);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
            }
        }

        public MyAddressAdapter() {
        }

        @Override
        public MyAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party_address, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
