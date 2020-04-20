package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 蓝牙列表组件适配器
 */
public class BleListAdapter extends BaseAdapter {

    //当前打印名称
    private String TAG = BleListAdapter.class.getSimpleName();

    //上下文
    private Context context = null;

    //中间逻辑层
    private DireState direState = DireState.getDireState();

    //图片显示对象(用的第三方)
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //事件通用总线(用的第三方)
    private EventBus eventBus = EventBus.getDefault();

    //初始化
    public BleListAdapter(Context context) {
        this.context = context;
        imageLoader = direState.getImageLoader(context);
    }

    /**
     * 列表数据数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return 33;
    }

    /**
     * 返回一个对象
     *
     * @param position
     * @return
     */
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
        //当前组件注册类
        ViewHolder viewHolder = null;
        //判断是否为空
        if (convertView == null) {
            //初始化一个view
            convertView = LayoutInflater.from(context).inflate(R.layout.item_blelist, null);
            //初始化组件注册类
            viewHolder = new ViewHolder(convertView);
            //保存组件注册类
            convertView.setTag(viewHolder);
        } else {
            //获取组件注册类
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {

        //名称
        @BindView(R.id.tv_ble_item_name)
        public TextView tvBleItemName = null;

        //地址
        @BindView(R.id.tv_ble_item_address)
        public TextView tvBleItemAddress = null;

        //距离
        @BindView(R.id.tv_ble_item_distance)
        public TextView tvBleItemDistance = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
