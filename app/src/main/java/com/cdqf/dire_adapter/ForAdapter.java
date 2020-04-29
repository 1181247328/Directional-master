package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_find.ForFind;
import com.cdqf.dire_state.DireState;
import com.lnyp.imgdots.bean.ForPoint;
import com.lnyp.imgdots.bean.PointSimple;
import com.lnyp.imgdots.view.ImageLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 景区图片适配器
 */
public class ForAdapter extends BaseAdapter {

    //日志头名称
    private String TAG = ForAdapter.class.getSimpleName();

    //上下文
    private Context context = null;

    //通讯事件总线路
    private EventBus eventBus = EventBus.getDefault();

    //图片框架
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //中间逻辑层
    private DireState direState = DireState.getDireState();

    //宽度
    private int width;

    //高度
    private int height;

    //图一
    private List<ForPoint> forPointOneList = null;

    //地图二
    private List<ForPoint> forPointTwoList = null;

    //地图
    private int[] forMap = new int[]{
//            R.mipmap.for_one,
//            R.mipmap.for_two
    };

    //初始化
    public ForAdapter(Context context, int width, int height, List<ForPoint> forPointOneList, List<ForPoint> forPointTwoList) {
        this.context = context;
        this.width = width;
        this.height = height;
        this.forPointOneList = forPointOneList;
        this.forPointTwoList = forPointTwoList;
        imageLoader = direState.getImageLoader(context);
    }

    @Override
    public int getCount() {
        return forMap.length;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_for, null);
        viewHolder = new ViewHolder();
        viewHolder.ivPlantItemMap = convertView.findViewById(R.id.iv_plant_item_map);
        if (position == 0) {
            viewHolder.ivPlantItemMap.setForPoints(forPointOneList, 1);
        } else {
            viewHolder.ivPlantItemMap.setForPoints(forPointTwoList, 1);
        }
        int height = (int) (width * 1.6f);
        viewHolder.ivPlantItemMap.setImgBg(width, height, forMap[position]);

        viewHolder.ivPlantItemMap.setOnForMapListenerF(new OnPlantMapListener(position));
        return convertView;
    }

    class ViewHolder {
        //删除
        public RelativeLayout rlAvItemDelete = null;

        //图片
        public ImageView ivAvItemVideo = null;

        //标题
        public TextView tvAvItemTitle = null;

        //状态
        public TextView tvAvItemState = null;

        /**
         * 地图
         **/
        public ImageLayout ivPlantItemMap = null;
    }

    class OnPlantMapListener implements ImageLayout.OnForMapListener {

        private int scenicSpotId;

        private int type;

        public OnPlantMapListener(int position) {
            this.type = position;
        }

        @Override
        public void onMap(View view, ArrayList<PointSimple> points, int position) {

        }

        @Override
        public void onMap(View view, List<ForPoint> points, int position) {
            if (this.type == 0) {
                scenicSpotId = forPointOneList.get(position).getScenicSpotId();
            } else {
                scenicSpotId = forPointTwoList.get(position).getScenicSpotId();
            }
            eventBus.post(new ForFind(type, position, scenicSpotId));
        }
    }
}
