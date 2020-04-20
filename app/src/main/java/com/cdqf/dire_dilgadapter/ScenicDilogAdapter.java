package com.cdqf.dire_dilgadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.BleListAdapter;
import com.cdqf.dire_state.DireState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 搜索景区列表适配器
 */
public class ScenicDilogAdapter extends BaseAdapter {

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
    public ScenicDilogAdapter(Context context) {
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
        return 4;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dilog_scenic, null);
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

        //景区图片
        @BindView(R.id.iv_scenic_dilog_hear)
        public ImageView ivScenicDilogHear = null;

        //景区名称
        @BindView(R.id.tv_scenic_main_name)
        public TextView tvScenicMainName = null;

        //查看详情
        @BindView(R.id.ll_scenic_main_details)
        public LinearLayout llScenicMainDetails = null;

        //播放
        @BindView(R.id.rl_scenic_dilog_play)
        public RelativeLayout rlScenicDilogPlay = null;
        @BindView(R.id.iv_scenic_dilog_play)
        public ImageView ivScenicDilogPlay = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

