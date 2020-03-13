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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.PeriodAdapter;
import com.cdqf.dire_class.DetailsGame;
import com.cdqf.dire_class.Period;
import com.cdqf.dire_dilog.WhyDilogFragment;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 发现活动
 */
public class PeriodActivity extends BaseActivity {

    private String TAG = PeriodActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_cement_return)
    public RelativeLayout rlCementReturn = null;

    //标题
    @BindView(R.id.tv_cement_title)
    public TextView tvCementTitle = null;

    @BindView(R.id.ll_cement_there)
    public LinearLayout llCementThere = null;

    //集合
    @BindView(R.id.ptrl_cement_pull)
    public PullToRefreshLayout ptrlCementPull = null;

    private ListView lvCementPull = null;

    private PeriodAdapter periodAdapter = null;

    private int page = 1;

    private ACache aCache = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    llCementThere.setVisibility(View.VISIBLE);
                    ptrlCementPull.setVisibility(View.GONE);
                    break;
                case 0x002:
                    llCementThere.setVisibility(View.GONE);
                    ptrlCementPull.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private DetailsGame game = null;

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
        setContentView(R.layout.activity_period);

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
        imageLoader = direState.getImageLoader(context);
        aCache = ACache.get(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        lvCementPull = (ListView) ptrlCementPull.getPullableView();
    }

    private void initAdapter() {
        periodAdapter = new PeriodAdapter(context);
        lvCementPull.setAdapter(periodAdapter);
    }

    private void initListener() {
        lvCementPull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (direState.isLogin()) {
//                    if (aCache.getAsString("details1" + direState.getUser().getId() + direState.getPeriodList().get(position).getId()) != null) {
//                        String gameJSON = aCache.getAsString("details1" + direState.getUser().getId() + direState.getPeriodList().get(position).getId());
//                        game = gson.fromJson(gameJSON, DetailsGame.class);
//                        boolean isSelete = game.getAnswer().get(game.getAnswer().size() - 1).isSelete();
//                        if (isSelete) {
//                            initIntentReward(RewardActivity.class, direState.getPeriodList().get(position).getId());
//                        } else {
//                            initIntent(DetailsGameActivity.class, position, 1);
//                        }
//                    } else {
                        initIntent(DetailsGameActivity.class, position, 1);
//                    }

                } else {
                    WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                    whyDilogFragment.setInit(0, "提示", "您当前未登录,是否前往登录", "否", "是");
                    whyDilogFragment.show(getSupportFragmentManager(), "登录");
                }
            }
        });
        ptrlCementPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            //下拉刷新
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
                Map<String, Object> params = new HashMap<String, Object>();
//                //页数
//                params.put("limit", page);
//                //条数
//                params.put("tatol", 10);
                //查询字样
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(DirectAddaress.PERIOD, false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse下拉刷新---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        String data = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    aCache.put("periodPage", page);
                                    page = 2;
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    handler.sendEmptyMessage(0x002);
                                    direState.getPeriodList().clear();
                                    List<Period> periodList = gson.fromJson(data, new TypeToken<List<Period>>() {
                                    }.getType());
                                    direState.setPeriodList(periodList);
                                    if (periodAdapter != null) {
                                        periodAdapter.notifyDataSetChanged();
                                    }
                                    aCache.put("period", data);
                                    break;
                                case 20042:
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    direState.initToast(context, msg, true, 0);
                                    break;
                                default:
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                    handler.sendEmptyMessage(0x001);
                                    break;
                            }
                        } else {
                            switch (error_code) {
                                case 20042:
                                    direState.initToast(context, msg, true, 0);
                                    break;
                                default:
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                    handler.sendEmptyMessage(0x001);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                });
            }

            //上拉加载
            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                Map<String, Object> params = new HashMap<String, Object>();
                //页数
                params.put("limit", page);
                //条数
                params.put("tatol", 10);
                //查询字样
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(DirectAddaress.GAME, false, "查找中", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse上拉加载---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        String data = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
//                                    page++;
//                                    handler.sendEmptyMessage(0x002);
//                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                                    List<Cement> cementList = gson.fromJson(data, new TypeToken<List<Cement>>() {
//                                    }.getType());
//                                    direState.getCementList().addAll(cementList);
//                                    if (cementAdapter != null) {
//                                        cementAdapter.notifyDataSetChanged();
//                                    }
//                                    String cement = gson.toJson(cementList);
//                                    aCache.put("periodPage", page);
//                                    aCache.put("period", cement);
                                    break;
                                case 20042:
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    direState.initToast(context, msg, true, 0);
                                    break;
                                default:
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                    handler.sendEmptyMessage(0x001);
                                    break;
                            }
                        } else {
                            switch (error_code) {
                                case 20042:
                                    direState.initToast(context, msg, true, 0);
                                    break;
                                default:
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                    handler.sendEmptyMessage(0x001);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                });
            }
        });
    }

    private void initBack() {
        if (aCache.getAsString("period") != null) {
            String data = aCache.getAsString("period");
//            page = (int) aCache.getAsObject("periodPage");
            handler.sendEmptyMessage(0x002);
            direState.getPeriodList().clear();
            List<Period> periodList = gson.fromJson(data, new TypeToken<List<Period>>() {
            }.getType());
            direState.setPeriodList(periodList);
            if (periodAdapter != null) {
                periodAdapter.notifyDataSetChanged();
            }
        }
        ptrlCementPull.setPullUpEnable(false);
        initPull();
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
//        //页数
//        params.put("limit", page);
//        //条数
//        params.put("tatol", 10);
        //查询字样
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.PERIOD, true, "请稍候", params, new OnHttpRequest() {
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
                            aCache.put("periodPage", page);
                            page = 2;
                            handler.sendEmptyMessage(0x002);
                            direState.getPeriodList().clear();
                            List<Period> periodList = gson.fromJson(data, new TypeToken<List<Period>>() {
                            }.getType());
                            direState.setPeriodList(periodList);
                            if (periodAdapter != null) {
                                periodAdapter.notifyDataSetChanged();
                            }
                            aCache.put("period", data);
                            break;
                        case 20042:
                            direState.initToast(context, msg, true, 0);
                            break;
                        default:
                            handler.sendEmptyMessage(0x001);
                            break;
                    }
                } else {
                    switch (error_code) {
                        case 20042:
                            direState.initToast(context, msg, true, 0);
                            break;
                        default:
                            handler.sendEmptyMessage(0x001);
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
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position, int type) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void initIntentReward(Class<?> activity, int live_id) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("live_id", live_id);
        startActivity(intent);
    }

    @OnClick({R.id.rl_cement_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_cement_return:
                finish();
                break;
            default:
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
     * 是否登录
     *
     * @param m
     */
    public void onEventMainThread(MainLoginFind m) {
        initIntent(LoginActivity.class);
    }
}

