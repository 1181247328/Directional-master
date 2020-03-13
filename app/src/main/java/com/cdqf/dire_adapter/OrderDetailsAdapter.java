package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_view.ListViewForScrollView;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 订单详情商品适配器
 * Created by liu on 2018/1/5.
 */

public class OrderDetailsAdapter extends BaseAdapter {

    private String TAG = OrderDetailsAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private String orderStatus;

    public OrderDetailsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlantViewHolder plantViewHolder = null;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_orderdetails, null);
        plantViewHolder = new PlantViewHolder();
        //图标
        plantViewHolder.ivOrderItemIcon = convertView.findViewById(R.id.iv_order_item_icon);
        //商品名称
        plantViewHolder.tvOrderItemTitle = convertView.findViewById(R.id.tv_order_item_title);
        //价格
        plantViewHolder.tvOrderItemPrice = convertView.findViewById(R.id.tv_order_item_price);
        //数量
        plantViewHolder.tvOrderItemNumber = convertView.findViewById(R.id.tv_order_item_number);
        return convertView;
    }

    class PlantViewHolder {
        //图标
        public ImageView ivOrderItemIcon = null;

        //商品名称
        public TextView tvOrderItemTitle = null;

        //价格
        public TextView tvOrderItemPrice = null;

        //数量
        public TextView tvOrderItemNumber = null;

        //商品集合
        public ListViewForScrollView lvOrderItemList = null;

        //合计
        public TextView tvOrderItemCombined = null;

        //状态
        public TextView tvOrderItemForpayment = null;

        //订单操作
        public RCRelativeLayout rcrlOrderItemOne = null;
        public TextView tvOrderItemOne = null;

        //物流
        public RCRelativeLayout rcrlOrderItemTwo = null;

        //订单操作
        public RCRelativeLayout rcrlOrderItemThree = null;
    }
}
