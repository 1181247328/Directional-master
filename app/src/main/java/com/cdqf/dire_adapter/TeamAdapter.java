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
 * 我的队伍适配器
 */
public class TeamAdapter extends BaseAdapter {
    private String TAG = TeamAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    public TeamAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getTeam().getUsername1().size() + 1;
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
        if (viewHolder == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_team, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            viewHolder.tvTeamItemName.setText("队长:  " + direState.getTeam().getUsername().getNickname());
        } else {
            viewHolder.tvTeamItemName.setText("队员:  " + direState.getTeam().getUsername1().get(position-1));
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_team_item_name)
        public TextView tvTeamItemName = null;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
