package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 勋章适配器
 */
public class MedalAdapter extends BaseAdapter {

    private String TAG = MedalAdapter.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();


    public MedalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return direState.getMedalList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_medal, null);
            viewHoloder = new ViewHoloder(convertView);
            convertView.setTag(viewHoloder);
        } else {
            viewHoloder = (ViewHoloder) convertView.getTag();
        }
        imageLoader.displayImage(direState.getMedalList().get(position).getMedal(), viewHoloder.ivMedalItemLoss,
                direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        return convertView;
    }

    class ViewHoloder {
        @BindView(R.id.iv_medal_item_loss)
        public ImageView ivMedalItemLoss = null;

        public ViewHoloder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
