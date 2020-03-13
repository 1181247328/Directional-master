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
import com.cdqf.dire_adapter.LineAdapter;
import com.cdqf.dire_class.UserLive;
import com.cdqf.dire_find.LineCompleteFind;
import com.cdqf.dire_find.LinePositionFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我的线路
 */
public class LineActivity extends BaseActivity {

    private String TAG = LineActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_line_return)
    public RelativeLayout rlLineReturn = null;

    //无线路
    @BindView(R.id.ll_line_there)
    public LinearLayout llLineThere = null;

    //集合
    @BindView(R.id.ptrl_line_pull)
    public PullToRefreshLayout ptrlLinePull = null;

    private ListView lvLinePull = null;

    private LineAdapter lineAdapter = null;

    private int userLineId = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    ptrlLinePull.setVisibility(View.VISIBLE);
                    llLineThere.setVisibility(View.GONE);
                    break;
                case 0x002:
                    ptrlLinePull.setVisibility(View.GONE);
                    llLineThere.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_line);

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
        lvLinePull = (ListView) ptrlLinePull.getPullableView();
    }

    private void initAdapter() {
        lineAdapter = new LineAdapter(context);
        lvLinePull.setAdapter(lineAdapter);
    }

    private void initListener() {
        lvLinePull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (direState.getUserLiveList().get(position).getStatus() == 0 || direState.getUserLiveList().get(position).getStatus() == 1 || direState.getUserLiveList().get(position).getStatus() == 2) {
//                    WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
//                    whyDilogFragment.setInit(6, "提示", "是否选择路线" + direState.getUserLiveList().get(position).getTitle(), "否", "是", position);
//                    whyDilogFragment.show(getSupportFragmentManager(), "选择路线");
//                } else if (direState.getUserLiveList().get(position).getStatus() == 3) {
//                    WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
//                    whyDilogFragment.setInit(7, "提示", "线路" + direState.getUserLiveList().get(position).getTitle() + "已经完成,是否查看奖章", "否", "是", position);
//                    whyDilogFragment.show(getSupportFragmentManager(), "线路完成");
//                }
            }
        });
        ptrlLinePull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            //下拉刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            //上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
    }

    private void initBack() {
        ptrlLinePull.setPullDownEnable(false);
        ptrlLinePull.setPullUpEnable(false);
        initPull();
    }

    private void initPull() {
        Map<String, String> params = new HashMap<String, String>();
        //用户id
        int id = direState.getUser().getId();
        params.put("user_id", String.valueOf(id));
        OkHttpUtils
                .post()
                .url(DirectAddaress.USERLIVE)
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
                        String data = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    handler.sendEmptyMessage(0x001);
                                    direState.getUserLiveList().clear();
                                    List<UserLive> userLiveList = gson.fromJson(data, new TypeToken<List<UserLive>>() {
                                    }.getType());
                                    direState.setUserLiveList(userLiveList);
                                    if (lineAdapter != null) {
                                        lineAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                default:
                                    handler.sendEmptyMessage(0x002);
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

    private void initIntent(Class<?> activity, int live_id) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("live_id", live_id);
        startActivity(intent);
    }

    @OnClick({R.id.rl_line_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_line_return:
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
     * 选择路线
     *
     * @param l
     */
    public void onEventMainThread(final LinePositionFind l) {
//        DirectPreferences.clearUserLive(context);
//        direState.getLiveList().clear();
//        //线路跟踪之选择路线
//        direState.getUserLine().setRoute(true);
//        //将选择的路线全部写进线路管理集合之中
//        direState.setLiveList(direState.getUserLiveList().get(l.position).getNodes());
//        /****线路的跟踪****/
//        //线路跟踪之用户id
//        direState.getUserLine().setId(direState.getUser().getId());
//        //线路跟踪之当前线路id
//        direState.getUserLine().setLineId(direState.getUserLiveList().get(l.position).getLive_id());
//        //线路跟踪之当前线路中第一个点的id或者用户所在的节点的id
//        int ids = direState.getUserLiveList().get(l.position).getNode_id();
//        direState.getUserLine().setLinePositionId(ids);
//
//        //线路跟踪之保存当前节点在哪个位置了
//        if (direState.getUserLiveList().get(l.position).getStatus() == 3) {
//            direState.getUserLine().setCustoms(true);
//            direState.getUserLine().setAgree(true);
//        } else {
//            for (int i = 0; i < direState.getLiveList().size(); i++) {
//                if (ids == direState.getLiveList().get(i).getId()) {
//                    direState.getUserLine().setNumber(i + 1);
//                    break;
//                }
//            }
//
//            if (direState.getUserLine().getNumber() == 1) {
//                direState.getUserLine().setSign(false);
//                direState.getUserLine().setAgree(false);
//            } else {
//                direState.getUserLine().setSign(true);
//                direState.getUserLine().setAgree(true);
//            }
//        }
////        for (int i = 0; i < direState.getLiveList().size(); i++) {
////            if (1 == direState.getLiveList().get(i).getStatus()) {
////                direState.getUserLine().setNumber(i + 1);
////                break;
////            } else {
////                //说明这条线路已经完成了
////                if ((i + 1) == direState.getLiveList().size()) {
////                    direState.getUserLine().setCustoms(true);
////                }
////            }
////        }
//        /***线路状态保存***/
//        DirectPreferences.setUserLive(context, direState.getUserLine());
//        Log.e(TAG, "---线路获取---" + gson.toJson(direState.getUserLine()));
//        DirectPreferences.setUserLine(context, direState.getLiveList());
//        if (MyActivity.myActivity != null) {
//            MyActivity.myActivity.finish();
//        }
//        direState.initToast(context, "请点击线路开始活动", true, 0);
//        finish();
    }

    /**
     * 线路完成
     *
     * @param l
     */
    public void onEventMainThread(final LineCompleteFind l) {
//        initIntent(RewardActivity.class, direState.getUserLiveList().get(l.position).getLive_id());
//        finish();
    }
}
