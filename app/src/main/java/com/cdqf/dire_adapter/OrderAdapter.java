package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单适配器
 * Created by liu on 2017/10/30.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context = null;

    private int[] orderImage = new int[]{
            R.mipmap.my_payment,
            R.mipmap.my_other,
            R.mipmap.my_delivery,
            R.mipmap.my_goods,
//            R.mipmap.my_evaluation,
    };

    private String[] orderName = new String[]{
            "全部",
            "待支付",
            "已支付",
            "已完成",
//            "待评价",
    };

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return orderImage.length;
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
       ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivMyItemLogo.setImageResource(orderImage[position]);
        viewHolder.tvMyItemName.setText(orderName[position]);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_my_item_logo)
        public ImageView ivMyItemLogo = null;

        @BindView(R.id.tv_my_item_name)
        public TextView tvMyItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
