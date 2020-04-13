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
 * 搜索适配器
 */
public class SearchAdapter extends BaseAdapter {

    private String TAG = SearchAdapter.class.getSimpleName();

    private DireState direState = DireState.getDireState();

    private Context context = null;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getSearchList().size();
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
        ViewHoloder viewHoloder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            viewHoloder = new ViewHoloder(convertView);
            convertView.setTag(viewHoloder);
        } else {
            viewHoloder = (ViewHoloder) convertView.getTag();
        }
        viewHoloder.tvSearchItemTitle.setText(direState.getSearchList().get(position).getTitle());
        return convertView;
    }

    class ViewHoloder {
        @BindView(R.id.tv_search_item_title)
        public TextView tvSearchItemTitle = null;

        public ViewHoloder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
