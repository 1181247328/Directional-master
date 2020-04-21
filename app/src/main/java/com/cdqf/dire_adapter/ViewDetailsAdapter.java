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
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 选项集合适配器
 */
public class ViewDetailsAdapter extends BaseAdapter {

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

    private int[] icon = {
            R.mipmap.viewdetails_clock,
            R.mipmap.viewdetails_experience,
            R.mipmap.viewdetails_course,
            R.mipmap.viewdetails_prep,
            R.mipmap.viewdetails_map,
            R.mipmap.viewdetails_service
    };

    private String[] name = {
            "景区打卡",
            "体验活动",
            "线上课程",
            "线上通关",
            "景区地图",
            "联系客服"
    };

    //初始化
    public ViewDetailsAdapter(Context context) {
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
        return name.length;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_viewdetails, null);
            //初始化组件注册类
            viewHolder = new ViewHolder(convertView);
            //保存组件注册类
            convertView.setTag(viewHolder);
        } else {
            //获取组件注册类
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //图片
        viewHolder.ivDetailsItemIcon.setImageResource(icon[position]);
        //名称
        viewHolder.tvDetailsItemTitle.setText(name[position]);
        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_details_item_icon)
        public ImageView ivDetailsItemIcon = null;

        //名称
        @BindView(R.id.tv_details_item_title)
        public TextView tvDetailsItemTitle = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

