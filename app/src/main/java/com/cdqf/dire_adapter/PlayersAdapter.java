package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_find.PlayersDeletelFind;
import com.cdqf.dire_state.DireState;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 添加队员适配器
 */
public class PlayersAdapter extends BaseAdapter {
    private String TAG = PlayersAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    public PlayersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getPlayersList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_players, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPlayersItemName.setText("队员:  " + direState.getPlayersList().get(position));
        viewHolder.tvPlayersItemDelete.setOnClickListener(new OnDeleteListener(position));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_players_item_name)
        public TextView tvPlayersItemName = null;

        //删除
        @BindView(R.id.tv_players_item_delete)
        public TextView tvPlayersItemDelete = null;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class OnDeleteListener implements View.OnClickListener {

        private int position;

        public OnDeleteListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new PlayersDeletelFind(position));
        }
    }
}

