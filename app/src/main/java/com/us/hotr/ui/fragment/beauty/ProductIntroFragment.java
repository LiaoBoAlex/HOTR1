package com.us.hotr.ui.fragment.beauty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.ui.activity.beauty.ListWithCategoryActivity;
import com.us.hotr.ui.view.ProductView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.response.GetProductDetailResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/12.
 */

public class ProductIntroFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private GetProductDetailResponse productDetail;

    public static ProductIntroFragment newInstance(GetProductDetailResponse productDetail) {
        ProductIntroFragment productIntroFragment = new ProductIntroFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, productDetail);
        productIntroFragment.setArguments(b);
        return productIntroFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productDetail = (GetProductDetailResponse) getArguments().getSerializable(Constants.PARAM_DATA);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(getActivity(), productDetail);
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
        myBaseAdapter.setFooterView();
        recyclerView.setAdapter(myBaseAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int TYPE_WEBVIEW =100;
        public static final int TYPE_PRODUCT = 101;
        public static final int TYPE_PRODUCT_HEADER = 102;
        public static final int TYPE_PRODUCT_FOOTER = 103;

        public MyAdapter(Context mContext, GetProductDetailResponse productDetail) {
            this.productDetail = productDetail;
            this.mContext = mContext;
            itemList.add(new Item(TYPE_WEBVIEW, productDetail.getProduct().getProductDetail()));
            if(productDetail.getProposedProduct()!=null && productDetail.getProposedProduct().size()>0){
                itemList.add(new Item(TYPE_PRODUCT_HEADER));
                for(int i=0;i<productDetail.getProposedProduct().size();i++)
                    itemList.add(new Item(TYPE_PRODUCT, productDetail.getProposedProduct().get(i)));
                if(productDetail.getProposedProductCount()>3)
                    itemList.add(new Item(TYPE_PRODUCT_FOOTER));
            }
        }

        private GetProductDetailResponse productDetail;
        private List<Item> itemList = new ArrayList<>();
        private Context mContext;

        public class ProductHolder extends RecyclerView.ViewHolder {
            ProductView productView;
            public ProductHolder(View view) {
                super(view);
                productView = (ProductView) view;
            }
        }

        public class WebHolder extends RecyclerView.ViewHolder {
            WebView wvContent;
            public WebHolder(View itemView) {
                super(itemView);
                wvContent = (WebView) itemView.findViewById(R.id.wv_content);
            }
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public HeaderHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public class FooterHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public FooterHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_PRODUCT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                    return new ProductHolder(view);
                case TYPE_PRODUCT_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_text, parent, false);
                    return new HeaderHolder(view);
                case TYPE_PRODUCT_FOOTER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more, parent, false);
                    return new FooterHolder(view);
                case TYPE_WEBVIEW:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web, parent, false);
                    return new WebHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case TYPE_PRODUCT_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.popular_product_in_hospital));
                    break;
                case TYPE_PRODUCT_FOOTER:
                    ((FooterHolder) holder).textView.setText(getString(R.string.all_product));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.hospital_appointment));
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                            b.putLong(Constants.PARAM_HOSPITAL_ID, productDetail.getHospital().getKey());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    break;
                case TYPE_PRODUCT:
                    final Product product = (Product)itemList.get(position).getContent();
                    ProductHolder productHolder = (ProductHolder) holder;
                    productHolder.productView.setData(product);
                    if(position < getItemCount()-1 && !(itemList.get(position+1).getContent() instanceof Product))
                        productHolder.productView.showDivider(false);
                    else
                        productHolder.productView.showDivider(true);
                    break;
                case TYPE_WEBVIEW:
                    ((WebHolder)holder).wvContent.loadData(Tools.getHtmlData(productDetail.getProduct().getProductDetail()), "text/html; charset=UTF-8", null);
                    break;
            }

        }

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return itemList.size();
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
}
