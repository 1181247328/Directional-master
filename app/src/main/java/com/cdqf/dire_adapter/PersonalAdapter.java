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
 * 侧滑信息适配器
 */
public class PersonalAdapter extends BaseAdapter {

    private String TAG = PersonalAdapter.class.getSimpleName();

    private Context context = null;

    //名称
    private String[] name = {
            "找回密码",
            "支付管理",
            "使用帮助"
    };

    //图片
    private int[] nameImage = {
            R.mipmap.personal_password,
            R.mipmap.personal_pay,
            R.mipmap.personal_help,
    };

    public PersonalAdapter(Context context) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_personal, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivPersonalItemImage.setImageResource(nameImage[position]);
        viewHolder.tvPersonalItemName.setText(name[position]);
        return convertView;
    }

    class ViewHolder {
        //图片
        @BindView(R.id.iv_personal_item_image)
        public ImageView ivPersonalItemImage = null;

        //姓名
        @BindView(R.id.tv_personal_item_name)
        public TextView tvPersonalItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
