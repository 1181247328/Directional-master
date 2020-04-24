package com.cdqf.dire_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_find.AuthorityFind;
import com.cdqf.dire_state.DireState;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 组队适配器
 */
public class GroupAdapter extends BaseAdapter {

    //当前打印名称
    private String TAG = GroupAdapter.class.getSimpleName();

    //上下文
    private Context context = null;

    //中间逻辑层
    private DireState direState = DireState.getDireState();

    //图片显示对象(用的第三方)
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //事件通用总线(用的第三方)
    private EventBus eventBus = EventBus.getDefault();

    //初始化
    public GroupAdapter(Context context) {
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
        return 3;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, null);
            //初始化组件注册类
            viewHolder = new ViewHolder(convertView);
            //保存组件注册类
            convertView.setTag(viewHolder);
        } else {
            //获取组件注册类
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            viewHolder.ivGroupItemBack.setImageResource(R.mipmap.team_captain);
            viewHolder.tvGroupItemTitle.setText("队长");
        } else {
            viewHolder.ivGroupItemBack.setImageResource(R.mipmap.team_captains);
            viewHolder.tvGroupItemTitle.setText("队员");
        }
        viewHolder.rcrlGroupItemApply.setOnClickListener(new OnAuthorityListener(position));
        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_group_item_back)
        public ImageView ivGroupItemBack = null;

        //组队职位
        @BindView(R.id.tv_group_item_title)
        public TextView tvGroupItemTitle = null;

        //名称
        @BindView(R.id.tv_group_item_name)
        public TextView tvGroupItemName = null;

        //权限
        @BindView(R.id.rcrl_group_item_apply)
        public RCRelativeLayout rcrlGroupItemApply = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    class OnAuthorityListener implements View.OnClickListener {

        private int position = 0;

        public OnAuthorityListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new AuthorityFind(position));
        }
    }
}

