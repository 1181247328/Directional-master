package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线路适配器
 */
public class LineAdapter extends BaseAdapter {

    private String TAG = LineAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    public LineAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getUserLiveList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_line, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvLineItemContext.setText(direState.getUserLiveList().get(position).getLine_name());
        int status = Integer.parseInt(direState.getUserLiveList().get(position).getState());
        switch (status) {
            case 1:
                viewHolder.tvLineItemTimer.setText("未开始");
                break;
            case 2:
                viewHolder.tvLineItemTimer.setText("进行中");
                break;
            case 3:
                viewHolder.tvLineItemTimer.setText("已结束");
                break;
            default:
                //TODO
                viewHolder.tvLineItemTimer.setText("未知");
                break;
        }
        return convertView;
    }

    class ViewHolder {
        //线路
        @BindView(R.id.tv_line_item_timer)
        public TextView tvLineItemTimer = null;

        //线路标题
        @BindView(R.id.tv_line_item_context)
        public TextView tvLineItemContext = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
