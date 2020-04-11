package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_class.Position;
import com.cdqf.dire_dilog.ForDilogFragment;
import com.cdqf.dire_dilog.MessageListDilogFragment;
import com.cdqf.dire_find.ExitFind;
import com.cdqf.dire_find.MessageFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 景点扫描到的详情页
 */
public class PositionActivity extends BaseActivity {

    private String TAG = PositionActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState cartState = DireState.getDireState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_myorder_return)
    public RelativeLayout rlMyorderReturn = null;

    //其它
    @BindView(R.id.rl_position_message)
    public RelativeLayout rlPositionMessage = null;

    //轮播图
    @BindView(R.id.mbv_position_banner)
    public MZBannerView mbvPositionBanner = null;

    //内容
    @BindView(R.id.htv_position_content)
    public HtmlTextView htvPositionContent = null;

    private Position p = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_position);

        //API>=20以上用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        ButterKnife.bind(this);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        initBanner();
//        htvPositionContent.setHtml("这是一个景区详情", new HtmlHttpImageGetter(htvPositionContent));
        htvPositionContent.setText("湘山纪念馆");
        initPull();
    }

    private void initBanner() {
        List<String> urlsList = new ArrayList<String>();
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585592585611&di=68f47b2388084787ec13f3259a845ea8&imgtype=0&src=http%3A%2F%2Fimg.album.texnet.com.cn%2Fview%2F2019%2F01%2F05%2F09%2F5c30557da2609.jpg");
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585592617725&di=7caac5a43876b6f4bd84b35a40b37b0e&imgtype=0&src=http%3A%2F%2Fpic.rmb.bdstatic.com%2Fc642a3e59ffbd932c73421e2c06a9616.jpeg%40wm_2%2Ct_55m%2B5a625Y%2B3L%2BeejOedoeWuhw%3D%3D%2Cfc_ffffff%2Cff_U2ltSGVp%2Csz_13%2Cx_9%2Cy_9");
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585592644097&di=6522af1e116310c17914d6d05fc17adc&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201812%2F01%2F20181201205841_MMiFr.jpeg");
        mbvPositionBanner.setPages(urlsList, new MZHolderCreator<BannerViewHolder>() {

            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mbvPositionBanner.setDelayedTime(3000);
        mbvPositionBanner.setIndicatorVisible(false);
        mbvPositionBanner.setDuration(1000);
        mbvPositionBanner.start();
    }

    private void initPull() {
        Map<String, String> params = new HashMap<String, String>();

        //用户id
        params.put("device", "C2:01:B0:00:01:8E");

        OkHttpUtils
                .get()
                .url(DirectAddaress.POSITION)
                .params(params)
                .build()
                .execute(new OKHttpStringCallback(context, true, "请稍候", new OnOkHttpResponseHandler() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        String dataJson = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    p = gson.fromJson(dataJson, Position.class);
                                    //图片
//                                    initBanner();
                                    //详情
                                    htvPositionContent.setHtml(p.getContent(), new HtmlHttpImageGetter(htvPositionContent));
                                    break;
                                //获取节点失败
                                case 400:
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                    }
                }));
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_position_message})
    public void onClick(View v) {
        switch (v.getId()) {
            //其它
            case R.id.rl_position_message:
                MessageListDilogFragment messageListDilogFragment = new MessageListDilogFragment();
                messageListDilogFragment.show(getSupportFragmentManager(), "其它");
                break;
        }
    }

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
        mbvPositionBanner.pause();
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
        mbvPositionBanner.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        eventBus.unregister(this);
    }

    class BannerViewHolder implements MZViewHolder<String> {

        private ImageView ivShopItemImage = null;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shop_banner, null);
            ivShopItemImage = view.findViewById(R.id.iv_shop_item_image);
            return view;
        }

        @Override
        public void onBind(Context context, int i, String s) {
            imageLoader.displayImage(s, ivShopItemImage, cartState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        }
    }

    /**
     * 语音介绍对话框
     *
     * @param r
     */
    public void onEventMainThread(MessageFind r) {
        ForDilogFragment forDilogFragment = new ForDilogFragment();
        forDilogFragment.setType(1);
        forDilogFragment.setName("景点名称");
        forDilogFragment.show(getSupportFragmentManager(), "景点名称");
    }

    /**
     * 退出登录
     *
     * @param r
     */
    public void onEventMainThread(ExitFind r) {
        exit();
    }

}

