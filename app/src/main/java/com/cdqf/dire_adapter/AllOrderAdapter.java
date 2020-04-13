package com.cdqf.dire_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_view.ListViewForScrollView;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * 全部订单适配器
 * Created by liu on 2017/11/20.
 */

public class AllOrderAdapter extends BaseAdapter {

    private String TAG = AllOrderAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    public AllOrderAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return 3;
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
        int type = 0;
        Log.e(TAG, "---type---" + type);
        switch (type) {
            //待支付
            case 0:
                PlantViewHolder plantOneViewHolder = null;
                convertView = LayoutInflater.from(context).inflate(R.layout.item_allforpayment, null);
                plantOneViewHolder = new PlantViewHolder();
                plantOneViewHolder.lvOrderItemList = convertView.findViewById(R.id.lv_order_item_list);
                //合计
                plantOneViewHolder.tvOrderItemCombined = convertView.findViewById(R.id.tv_order_item_combined);
                //状态
                plantOneViewHolder.tvOrderItemForpayment = convertView.findViewById(R.id.tv_order_item_forpayment);
                //取消订单
                plantOneViewHolder.rcrlOrderItemOne = convertView.findViewById(R.id.rcrl_order_item_one);
                //订单操作
                plantOneViewHolder.rcrlOrderItemThree = convertView.findViewById(R.id.rcrl_order_item_three);

                plantOneViewHolder.lvOrderItemList.setAdapter(new GoodsAdapter(position));
                break;
            //已支付
            case 1:
                PlantViewHolder plantTwoViewHolder = null;
                convertView = LayoutInflater.from(context).inflate(R.layout.item_sendgoods, null);
                plantTwoViewHolder = new PlantViewHolder();
                plantTwoViewHolder.lvOrderItemList = convertView.findViewById(R.id.lv_order_item_list);
                //合计
                plantTwoViewHolder.tvOrderItemCombined = convertView.findViewById(R.id.tv_order_item_combined);
                //状态
                plantTwoViewHolder.tvOrderItemForpayment = convertView.findViewById(R.id.tv_order_item_forpayment);

                plantTwoViewHolder.lvOrderItemList.setAdapter(new GoodsAdapter(position));
                break;
            case 2:
                PlantViewHolder plantThreeViewHolder = null;
                convertView = LayoutInflater.from(context).inflate(R.layout.item_forgoods, null);
                plantThreeViewHolder = new PlantViewHolder();
                plantThreeViewHolder.lvOrderItemList = convertView.findViewById(R.id.lv_order_item_list);
                //合计
                plantThreeViewHolder.tvOrderItemCombined = convertView.findViewById(R.id.tv_order_item_combined);
                //状态
                plantThreeViewHolder.tvOrderItemForpayment = convertView.findViewById(R.id.tv_order_item_forpayment);

                plantThreeViewHolder.lvOrderItemList.setAdapter(new GoodsAdapter(position));
                break;
        }
        return convertView;
    }

    class PlantViewHolder {

        //商品集合
        public ListViewForScrollView lvOrderItemList = null;

        //合计
        public TextView tvOrderItemCombined = null;

        //状态
        public TextView tvOrderItemForpayment = null;

        //订单操作
        public RCRelativeLayout rcrlOrderItemOne = null;

        //订单操作
        public RCRelativeLayout rcrlOrderItemThree = null;
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
            PlantViewHolder plantViewHolder = null;
            convertView = LayoutInflater.from(context).inflate(R.layout.itemtwo_goods, null);
            plantViewHolder = new PlantViewHolder();
            //图标
            plantViewHolder.ivOrderItemIcon = convertView.findViewById(R.id.iv_order_item_icon);
            //商品名称
            plantViewHolder.tvOrderItemTitle = convertView.findViewById(R.id.tv_order_item_title);
            //价格
            plantViewHolder.tvOrderItemPrice = convertView.findViewById(R.id.tv_order_item_price);
            //数量
            plantViewHolder.tvOrderItemNumber = convertView.findViewById(R.id.tv_order_item_number);
            return convertView;
        }

        class PlantViewHolder {
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
