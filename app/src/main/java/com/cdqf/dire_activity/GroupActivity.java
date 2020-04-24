package com.cdqf.dire_activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.GroupAdapter;
import com.cdqf.dire_dilog.AuthorityDilogFragment;
import com.cdqf.dire_dilog.TeamDilogFragment;
import com.cdqf.dire_find.AuthorityFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StaturBar;
import com.cdqf.dire_view.ListViewForScrollView;
import com.cdqf.dire_view.VerticalSwipeRefreshLayout;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 组队
 */
public class GroupActivity extends BaseActivity {

    //当前打印名称
    private String TAG = GroupActivity.class.getSimpleName();

    //上下文
    private Context context = null;

    //中间逻辑层
    private DireState direState = DireState.getDireState();

    //图片显示对象(用的第三方)
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //事件通用总线(用的第三方)
    private EventBus eventBus = EventBus.getDefault();

    /*****通用组件注册******/
    //下拉刷新
    @BindView(R.id.srl_plant_pull)
    public VerticalSwipeRefreshLayout srlPlantPull = null;

    //进度条显示
    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //与进度条和没有团队的时候显示页面的父组件
    @BindView(R.id.ll_group_no)
    public LinearLayout llGroupNo = null;

    //异常情况显示
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    /*****本页组件注册********/

    //返回
    @BindView(R.id.rl_main_return)
    public RelativeLayout rlMainReturn = null;

    //创建团队
    @BindView(R.id.rl_main_message)
    public RelativeLayout rlMainMessage = null;

    /*************没有加入团队的显示页面*************/

    //没有团队的时候显示页面
    @BindView(R.id.tv_group_add)
    public LinearLayout tvGroupAdd = null;

    //搜索团队
    @BindView(R.id.rcrl_group_seek)
    public RCRelativeLayout rcrlGroupSeek = null;

    //添加团队
    @BindView(R.id.rcrl_group_add)
    public RCRelativeLayout rcrlGroupAdd = null;

    /**************有团队的情况下显示的页面***********/
    //组队名称
    @BindView(R.id.rcrl_group_name)
    public TextView rcrlGroupName = null;

    //人数
    @BindView(R.id.tv_group_number)
    public TextView tv_group_number = null;

    //组队
    @BindView(R.id.lvfsv_group_list)
    public ListViewForScrollView lvfsvGroupList = null;

    //适配器
    private GroupAdapter groupAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_group);

        //沉侵式菜单栏颜色
        StaturBar.setStatusBar(this, R.color.white);

        //改变沉侵式状态栏文字颜色
        direState.changeStatusBarTextImgColor(this, true);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    /**
     * 初始化使用
     */
    private void initAgo() {
        //上下文
        context = this;
        //注册ButterKnife
        ButterKnife.bind(this);
        //初始化ImageLoader
        imageLoader = direState.getImageLoader(context);
        //注册eventBus
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    /**
     * 组件初始化使用
     */
    private void initView() {
    }

    /**
     * 适配器通用
     */
    private void initAdapter() {
        groupAdapter = new GroupAdapter(context);
        lvfsvGroupList.setAdapter(groupAdapter);
    }

    /**
     * 监听事件使用
     */
    private void initListener() {

    }

    /**
     * 事件逻辑层使用
     */
    private void initBack() {

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @OnClick({R.id.rl_main_return, R.id.rl_main_message, R.id.rcrl_group_seek, R.id.rcrl_group_add})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.rl_main_return:
                finish();
                break;
            //创建团队
            case R.id.rl_main_message:
                TeamDilogFragment teamAddDilogFragment = new TeamDilogFragment();
                teamAddDilogFragment.initTeam("创建队伍");
                teamAddDilogFragment.show(getSupportFragmentManager(), "创建队伍");
                break;
            //搜索团队
            case R.id.rcrl_group_seek:
                break;
            //添加团队
            case R.id.rcrl_group_add:
                TeamDilogFragment teamDilogFragment = new TeamDilogFragment();
                teamDilogFragment.initTeam("加入队伍");
                teamDilogFragment.show(getSupportFragmentManager(), "加入队伍");
                break;
        }
    }

    /**
     * 返回建关闭当前页面
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "---启动---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "---恢复---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        //注销eventBus
        eventBus.unregister(this);
    }

    /**
     * 队伍权限
     *
     * @param m
     */
    public void onEventMainThread(AuthorityFind m) {
        AuthorityDilogFragment authorityDilogFragment = new AuthorityDilogFragment();
        authorityDilogFragment.show(getSupportFragmentManager(), "队伍权限");
    }
}
