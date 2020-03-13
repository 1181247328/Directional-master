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
 * 其它功能适配器
 * Created by liu on 2017/10/30.
 */

public class OtherAdapter extends BaseAdapter {

    private String TAG = OtherAdapter.class.getSimpleName();

    private Context context = null;

    private int[] orderImage = new int[]{
            R.mipmap.my_traffic,
            R.mipmap.my_vehicle,
            R.mipmap.my_av,
            R.mipmap.my_spot,
            R.mipmap.my_consulting,
            R.mipmap.my_visit,
            R.mipmap.my_surrounding,
            R.mipmap.my_travel,
    };

    private String[] orderName = new String[]{
            "我的线路",
            "我的队伍",
            "二维码",
            "我的勋章",
            "联系我们",
            "关于我们",
            "清空缓存",
            "退出登录",
    };

    public OtherAdapter(Context context) {
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
