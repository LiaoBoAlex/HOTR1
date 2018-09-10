package com.us.hotr.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ShapedImageView;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.MassageReceipt;
import com.us.hotr.storage.bean.MasseurTag;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetMasseurCommentsResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

public class CommentMasseurDialogFragment extends BottomSheetDialogFragment {
    private ImageView ivClose;
    private ShapedImageView ivAvatar;
    private TextView tvName, tvComment, tvSubmit;
    private RecyclerView rvStar, rvComments;

    private BottomSheetBehavior mBehavior;
    private StarAdapter starAdapter;
    private CommentAdapter commentAdapter;

    private MassageReceipt massageReceipt;
    private GetMasseurCommentsResponse comments;

    public static CommentMasseurDialogFragment newInstance(MassageReceipt massageReceipt) {
        CommentMasseurDialogFragment commentMasseurDialogFragment = new CommentMasseurDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, massageReceipt);
        commentMasseurDialogFragment.setArguments(b);
        return commentMasseurDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_masseur_comment, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        massageReceipt = (MassageReceipt)getArguments().getSerializable(Constants.PARAM_DATA);

        ivClose = (ImageView) view.findViewById(R.id.iv_close);
        ivAvatar = (ShapedImageView) view.findViewById(R.id.iv_avatar);
        tvComment = (TextView) view.findViewById(R.id.tv_comment);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvSubmit = (TextView) view.findViewById(R.id.tv_submit);
        rvStar = (RecyclerView) view.findViewById(R.id.rv_star);
        rvComments = (RecyclerView) view.findViewById(R.id.rv_comments);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        Glide.with(this).load(massageReceipt.getMain_img()).error(R.mipmap.holder_post).into(ivAvatar);
        tvName.setText(massageReceipt.getMassagist_name());

        rvComments.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        rvComments.setItemAnimator(new DefaultItemAnimator());
        loadComments();
        rvStar.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false));
        rvStar.setItemAnimator(new DefaultItemAnimator());
        starAdapter = new StarAdapter(0);
        rvStar.setAdapter(starAdapter);
        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mBehavior.setState(STATE_EXPANDED);
    }

    private void comment(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                GlobalBus.getBus().post(new Events.Refresh());
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        };
        ServiceClient.getInstance().commentMasseur(new ProgressSubscriber(mListener, getActivity()),
                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                massageReceipt.getId(), commentAdapter.getSelectedComments(), starAdapter.getScore());
    }

    private void loadComments(){
        SubscriberListener mListener = new SubscriberListener<GetMasseurCommentsResponse>() {
            @Override
            public void onNext(GetMasseurCommentsResponse result) {
                comments = result;
            }
        };
        ServiceClient.getInstance().getMasseurCommentTable(new SilentSubscriber(mListener, getActivity(), null),
                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
    }

    public void doclick(View v)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public class StarAdapter extends RecyclerView.Adapter<StarAdapter.MyViewHolder> {
        private int starNumber = 0;

        public StarAdapter(int starNumber){
            this.starNumber = starNumber;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivStar;
            public MyViewHolder(View view) {
                super(view);
                ivStar = (ImageView) view.findViewById(R.id.iv_star);
            }
        }

        @Override
        public StarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_star, parent, false);
            return new MyViewHolder(view);
        }

        public int getScore(){
            return starNumber;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if(position<starNumber)
                holder.ivStar.setImageResource(R.mipmap.ic_star_selected);
            else
                holder.ivStar.setImageResource(R.mipmap.ic_star);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starNumber = position + 1;
                    notifyDataSetChanged();
                    List<MasseurTag> levelComments = new ArrayList<>();
                    for(MasseurTag mc:comments.getTabInfoList()){
                        if(mc.getTab_level() == (position+1))
                            levelComments.add(mc);
                    }
                    switch (position){
                        case 0:
                            tvComment.setText(comments.getOne());
                            break;
                        case 1:
                            tvComment.setText(comments.getTwo());
                            break;
                        case 2:
                            tvComment.setText(comments.getThree());
                            break;
                        case 3:
                            tvComment.setText(comments.getFour());
                            break;
                        case 4:
                            tvComment.setText(comments.getFive());
                            break;
                    }
                    rvComments.setVisibility(View.VISIBLE);
                    commentAdapter = new CommentAdapter(levelComments);
                    rvComments.setAdapter(commentAdapter);
                    tvSubmit.setBackgroundColor(getResources().getColor(R.color.text_black));
                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            comment();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
        List<MasseurTag> commentList;
        List<Boolean> isCommentSelected = new ArrayList<>();

        public CommentAdapter(List<MasseurTag> commentList){
            this.commentList = commentList;
            if(commentList.size()>0)
                for(int i=0;i<commentList.size();i++)
                    isCommentSelected.add(false);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvComent;
            public MyViewHolder(View view) {
                super(view);
                tvComent = (TextView) view.findViewById(R.id.tv_comment);
            }
        }

        public List<Long> getSelectedComments(){
            List<Long> result = new ArrayList<>();
            for(int i=0;i<commentList.size();i++){
                if(isCommentSelected.get(i))
                    result.add(commentList.get(i).getId());
            }
            return result;
        }

        @Override
        public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur_comment, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tvComent.setText(commentList.get(position).getTab_content());
            if(isCommentSelected.get(position))
                holder.tvComent.setBackgroundResource(R.drawable.bg_button_dark_solid);
            else
                holder.tvComent.setBackgroundResource(R.drawable.bg_button);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isCommentSelected.get(position)){
                        isCommentSelected.set(position, false);
                        holder.tvComent.setBackgroundResource(R.drawable.bg_button);
                    }else{
                        isCommentSelected.set(position, true);
                        holder.tvComent.setBackgroundResource(R.drawable.bg_button_dark_solid);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (commentList==null)
                return 0;
            else
                return commentList.size();
        }
    }
}
