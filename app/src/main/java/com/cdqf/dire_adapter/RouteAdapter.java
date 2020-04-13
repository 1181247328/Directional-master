package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 路线适配器
 */
public class RouteAdapter extends BaseAdapter {

    private String TAG = RouteAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    public RouteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getRouteList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_route, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvRouteItemName.setText(direState.getRouteList().get(position).getTitle());
        viewHolder.tvRouteItemContext.setText(direState.getRouteList().get(position).getDesc());
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.rcrl_route_item_layout)
        public RCRelativeLayout rcrlRouteItemLayout = null;

        //竖
        @BindView(R.id.tv_route_item_title)
        public TextView tvRouteItemTitle = null;

        //名称
        @BindView(R.id.tv_route_item_name)
        public TextView tvRouteItemName = null;

        //内容
        @BindView(R.id.tv_route_item_context)
        public TextView tvRouteItemContext = null;

        //背景
        @BindView(R.id.tv_route_item_back)
        public TextView tvRouteItemBack = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
