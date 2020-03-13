package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;

public class UserAdapter extends BaseAdapter {

    private String TAG = UserAdapter.class.getSimpleName();

    private Context context = null;

    private String[] name = {
            "头像",
            "昵称",
            "姓名",
            "性别",
            "电子邮箱",
            "手机号",
            "我的二维码"
    };

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == getCount() - 1) {
            return 2;
        } else {
            return 1;
        }
    }

    public UserAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return name.length;
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
        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        if (type == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_hear, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUserItemName = convertView.findViewById(R.id.tv_user_item_name);
            viewHolder.ivUserItemHear = convertView.findViewById(R.id.iv_user_item_hear);
            viewHolder.tvUserItemName.setText(name[position]);
        } else if (type == 1) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUserItemName = convertView.findViewById(R.id.tv_user_item_name);
            viewHolder.tvUserItemContext = convertView.findViewById(R.id.tv_user_item_context);
            viewHolder.tvUserItemName.setText(name[position]);
        } else if (type == 2) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_bottom, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUserItemName = convertView.findViewById(R.id.tv_user_item_name);
            viewHolder.ivUserItemCode = convertView.findViewById(R.id.iv_user_item_code);
            viewHolder.tvUserItemName.setText(name[position]);
        }
        return convertView;
    }

    class ViewHolder {
        //名称
        public TextView tvUserItemName = null;

        //头像
        public ImageView ivUserItemHear = null;

        //内容
        public TextView tvUserItemContext = null;

        //二维码
        public ImageView ivUserItemCode = null;
    }
}
