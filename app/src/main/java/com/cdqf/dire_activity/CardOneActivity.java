package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_class.Live;
import com.cdqf.dire_find.BleFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 探索卡
 */
public class CardOneActivity extends BaseActivity {

    private String TAG = CardOneActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    //返回
    @BindView(R.id.rl_cardone_return)
    public RelativeLayout rlCardoneReturn = null;

    @BindView(R.id.htv_cardone_context)
    public HtmlTextView htvCardoneContext = null;

    @BindView(R.id.tv_cardone_note)
    public TextView tvCardoneNote = null;

    //输入密码
    @BindView(R.id.xet_cardone_password)
    public EditText xetCardonePassword = null;

    /**
     * 未到达目标点
     */
    @BindView(R.id.ll_cardone_layouttwo)
    public LinearLayout llCardoneLayouttwo = null;

    //目标点
    @BindView(R.id.tv_cardone_address)
    public TextView tvCardoneAddress = null;

    //图片
    @BindView(R.id.iv_cardone_address)
    public ImageView ivCardoneAddress = null;

    /**
     * 到达目标点
     */
    @BindView(R.id.ll_cardone_layoutone)
    public LinearLayout llCardoneLayoutone = null;

    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    private int position = 0;

    private String device = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    llCardoneLayouttwo.setVisibility(View.VISIBLE);
                    llCardoneLayoutone.setVisibility(View.GONE);
                    break;
                case 0x002:
                    llCardoneLayouttwo.setVisibility(View.GONE);
                    llCardoneLayoutone.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

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
        setContentView(R.layout.activity_cardone);

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
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        direState.setRoutePosition(position);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        imageLoader = direState.getImageLoader(context);
    }

    private void initView() {
    }

    private void initAdapter() {

    }

    private void initListener() {
        xetCardonePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pass = xetCardonePassword.getText().toString().length();
                if (pass == 5) {
                    final int number = direState.getUserLine().getNumber();
                    if (number == direState.getLiveList().size()) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("live_id", String.valueOf(direState.getUserLine().getLineId()));
                        int status = 3;
                        params.put("status", String.valueOf(status));
                        Log.e(TAG, "---通关---");
                        OkHttpUtils
                                .post()
                                .url(DirectAddaress.EDITLINE)
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
                                        if (isStatus) {
                                            switch (error_code) {
                                                //获取成功
                                                case 0:
                                                    direState.getUserLine().setCustoms(true);
                                                    direState.getUserLine().setNumber(number);
                                                    DirectPreferences.setUserLive(context,direState.getUserLine());
                                                    direState.initToast(context, "恭喜通关", true, 0);
                                                    initIntent(RewardActivity.class);
                                                    break;
                                                default:
                                                    direState.initToast(context, msg, true, 0);
                                                    break;
                                            }
                                        } else {
                                            switch (error_code) {
                                                default:
                                                    direState.initToast(context, msg, true, 0);
                                                    break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onOkHttpError(String error) {
                                        Log.e(TAG, "---onOkHttpError---" + error);
                                    }
                                }));
                        return;
                    }
                    int sort = 0;
                    int live_id = 0;
                    for (int i = 0; i < direState.getLiveList().size(); i++) {
                        if (direState.getLiveList().get(i).getId() == direState.getUserLine().getLinePositionId()) {
                            //获得要跳转的游戏页面
                            sort = direState.getLiveList().get(i).getSort();
                            //获取下一个点的id
                            if (i == direState.getLiveList().size() - 1) {
                                live_id = direState.getLiveList().get(i).getId();
                            } else {
                                live_id = direState.getLiveList().get(i + 1).getId();
                            }
                            break;
                        }
                    }
                    Map<String, String> params = new HashMap<String, String>();
                    //用户id
                    int id = direState.getUser().getId();
                    params.put("user_id", String.valueOf(id));
                    //当前线路的id
                    params.put("live_id", String.valueOf(direState.getUserLine().getLineId()));
                    //当前用户所在的线路的节点的id
                    int node_id = direState.getUserLine().getLinePositionId();
                    params.put("node_id", String.valueOf(node_id));
                    Log.e(TAG, "---" + sort + "---" + id + "---" + direState.getUserLine().getLineId() + "---" + node_id);
                    final int finalSort = sort;
                    final int finalLive_id = live_id;
                    OkHttpUtils
                            .post()
                            .url(DirectAddaress.USERLIVEID)
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
                                    if (isStatus) {
                                        switch (error_code) {
                                            //获取成功
                                            case 0:
                                                direState.getUserLine().setLinePositionId(finalLive_id);
                                                direState.getUserLine().setNumber(number+1);
                                                DirectPreferences.setUserLive(context,direState.getUserLine());
                                                switch (finalSort) {
                                                    case 1:
                                                        initIntent(CardOneActivity.class);
                                                        break;
                                                    case 2:
                                                        initIntent(CardTwoActivity.class);
                                                        break;
                                                    case 3:
                                                        initIntent(CardThreeActivity.class);
                                                        break;
                                                    default:
                                                        direState.initToast(context, "您选择的点没有游戏", true, 0);
                                                        break;
                                                }
                                                break;
                                            default:
                                                direState.initToast(context, msg, true, 0);
                                                break;
                                        }
                                    } else {
                                        switch (error_code) {
                                            default:
                                                direState.initToast(context, msg, true, 0);
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
            }
        });
    }

    private void initBack() {
        htvCardoneContext.setText(direState.getLiveList().get(position).getContent());
        tvCardoneNote.setText(direState.getLiveList().get(position).getDesc());
        /*****没扫描到时*****/
        tvCardoneAddress.setText(direState.getLiveList().get(position).getTitle());
        imageLoader.displayImage(DirectAddaress.ADDRESS + direState.getLiveList().get(position).getImg_url(), ivCardoneAddress, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));

        //通过跟踪记录的当前线路的当前点的id来查找当前点的蓝牙地址
        for (Live l : direState.getLiveList()) {
            if (l.getId() == direState.getUserLine().getLinePositionId()) {
                device = l.getDevice();
                break;
            }
        }
        //判断是不是已经扫描到这个点了
        boolean isBle = false;
        for (int i = 0; i < direState.getBleList().size(); i++) {
            if (TextUtils.equals(direState.getBleList().get(i), device)) {
                isBle = true;
                break;
            }
        }
        if (isBle) {
            //扫到
            handler.sendEmptyMessage(0x002);
        } else {
            //未扫到
            handler.sendEmptyMessage(0x001);
        }
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        finish();
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.rl_cardone_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_cardone_return:
                finish();
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
        eventBus.unregister(this);
    }

    /**
     * 扫描到ble后但已经选择路线
     *
     * @param b
     */
    public void onEventMainThread(BleFind b) {
        direState.setBleList(b.bleList);
        boolean isBle = false;
        for (int i = 0; i < direState.getBleList().size(); i++) {
            if (TextUtils.equals(direState.getBleList().get(i), device)) {
                isBle = true;
                break;
            }
        }

        if (isBle) {
            //扫到
            handler.sendEmptyMessage(0x002);
        } else {
            //未扫到
            handler.sendEmptyMessage(0x001);
        }
    }
}

