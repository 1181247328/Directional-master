package com.cdqf.dire_dilgadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthorityAdapter extends BaseAdapter {
    private String TAG = AuthorityAdapter.class.getSimpleName();

    private Context context = null;

    private String[] name = {
            "删除队员",
            "解散队伍",
            "转让队长",
            "退出队伍"
    };

    public AuthorityAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dilog_hear, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvHearItemName.setText(name[position]);
        return convertView;
    }

    class ViewHolder {
        //名称
        @BindView(R.id.tv_hear_item_name)
        public TextView tvHearItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
