package com.cdqf.dire_adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardThreeAdapter extends BaseAdapter {
    private String TAG = CardThreeAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private int posi = -1;

    public CardThreeAdapter(Context context) {
        this.context = context;
    }

    public void setPositionColor(int position) {
        this.posi = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return direState.getChoiceList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cardthree, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCardthreeItemTitle.setText(direState.getChoiceList().get(position).getChoice() + "." + direState.getChoiceList().get(position).getContent() + "分钟");
        if (posi == position) {
            viewHolder.tvCardthreeItemTitle.setTextColor(ContextCompat.getColor(context, R.color.card_one));
            setShape(viewHolder.tvCardthreeItemTitle, R.color.card_one, R.color.card_one);
        } else {
            setShape(viewHolder.tvCardthreeItemTitle, R.color.card_three_font, R.color.card_three_shape);
        }
        return convertView;
    }

    private void setShape(TextView t, int fontColor, int lineColor) {
        t.setTextColor(ContextCompat.getColor(context, fontColor));
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(ContextCompat.getColor(context, R.color.white));
        gradient.setCornerRadius(1f);
        gradient.setStroke(1, ContextCompat.getColor(context, lineColor));//描边的颜色和宽度
        gradient.setGradientType(GradientDrawable.RECTANGLE);
        t.setBackground(gradient);
    }

    class ViewHolder {
        @BindView(R.id.tv_cardthree_item_title)
        public TextView tvCardthreeItemTitle = null;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
