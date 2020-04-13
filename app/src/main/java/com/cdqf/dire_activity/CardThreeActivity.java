package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.CardThreeAdapter;
import com.cdqf.dire_class.Choice;
import com.cdqf.dire_class.Live;
import com.cdqf.dire_dilog.RouteDilogFragment;
import com.cdqf.dire_find.BleFind;
import com.cdqf.dire_find.CardThreeFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.cdqf.dire_utils.OnResponseHandler;
import com.cdqf.dire_utils.RequestHandler;
import com.cdqf.dire_utils.RequestStatus;
import com.cdqf.dire_view.ListViewForScrollView;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 答题卡
 */
public class CardThreeActivity extends BaseActivity {

    private String TAG = CardThreeActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private HttpRequestWrap httpRequestWrap = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    //返回
    @BindView(R.id.rl_cardone_return)
    public RelativeLayout rlExploreReturn = null;

    @BindView(R.id.htv_cardthree_context)
    public HtmlTextView htvCardthreeContext = null;

    @BindView(R.id.tv_cardthree_note)
    public TextView tvCardthreeNote = null;

    @BindView(R.id.lvfsv_cardthree_list)
    public ListViewForScrollView lvfsvCardthreeList = null;

    private CardThreeAdapter cardThreeAdapter = null;

    private int position = 0;

    private String answer = "";

    /**
     * 未到达目标点
     */
    @BindView(R.id.ll_cardthree_layouttwo)
    public LinearLayout llCardthreeLayouttwo = null;

    //目标点
    @BindView(R.id.tv_cardthree_address)
    public TextView tvCardthreeAddress = null;

    //图片
    @BindView(R.id.iv_cardthree_address)
    public ImageView ivCardthreeAddress = null;

    @BindView(R.id.rl_cardthree_image)
    public RelativeLayout rlCardthreeImage = null;

    /**
     * 到达目标点
     */
    @BindView(R.id.ll_cardthree_layoutone)
    public RCRelativeLayout llCardthreeLayoutone = null;

    private String device = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    llCardthreeLayouttwo.setVisibility(View.VISIBLE);
                    llCardthreeLayoutone.setVisibility(View.GONE);
                    rlCardthreeImage.setVisibility(View.GONE);
                    break;
                case 0x002:
                    llCardthreeLayouttwo.setVisibility(View.GONE);
                    llCardthreeLayoutone.setVisibility(View.VISIBLE);
                    rlCardthreeImage.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_cardthree);

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
        httpRequestWrap = new HttpRequestWrap(context);
        httpRequestWrap.setMethod(HttpRequestWrap.POST);
    }

    private void initView() {
    }

    private void initAdapter() {
        cardThreeAdapter = new CardThreeAdapter(context);
        lvfsvCardthreeList.setAdapter(cardThreeAdapter);
    }

    private void initListener() {
        lvfsvCardthreeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cardThreeAdapter.setPositionColor(position);
                RouteDilogFragment routeDilogFragment = new RouteDilogFragment();
                routeDilogFragment.setPosition(1, "您选择的答案为" + direState.getChoiceList().get(position).getChoice(), position);
                routeDilogFragment.show(getSupportFragmentManager(), "确定选择的路线");
            }
        });
    }

    private void initBack() {

        direState.getUserLine().setAgree(true);
        //保存节点信息
        DirectPreferences.setUserLive(context, direState.getUserLine());

        htvCardthreeContext.setText(direState.getLiveList().get(direState.getUserLine().getNumber()).getContent());
        tvCardthreeNote.setText(direState.getLiveList().get(direState.getUserLine().getNumber()).getDesc());
        /*****没扫描到时*****/
        tvCardthreeAddress.setText(direState.getLiveList().get(direState.getUserLine().getNumber()).getTitle());
        imageLoader.displayImage(DirectAddaress.ADDRESS + direState.getLiveList().get(direState.getUserLine().getNumber()).getImg_url(), ivCardthreeAddress, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));

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
            initPull();
        } else {
            //未扫到
            handler.sendEmptyMessage(0x001);
        }

    }

    private void initPull() {
        httpRequestWrap.setCallBack(new RequestHandler(context, 1, "请稍候", new OnResponseHandler() {
            @Override
            public void onResponse(String result, RequestStatus status) {
                Log.e(TAG, "---当前节点的题目----" + result);
                Log.e(TAG, "---会馆节点路线---" + result);
                if (status == RequestStatus.SUCCESS) {
                    if (result != null) {
                        JSONObject resultJSON = JSON.parseObject(result);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        JSONObject data = resultJSON.getJSONObject("Data");
                        String choice = data.getString("choice");
                        answer = data.getString("answer");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    direState.getChoiceList().clear();
                                    List<Choice> choiceList = gson.fromJson(choice, new TypeToken<List<Choice>>() {
                                    }.getType());
                                    direState.setChoiceList(choiceList);
                                    if (cardThreeAdapter != null) {
                                        cardThreeAdapter.notifyDataSetChanged();
                                    }
                                    //没线路
                                case 400:
                                    direState.initToast(context, msg, true, 0);
                                    break;
                            }
                        }
                    } else {
                        direState.initToast(context, "获取数据失败,请检查网络", true, 0);
                    }
                } else {
                    direState.initToast(context, "请求失败,请检查网络", true, 0);
                }
            }
        }));
        Map<String, Object> params = new HashMap<String, Object>();
        int node_id = direState.getLiveList().get(direState.getUserLine().getNumber() - 1).getId();
        Log.e(TAG, "---node_id---" + node_id);
        params.put("node_id", node_id);
        httpRequestWrap.send(DirectAddaress.TOPICS, params);
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

    private void initIntentId(Class<?> activity, int live_id) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("live_id", live_id);
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
        for (int i = 0; i < direState.getBleList().size(); i++) {
            Log.e(TAG, "---" + direState.getBleList().get(i));
        }
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
            initPull();
        } else {
            //未扫到
            handler.sendEmptyMessage(0x001);
        }
    }

    public void onEventMainThread(CardThreeFind c) {
        Log.e(TAG, "---answer---" + answer + "---choice---" + direState.getChoiceList().get(c.position).getChoice());
        if (TextUtils.equals(answer, direState.getChoiceList().get(c.position).getChoice())) {
            final int number = direState.getUserLine().getNumber() + 1;
            Log.e(TAG, "---number---" + number);
            if (number == direState.getLiveList().size()) {
                Map<String, String> params = new HashMap<String, String>();
                //用户id
                params.put("user_id", String.valueOf(direState.getUserLine().getId()));
                //线路id
                params.put("live_id", String.valueOf(direState.getUserLine().getLineId()));
                Log.e(TAG, "---live_id---" + String.valueOf(direState.getUserLine().getLineId()));
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
                                            Log.e(TAG, "---" + direState.getUserLine().isCustoms());
                                            DirectPreferences.setUserLive(context, direState.getUserLine());
                                            Log.e(TAG, "---" + gson.toJson(direState.getUserLine()));
                                            direState.initToast(context, "恭喜通关", true, 0);
                                            initIntentId(RewardActivity.class, direState.getUserLine().getLineId());
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
            int sorts = 0;
            int live_id = 0;
            for (int i = 0; i < direState.getLiveList().size(); i++) {
                if (direState.getLiveList().get(i).getId() == direState.getUserLine().getLinePositionId()) {
                    sorts = direState.getLiveList().get(i).getId();
                    //获取下一个点的id
                    if (i == direState.getLiveList().size() - 1) {
                        //获得要跳转的游戏页面
                        sort = direState.getLiveList().get(i).getSort();
                        live_id = direState.getLiveList().get(i).getId();
                    } else {
                        //获得要跳转的游戏页面
                        sort = direState.getLiveList().get(i + 1).getSort();
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
            final int finalSorts = sorts;
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
                                        direState.getUserLine().setNumber(number + 1);
                                        DirectPreferences.setUserLive(context, direState.getUserLine());
                                        switch (finalSort) {
                                            case 1:
                                                initIntent(CardOneActivity.class);
                                                break;
                                            case 2:
                                                initIntent(CardTwoActivity.class);
                                                break;
                                            case 3:
                                                if (finalSort == finalSorts) {
                                                    initBack();
                                                } else {
                                                    initIntent(CardThreeActivity.class);
                                                }
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
        } else {
            direState.initToast(context, "请选择正确的答案", true, 0);
        }
    }
}
