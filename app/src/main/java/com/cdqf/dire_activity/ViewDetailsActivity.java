package com.cdqf.dire_activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.ViewDetailsAdapter;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StaturBar;
import com.cdqf.dire_view.MyGridView;
import com.cdqf.dire_view.VerticalSwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 景区详情
 */
public class ViewDetailsActivity extends BaseActivity {

    //当前打印名称
    private String TAG = BleListActivity.class.getSimpleName();

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

    //返回按钮
    @BindView(R.id.rl_main_return)
    public RelativeLayout rlMainReturn = null;

    //轮播图
    @BindView(R.id.mzbv_details_list)
    public MZBannerView mzbvDetailsList = null;

    //播放声音
    @BindView(R.id.rl_main_play)
    public RelativeLayout rlMainPlay = null;
    @BindView(R.id.iv_main_play)
    public ImageView ivMainPlay = null;

    //选项集合
    @BindView(R.id.mgv_details_list)
    public MyGridView mgvDetailsList = null;

    //适配器
    private ViewDetailsAdapter viewDetailsAdapter = null;

    //时间
    @BindView(R.id.tv_details_timer)
    public TextView tvDetailsTimer = null;

    //地址
    @BindView(R.id.tv_details_address)
    public TextView tvDetailsAddress = null;

    //内容
    @BindView(R.id.htv_details_content)
    public HtmlTextView htvDetailsContent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_viewdetails);

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
        viewDetailsAdapter = new ViewDetailsAdapter(context);
        mgvDetailsList.setAdapter(viewDetailsAdapter);
    }

    /**
     * 监听事件使用
     */
    private void initListener() {
        mgvDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        //打卡
                        break;
                }
            }
        });
    }

    /**
     * 事件逻辑层使用
     */
    private void initBack() {

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
    @OnClick({R.id.rl_main_return, R.id.rl_main_play})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.rl_main_return:
                finish();
                break;
            //播放语音介绍
            case R.id.rl_main_play:
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
        mzbvDetailsList.start();
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
