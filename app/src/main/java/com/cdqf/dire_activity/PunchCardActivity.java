package com.cdqf.dire_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.PunchCardAdapter;
import com.cdqf.dire_dilog.PunchCardDilogFragment;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StaturBar;
import com.cdqf.dire_view.LineGridView;
import com.cdqf.dire_view.VerticalSwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 打卡
 */
public class PunchCardActivity extends BaseActivity {

    //当前打印名称
    private String TAG = PunchCardActivity.class.getSimpleName();

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

    //异常情况显示
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    /*****本页组件注册********/

    @BindView(R.id.rl_main_return)
    public RelativeLayout rlMainReturn = null;

    //打卡集合页
    @BindView(R.id.lgv_punch_list)
    public LineGridView lgvPunchList = null;

    private PunchCardAdapter punchCardAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_punchcard);

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
        punchCardAdapter = new PunchCardAdapter(context);
        lgvPunchList.setAdapter(punchCardAdapter);
    }

    /**
     * 监听事件使用
     */
    private void initListener() {
        srlPlantPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlPlantPull.setRefreshing(false);
            }
        });
        lgvPunchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PunchCardDilogFragment punchCardDilogFragment = new PunchCardDilogFragment();
                punchCardDilogFragment.show(getSupportFragmentManager(), "确定打卡");
            }
        });
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
    @OnClick({R.id.rl_main_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.rl_main_return:
                finish();
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
     * 事件通用总线信息
     *
     * @param m
     */
    public void onEventMainThread(MainLoginFind m) {

    }
}
