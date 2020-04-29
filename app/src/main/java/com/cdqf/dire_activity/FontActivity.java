package com.cdqf.dire_activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.ForAdapter;
import com.cdqf.dire_find.ForFind;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StaturBar;
import com.cdqf.dire_view.VerticalSwipeRefreshLayout;
import com.lnyp.imgdots.bean.ForPoint;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.widget.HListView;

/**
 * 景区地图
 */
public class FontActivity extends BaseActivity {

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

    @BindView(R.id.rl_main_return)
    public RelativeLayout rlMainReturn = null;

    //目的地
    @BindView(R.id.tv_for_place)
    public TextView tvForPlace = null;

    //地图
    @BindView(R.id.hlv_for_list)
    public HListView hlvForList = null;

    //规划
    @BindView(R.id.rcrl_for_planning)
    public TextView rcrlForPlanning = null;

    @BindView(R.id.iv_for_post)
    public ImageView ivForPost = null;

    private ForAdapter forAdapter = null;

    private List<ForPoint> forPointOneList = new ArrayList<>();

    private List<ForPoint> forPointTwoList = new ArrayList<>();

    private int width;

    private int height;

    private String longitude;

    private String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_font);

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
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
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
        forAdapter = new ForAdapter(context, width, height, forPointOneList, forPointTwoList);
        hlvForList.setAdapter(forAdapter);
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
    @OnClick({R.id.tv_for_place, R.id.rcrl_for_planning, R.id.iv_for_post})
    public void onClick(View v) {
        switch (v.getId()) {
            //线路规划
            case R.id.rcrl_for_planning:
                break;
            //搜索
            case R.id.tv_for_place:
                break;
            //定位最近
            case R.id.iv_for_post:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
        for (ForPoint f : forPointOneList) {
            f.setLoactionMap(false);
        }
        for (ForPoint f : forPointTwoList) {
            f.setLoactionMap(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        eventBus.unregister(this);
    }

    /**
     * 事件通用总线信息
     *
     * @param m
     */
    public void onEventMainThread(MainLoginFind m) {

    }

    public void onEventMainThread(final ForFind f) {
//        Log.e(TAG, "---景点id---" + f.scenicSpotId);
//        httpRequestWrap.setCallBack(new RequestHandler(getContext(), 1, plantState.getPlantString(getContext(), R.string.please_while), new OnResponseHandler() {
//            @Override
//            public void onResponse(String result, RequestStatus status) {
//                String data = Errer.isResult(getContext(), result, status);
//                if (data == null) {
//                    Log.e(TAG, "---获取景点详情解密失败---" + data);
//                    return;
//                }
//                Log.e(TAG, "---获取景点详情解密成功---");
//                plantState.showLogCompletion(TAG, data, 1024);
//                ForDetails forDetails = new ForDetails();
//                forDetails = gson.fromJson(data, ForDetails.class);
//                plantState.setForDetails(forDetails);
//                ForDilogFragment forDilogFragment = new ForDilogFragment();
//                if (f.type == 0) {
//                    forDilogFragment.setPath(forPointOneList.get(f.position).getHttpPic());
//                } else if (f.type == 1) {
//                    forDilogFragment.setPath(forPointTwoList.get(f.position).getHttpPic());
//                }
////                if (forDetails.getPicList().size() > 0) {
////                    forDilogFragment.setPath(forDetails.getPicList().get(0).getHttpPic());
////                }
//                if (forDetails.getVoiceList().size() > 0) {
//                    forDilogFragment.setVoice(forDetails.getVoiceList().get(0).getHttpVoice());
////                    forDilogFragment.setEnglishvoice(forDetails.getVoiceList().get(0).getEnglishvoice());
//                    forDilogFragment.setEnglishvoice(forDetails.getVoiceList().get(1).getHttpVoice());
//                }
//                forDilogFragment.show(getFragmentManager(), "景点");
//            }
//        }));
//        Map<String, Object> params = new HashMap<String, Object>();
//        int spotId = f.scenicSpotId;
//        params.put("spotId", spotId);
//        //随机数
//        int random = plantState.getRandom();
//        String sign = random + "" + spotId;
//        Log.e(TAG, "---明文---" + random);
//        //加密文字
//        String signEncrypt = null;
//        try {
//            signEncrypt = DESUtils.encryptDES(sign, Constants.secretKey.substring(0, 8));
//            Log.e(TAG, "---加密成功---" + signEncrypt);
//        } catch (Exception e) {
//            Log.e(TAG, "---加密失败---");
//            e.printStackTrace();
//        }
//        if (signEncrypt == null) {
//            plantState.initToast(getContext(), "加密失败", true, 0);
//        }
//        //随机数
//        params.put("random", random);
//        params.put("sign", signEncrypt);
//        httpRequestWrap.send(PlantAddress.FOR_GETDETAILS, params);
    }

}
