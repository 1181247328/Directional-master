package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_class.DetailsGame;
import com.cdqf.dire_find.AnswerSumitFind;
import com.cdqf.dire_find.BleFind;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 发现活动和景区
 */
public class DetailsGameActivity extends BaseActivity {
    private String TAG = DetailsGameActivity.class.getSimpleName();

    private Context context = null;

    public static DetailsGameActivity detailsGameActivity = null;

    private DireState direState = DireState.getDireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private HttpRequestWrap httpRequestWrap = null;

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private ACache aCache = null;

    //返回
    @BindView(R.id.rl_detailsgame_return)
    public RelativeLayout rlDetailsgameReturn = null;

    //标题
    @BindView(R.id.tv_detailsgame_title)
    public TextView tvDetailsgameTitle = null;

    //图片
    @BindView(R.id.iv_detailsgame_map)
    public ImageView ivDetailsgameMap = null;

    //开始答题
    @BindView(R.id.rcrl_detailsgame_questions)
    public RCRelativeLayout rcrlDetailsgameQuestions = null;

    @BindView(R.id.tv_detailsgame_questions)
    public TextView tvDetailsgameAuestions = null;

    @BindView(R.id.tv_detailsgame_views)
    public TextView tvDetailsgameViews = null;

    public int type = 0;

    public int position = 0;

    //获取BLE地址
    public String ble = "";

    private DetailsGame detailsGame = null;

    private boolean isStart = false;

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
        setContentView(R.layout.activity_detailsgame);

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
        detailsGameActivity = this;
        aCache = ACache.get(context);
        httpRequestWrap = new HttpRequestWrap(context);
        imageLoader = direState.getImageLoader(context);
        ButterKnife.bind(this);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        position = intent.getIntExtra("position", 0);
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        Log.e(TAG, "---蓝牙地址----" + direState.getBleList().size());
        for (String ble : direState.getBleList()) {
            Log.e(TAG, "---蓝牙地址----" + ble);
        }
        initPull();
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        String http = "";
        if (type == 1) {
            //id
            http = DirectAddaress.DETAILS;
            params.put("id", direState.getPeriodList().get(position).getId());
            Log.e(TAG, "---id发现活动---" + direState.getPeriodList().get(position).getId());
        } else {
            //id
            http = DirectAddaress.SCENERYS;
            params.put("id", direState.getScenicList().get(position).getId());
            Log.e(TAG, "---id发现景区---" + direState.getScenicList().get(position).getId());
        }

        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(http, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                String data = resultJSON.getString("Data");
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            detailsGame = gson.fromJson(data, DetailsGame.class);
                            direState.setDetailsGame(detailsGame);
                            tvDetailsgameTitle.setText(detailsGame.getTitle());
                            imageLoader.displayImage(DirectAddaress.ADDRESS + detailsGame.getImg(), ivDetailsgameMap, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
//                            if (aCache.getAsString("details" + direState.getUser().getId() + direState.getDetailsGame().getId()) != null) {
//                                String gameJSON = aCache.getAsString("details" + direState.getUser().getId() + direState.getDetailsGame().getId());
//                                DetailsGame game = gson.fromJson(gameJSON, DetailsGame.class);
//                                for (int i = 0; i < game.getAnswer().size(); i++) {
//                                    if (TextUtils.equals(detailsGame.getAnswer().get(i).getBle(), game.getAnswer().get(i).getBle())) {
//                                        detailsGame.getAnswer().get(i).setSelete(game.getAnswer().get(i).isSelete());
//                                    }
//                                }
//                            }
                            int position = 0;
                            String name = "";
                            for (int i = 0; i < detailsGame.getAnswer().size(); i++) {
                                if (!detailsGame.getAnswer().get(i).isSelete()) {
                                    ble = detailsGame.getAnswer().get(i).getBle();
                                    position = i + 1;
                                    name = detailsGame.getAnswer().get(i).getTitle();
                                    break;
                                }
                            }
                            for (String b : direState.getBleList()) {
                                Log.e(TAG, "---蓝牙地址----" + ble);
                                if (TextUtils.equals(b, ble)) {
                                    isStart = true;
                                    tvDetailsgameViews.setText("已到达第" + position + "个点");
                                    tvDetailsgameAuestions.setBackgroundColor(ContextCompat.getColor(context, R.color.main_team_back));
                                    break;
                                }
                            }
                            break;
                        case 20042:
                            direState.initToast(context, msg, true, 0);
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (error_code) {
                        case 20042:
                            direState.initToast(context, msg, true, 0);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("image", DirectAddaress.ADDRESS + detailsGame.getImg());
        startActivity(intent);
    }

    private void initIntentBle(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("ble", ble);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @OnClick({R.id.rl_detailsgame_return, R.id.rcrl_detailsgame_questions, R.id.iv_detailsgame_map})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_detailsgame_return:
                finish();
                break;
            case R.id.iv_detailsgame_map:
                initIntent(BigActivity.class);
                break;
            //开始答题
            case R.id.rcrl_detailsgame_questions:
                if (!isStart) {
                    direState.initToast(context, "请进入答题点", true, 0);
                    break;
                }
                initIntentBle(AnswerActivity.class);
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
        String name = "";
        int position = 0;
        for (int i = 0; i < detailsGame.getAnswer().size(); i++) {
            if (!detailsGame.getAnswer().get(i).isSelete()) {
                ble = detailsGame.getAnswer().get(i).getBle();
                name = detailsGame.getAnswer().get(i).getTitle();
                position = i + 1;
                break;
            }
        }
        for (String be : b.bleList) {
            Log.e(TAG, "---扫描到不重复蓝牙地址----" + ble);
            if (TextUtils.equals(be, ble)) {
                isStart = true;
                tvDetailsgameViews.setText("到达点" + position + "." + name);
                tvDetailsgameAuestions.setBackgroundColor(ContextCompat.getColor(context, R.color.main_team_back));
                direState.initToast(context, "可以开始答题了", true, 0);
                break;
            }
        }
    }

    /**
     * 看下一个点扫描到了吗
     *
     * @param a
     */
    public void onEventMainThread(AnswerSumitFind a) {
        String name = "";
        int position = 0;
        for (int i = 0; i < detailsGame.getAnswer().size(); i++) {
            if (!detailsGame.getAnswer().get(i).isSelete()) {
                ble = detailsGame.getAnswer().get(i).getBle();
                name = detailsGame.getAnswer().get(i).getTitle();
                position = i + 1;
                break;
            }
        }
        //假设没扫到
        isStart = false;
        for (String b : direState.getBleList()) {
            Log.e(TAG, "---蓝牙地址----" + ble);
            if (TextUtils.equals(b, ble)) {
                isStart = true;
                tvDetailsgameViews.setText("到达点" + position);
                tvDetailsgameAuestions.setBackgroundColor(ContextCompat.getColor(context, R.color.main_team_back));
                break;
            }
        }
        if (!isStart) {
            tvDetailsgameViews.setText("");
            tvDetailsgameAuestions.setBackgroundColor(ContextCompat.getColor(context, R.color.plant_address));
        }
    }
}
