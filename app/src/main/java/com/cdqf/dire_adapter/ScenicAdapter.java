package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现景区
 */
public class ScenicAdapter extends BaseAdapter {

    private String TAG = ScenicAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ScenicAdapter(Context context) {
        this.context = context;
        imageLoader = direState.getImageLoader(context);
    }

    @Override
    public int getCount() {
        return direState.getScenicList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_scenic, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //图片
        imageLoader.displayImage(DirectAddaress.ADDRESS + direState.getScenicList().get(position).getImg(),
                viewHolder.ivCementItemImage,
                direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        //标题
        viewHolder.tvCementItemTimer.setText(direState.getScenicList().get(position).getTitle());
        //线路起始点
        viewHolder.tvCementItemTitle.setText(direState.getScenicList().get(position).getName());
        //简介
        viewHolder.tvCementItemIntroduction.setText(direState.getScenicList().get(position).getIntroduction());
        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_cement_item_image)
        public ImageView ivCementItemImage = null;

        //时间
        @BindView(R.id.tv_cement_item_timer)
        public TextView tvCementItemTimer = null;

        //标题
        @BindView(R.id.tv_cement_item_title)
        public TextView tvCementItemTitle = null;

        //简介
        @BindView(R.id.tv_cement_item_introduction)
        public TextView tvCementItemIntroduction = null;


        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

