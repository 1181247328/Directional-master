package com.cdqf.dire_activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.ProbeAwardAdapter;
import com.cdqf.dire_adapter.ProbePlanAdapter;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StaturBar;
import com.cdqf.dire_view.ListViewForScrollView;
import com.cdqf.dire_view.VerticalSwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 体验类活动界面
 */
public class ProbeActivity extends BaseActivity {

    //当前打印名称
    private String TAG = ProbeActivity.class.getSimpleName();

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

    //返回
    @BindView(R.id.rl_main_return)
    public RelativeLayout rlMainReturn = null;

    //轮播图
    @BindView(R.id.mzbv_details_list)
    public MZBannerView mzbvDetailsList = null;

    //标题
    @BindView(R.id.tv_probe_title)
    public TextView tvProbeTitle = null;

    //内容
    @BindView(R.id.tv_probe_context)
    public TextView tvProbeContext = null;

    //活动规划
    @BindView(R.id.lvfsv_probe_list)
    public ListViewForScrollView lvfsvProbeList = null;
    private ProbePlanAdapter probePlanAdapter = null;

    //活动奖励
    @BindView(R.id.lvfsv_probe_award)
    public ListViewForScrollView lvfsvProbeAward = null;
    private ProbeAwardAdapter probeAwardAdapter = null;

    //输入密码
    @BindView(R.id.et_probe_password)
    public EditText etProbeassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_probe);

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
        probePlanAdapter = new ProbePlanAdapter(context);
        lvfsvProbeList.setAdapter(probePlanAdapter);
        probeAwardAdapter = new ProbeAwardAdapter(context);
        lvfsvProbeAward.setAdapter(probeAwardAdapter);
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

        //
        initBanner();
    }

    private void initBanner() {
        List<String> urlsList = new ArrayList<String>();
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587363916983&di=e4016258434f9eba03eb56082860922c&imgtype=0&src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110111%2F292-110111035J3100.jpg");
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587363916983&di=e4016258434f9eba03eb56082860922c&imgtype=0&src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110111%2F292-110111035J3100.jpg");
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587363916983&di=e4016258434f9eba03eb56082860922c&imgtype=0&src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110111%2F292-110111035J3100.jpg");
        mzbvDetailsList.setPages(urlsList, new MZHolderCreator<BannerViewHolder>() {

            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mzbvDetailsList.setDelayedTime(3000);
        mzbvDetailsList.setIndicatorVisible(false);
        mzbvDetailsList.setDuration(1000);
        mzbvDetailsList.start();
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
        mzbvDetailsList.start();
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
        mzbvDetailsList.pause();
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

    class BannerViewHolder implements MZViewHolder<String> {

        //图片
        private ImageView ivMainImage = null;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shop_banner, null);
            ivMainImage = view.findViewById(R.id.iv_shop_item_image);
            return view;
        }

        @Override
        public void onBind(Context context, int i, String s) {
            imageLoader.displayImage(s, ivMainImage, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        }
    }

    /**
     * 事件通用总线信息
     *
     * @param m
     */
    public void onEventMainThread(MainLoginFind m) {

    }
}
