package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.MedalAdapter;
import com.cdqf.dire_class.Medal;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_view.LineGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 勋章
 */
public class MedalActivity extends BaseActivity {

    private String TAG = MedalActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private ACache aCache = null;

    @BindView(R.id.sr_medal_pull)
    public SwipeRefreshLayout srMedalPull = null;

    //返回
    @BindView(R.id.rl_medal_return)
    public RelativeLayout rlMedalReturn = null;

    @BindView(R.id.ll_medal_there)
    public LinearLayout llMedalThere = null;

    //集合
    @BindView(R.id.lgv_medal_list)
    public LineGridView lgvMedalList = null;

    private MedalAdapter medalAdapter = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lgvMedalList.setVisibility(View.VISIBLE);
                    llMedalThere.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lgvMedalList.setVisibility(View.GONE);
                    llMedalThere.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_medal);

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
    }

    private void initView() {

    }

    private void initAdapter() {
        medalAdapter = new MedalAdapter(context);
        lgvMedalList.setAdapter(medalAdapter);
    }

    private void initListener() {
        srMedalPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        lgvMedalList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                srMedalPull.setEnabled(firstVisibleItem == 0);
            }
        });
    }

    private void initBack() {
        initPull();
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", direState.getUser().getId());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.MEDAL, true, "请稍候", params, new OnHttpRequest() {
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
                            direState.getPeriodList().clear();
                            List<Medal> medalList = gson.fromJson(data, new TypeToken<List<Medal>>() {
                            }.getType());
                            direState.setMedalList(medalList);
                            if (medalAdapter != null) {
                                medalAdapter.notifyDataSetChanged();
                            }
                            break;
                        case 20042:
                            handler.sendEmptyMessage(0x002);
                            direState.initToast(context, msg, true, 0);
                            break;
                        default:
                            handler.sendEmptyMessage(0x002);
                            break;
                    }
                } else {
                    switch (error_code) {
                        case 20042:
                            handler.sendEmptyMessage(0x002);
                            direState.initToast(context, msg, true, 0);
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

    @OnClick({R.id.rl_medal_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_medal_return:
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
    }
}
