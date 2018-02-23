package com.us.hotr.ui.activity.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Theme;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetAllGroupResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaobo on 2018/1/11.
 */

public class UploadPostActivity2 extends BaseLoadingActivity {
    RecyclerView rvTitle, rvSubTitle;
    TitleAdapter titleAdapter;
    SubTitleAdapter subTitleAdapter;
    LinearLayoutManager linearLayoutManager;

    private int row_index = 0;
    private long selectedId = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStaticView();
        setMyTitle(R.string.select_group);
        loadData(Constants.LOAD_PAGE);
    }

    private void initStaticView(){
        rvTitle = (RecyclerView) findViewById(R.id.rv_title);
        rvSubTitle = (RecyclerView) findViewById(R.id.rv_sub_title);

        rvTitle.setLayoutManager(new LinearLayoutManager(this));
        rvTitle.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager = new LinearLayoutManager(this);
        rvSubTitle.setLayoutManager(linearLayoutManager);
        rvSubTitle.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetAllGroupResponse>() {
            @Override
            public void onNext(GetAllGroupResponse result) {
                if(result.getMyAttentionCoshow()!=null && result.getMyAttentionCoshow().size()>0){
                    Theme theme = new Theme();
                    theme.setThemeName(getString(R.string.my_fav_group));
                    theme.setCoshowList(result.getMyAttentionCoshow());
                    result.getThemeList().add(0, theme);
                }
                titleAdapter = new TitleAdapter(result.getThemeList());
                rvTitle.setAdapter(titleAdapter);
                subTitleAdapter = new SubTitleAdapter(result.getThemeList().get(row_index));
                rvSubTitle.setAdapter(subTitleAdapter);
            }
        };
        ServiceClient.getInstance().getAllGroup(new LoadingSubscriber(mListener, this),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_group;
    }

    public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.MyViewHolder> {

        private List<Theme> themeList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            View vIndicator;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                vIndicator = view.findViewById(R.id.v_indicator);
            }
        }


        public TitleAdapter(List<Theme> themeList) {
            this.themeList = themeList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_subject_left, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Theme theme = themeList.get(position);
            holder.tvTitle.setText(theme.getThemeName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position;
                    notifyDataSetChanged();

                    if(subTitleAdapter!=null) {
                        subTitleAdapter.setItem(theme);
                        linearLayoutManager.scrollToPosition(0);
                    }
                    else {
                        subTitleAdapter = new SubTitleAdapter(theme);
                        rvSubTitle.setAdapter(subTitleAdapter);
                    }
                }
            });
            if (row_index == position) {
                holder.tvTitle.setBackgroundResource(R.color.white);
                holder.vIndicator.setVisibility(View.VISIBLE);
            } else {
                holder.tvTitle.setBackgroundResource(R.color.bg_grey);
                holder.vIndicator.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            if(themeList == null)
                return 0;
            else
                return  themeList.size();
        }
    }

    public class SubTitleAdapter extends RecyclerView.Adapter<SubTitleAdapter.MyViewHolder> {

        private Theme theme;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubTitle;
            FlowLayout flSubSubTitle;

            public MyViewHolder(View view) {
                super(view);
                tvSubTitle = (TextView) view.findViewById(R.id.tv_title);
                flSubSubTitle = (FlowLayout) view.findViewById(R.id.fl_subtitle);
            }
        }

        public SubTitleAdapter(Theme theme) {
            this.theme = theme;
        }

        public void setItem(Theme theme) {
            this.theme = theme;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_subject_catalog, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tvSubTitle.setText(theme.getThemeName());
            int p = -1;
            List<String> nameList = new ArrayList<>();
            if(theme.getCoshowList()!=null && theme.getCoshowList().size()>0) {
                for (int i = 0; i < theme.getCoshowList().size(); i++){
                    nameList.add(theme.getCoshowList().get(i).getCoshow_name());
                    if (theme.getCoshowList().get(i).getId() == selectedId)
                        p = i;
                }
            }
            holder.flSubSubTitle.setFlowLayout(nameList, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content, int position) {
                    selectedId = theme.getCoshowList().get(position).getId();
                    notifyDataSetChanged();
                    TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(UploadPostActivity2.this);
                    alertDialogBuilder.setMessage(String.format(getString(R.string.post_to), theme.getCoshowList().get(position).getCoshow_name()));
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent();
                                    Bundle b = new Bundle();
                                    b.putLong(Constants.PARAM_ID, selectedId);
                                    i.putExtras(b);
                                    setResult(RESULT_OK, i);
                                    UploadPostActivity2.this.finish();
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
            });
            holder.flSubSubTitle.setHighlightedItem(p);
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
