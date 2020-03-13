package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailAdapter extends BaseAdapter {

    private String TAG = EmailAdapter.class.getSimpleName();

    private Context context = null;

    private List<String> emailList = new ArrayList<String>();

    public EmailAdapter(Context context) {
        this.context = context;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return emailList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_email, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvEmailItemNumber.setText(emailList.get(position));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_email_item_number)
        public TextView tvEmailItemNumber = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);

        }
    }
}
