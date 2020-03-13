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
import com.cdqf.dire_view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 已完成适配器
 */
public class ForGoodsAdapter extends BaseAdapter {

    private String TAG = ForGoodsAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    public ForGoodsAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return 5;
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
        PlantViewHolder plantViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_forgoods, null);
            plantViewHolder = new PlantViewHolder(convertView);
            convertView.setTag(plantViewHolder);
        } else {
            plantViewHolder = (PlantViewHolder) convertView.getTag();
        }

        plantViewHolder.lvOrderItemList.setAdapter(new GoodsAdapter(position));
        return convertView;
    }

    class PlantViewHolder {

        //商品集合
        @BindView(R.id.lv_order_item_list)
        public ListViewForScrollView lvOrderItemList = null;

        //合计
        @BindView(R.id.tv_order_item_combined)
        public TextView tvOrderItemCombined = null;

        //状态
        @BindView(R.id.tv_order_item_forpayment)
        public TextView tvOrderItemForpayment = null;

        public PlantViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 商品适配器
     */
    class GoodsAdapter extends BaseAdapter {

        private int position;

        public GoodsAdapter(int position) {
            this.position = position;

        }

        @Override
        public int getCount() {
            return 1;
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
            PlantGoodsViewHolder plantGoodsViewHolder = null;
            convertView = LayoutInflater.from(context).inflate(R.layout.itemtwo_goods, null);
            plantGoodsViewHolder = new PlantGoodsViewHolder();
            //图标
            plantGoodsViewHolder.ivOrderItemIcon = convertView.findViewById(R.id.iv_order_item_icon);
            //商品名称
            plantGoodsViewHolder.tvOrderItemTitle = convertView.findViewById(R.id.tv_order_item_title);
            //价格
            plantGoodsViewHolder.tvOrderItemPrice = convertView.findViewById(R.id.tv_order_item_price);
            //数量
            plantGoodsViewHolder.tvOrderItemNumber = convertView.findViewById(R.id.tv_order_item_number);
            return convertView;
        }

        class PlantGoodsViewHolder {
            //图标
            public ImageView ivOrderItemIcon = null;

            //商品名称
            public TextView tvOrderItemTitle = null;

            //价格
            public TextView tvOrderItemPrice = null;

            //数量
            public TextView tvOrderItemNumber = null;

        }
    }
}
