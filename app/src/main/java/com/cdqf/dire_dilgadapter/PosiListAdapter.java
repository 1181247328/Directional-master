package com.cdqf.dire_dilgadapter;

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
 * 景点经过适配器
 */
public class PosiListAdapter extends BaseAdapter {

    private String TAG = PosiListAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    public PosiListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getPosiListList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_posilist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPosiItemName.setText(direState.getPosiListList().get(getCount() - 1 - position).getName());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_posi_item_name)
        public TextView tvPosiItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
