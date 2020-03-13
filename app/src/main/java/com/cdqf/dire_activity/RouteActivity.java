package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.RouteAdapter;
import com.cdqf.dire_class.Live;
import com.cdqf.dire_class.Route;
import com.cdqf.dire_dilog.PayDilogFragment;
import com.cdqf.dire_dilog.RouteDilogFragment;
import com.cdqf.dire_dilog.WhyDilogFragment;
import com.cdqf.dire_find.BleOneFind;
import com.cdqf.dire_find.RouteFind;
import com.cdqf.dire_find.RouteLoginFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.cdqf.dire_utils.OnResponseHandler;
import com.cdqf.dire_utils.RequestHandler;
import com.cdqf.dire_utils.RequestStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 路线
 */
public class RouteActivity extends BaseActivity {

    private String TAG = RouteActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private HttpRequestWrap httpRequestWrap = null;

    //返回
    @BindView(R.id.rl_route_return)
    public RelativeLayout rlRouteReturn = null;

    //集合
    @BindView(R.id.lv_roture_list)
    public ListView lvRotureList = null;

    //无路线
    @BindView(R.id.ll_route_there)
    public LinearLayout llRouteThere = null;

    private RouteAdapter routeAdapter = null;

    private int position = 0;

    //代表不同活动页面,0=MainActivity
    private int isRoute = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvRotureList.setVisibility(View.VISIBLE);
                    llRouteThere.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvRotureList.setVisibility(View.GONE);
                    llRouteThere.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_route);

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
        httpRequestWrap = new HttpRequestWrap(context);
        httpRequestWrap.setMethod(HttpRequestWrap.POST);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        isRoute = intent.getIntExtra("isRoute", 0);
    }

    private void initView() {
    }

    private void initAdapter() {
        routeAdapter = new RouteAdapter(context);
        lvRotureList.setAdapter(routeAdapter);
    }

    private void initListener() {
        lvRotureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (direState.isLogin()) {
                    //是否需要支付
                    if (true) {
                        RouteDilogFragment routeDilogFragment = new RouteDilogFragment();
                        routeDilogFragment.setPosition(0, "是否选择路线" + direState.getRouteList().get(position).getTitle(), position);
                        routeDilogFragment.show(getSupportFragmentManager(), "确定选择的路线");
                    } else {
                        PayDilogFragment payDilogFragment = new PayDilogFragment();
                        payDilogFragment.show(getSupportFragmentManager(), "支付方式");
                    }
                } else {
                    WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                    whyDilogFragment.setInit(5, "提示", "您还没有登录,是否前往登录", "否", "是");
                    whyDilogFragment.show(getSupportFragmentManager(), "请登录");
                }
            }
        });
    }

    private void initBack() {
        route();
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    /**
     * 线路
     */
    private void route() {
        httpRequestWrap.setCallBack(new RequestHandler(context, 1, "请稍候", new OnResponseHandler() {
            @Override
            public void onResponse(String result, RequestStatus status) {
                Log.e(TAG, "---会馆路线---" + result);
                if (status == RequestStatus.SUCCESS) {
                    if (result != null) {
                        JSONObject resultJSON = JSON.parseObject(result);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        String data = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    handler.sendEmptyMessage(0x001);
                                    direState.getRouteList().clear();
                                    List<Route> routeList = gson.fromJson(data, new TypeToken<List<Route>>() {
                                    }.getType());
                                    direState.setRouteList(routeList);
                                    if (routeAdapter != null) {
                                        routeAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                //没线路
                                case 400:
                                    handler.sendEmptyMessage(0x002);
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
        int user_id = 0;
        switch (isRoute) {
            //MainActivity
            case 0:
                user_id = direState.getVenuesList().get(position).getId();
                break;
            //SearchActivity
            case 1:
                user_id = direState.getSearchList().get(position).getId();
                break;
        }
        params.put("user_id", user_id);
        httpRequestWrap.send(DirectAddaress.LIVE, params);
    }

    @OnClick({R.id.rl_route_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_route_return:
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
     * 确定
     *
     * @param r
     */
    public void onEventMainThread(final RouteFind r) {
//        direState.setRoute(true);
        httpRequestWrap.setCallBack(new RequestHandler(context, 1, "请稍候", new OnResponseHandler() {
            @Override
            public void onResponse(String result, RequestStatus status) {
                Log.e(TAG, "---会馆节点路线---" + result);
                if (status == RequestStatus.SUCCESS) {
                    if (result != null) {
                        JSONObject resultJSON = JSON.parseObject(result);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        String data = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    DirectPreferences.clearUserLive(context);
                                    direState.getLiveList().clear();
                                    //线路跟踪之选择路线
                                    direState.getUserLine().setRoute(true);
                                    List<Live> liveList = gson.fromJson(data, new TypeToken<List<Live>>() {
                                    }.getType());
                                    direState.setLiveList(liveList);
                                    /****线路的跟踪****/
                                    //线路跟踪之用户id
                                    direState.getUserLine().setId(direState.getUser().getId());
                                    //线路跟踪之当前线路id
                                    direState.getUserLine().setLineId(direState.getRouteList().get(r.position).getId());
                                    //线路跟踪之当前线路中第一个点的id或者用户所在的节点的id
                                    JSONObject nodeIdJSON = JSON.parseObject(resultJSON.getString("node_id"));
                                    int id = nodeIdJSON.getInteger("node_id");
                                    direState.getUserLine().setLinePositionId(id);
                                    eventBus.post(new BleOneFind(r.position));
                                    //线路跟踪之保存当前节点在哪个位置了
                                    if (direState.getRouteList().get(r.position).getStatus() == 3) {
                                        direState.getUserLine().setCustoms(true);
                                        direState.getUserLine().setAgree(true);
                                    } else {
                                        for (int i = 0; i < direState.getLiveList().size(); i++) {
                                            if (id == direState.getLiveList().get(i).getId()) {
                                                direState.getUserLine().setNumber(i + 1);
                                                break;
                                            }
                                        }
                                        if (direState.getUserLine().getNumber() == 1) {
                                            direState.getUserLine().setSign(false);
                                            direState.getUserLine().setAgree(false);
                                        } else {
                                            direState.getUserLine().setSign(true);
                                            direState.getUserLine().setAgree(true);
                                        }
                                    }
                                    /***线路状态保存***/
                                    DirectPreferences.setUserLive(context, direState.getUserLine());
                                    DirectPreferences.setUserLine(context, direState.getLiveList());
                                    finish();
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
        int id = direState.getUser().getId();
        params.put("user_id", id);
        Log.e(TAG, "---user_id---" + id);
        int live_id = direState.getRouteList().get(r.position).getId();
        Log.e(TAG, "---live_id---" + live_id);
        params.put("live_id", live_id);
        httpRequestWrap.send(DirectAddaress.NODES, params);
    }

    /**
     * 前往登录
     *
     * @param r
     */
    public void onEventMainThread(RouteLoginFind r) {
        initIntent(LoginActivity.class);
        finish();
    }
}