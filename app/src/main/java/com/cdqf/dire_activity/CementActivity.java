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
import com.cdqf.dire_adapter.CementAdapter;
import com.cdqf.dire_class.Cement;
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
 * 线路、发现活动和景区
 */
public class CementActivity extends BaseActivity {

    private String TAG = CementActivity.class.getSimpleName();

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

    private CementAdapter cementAdapter = null;

    private int page = 1;

    //1=线路
    private int type = 0;

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
        setContentView(R.layout.activity_cement);

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
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

    }

    private void initView() {
        lvCementPull = (ListView) ptrlCementPull.getPullableView();
    }

    private void initAdapter() {
        cementAdapter = new CementAdapter(context);
        lvCementPull.setAdapter(cementAdapter);
    }

    private void initListener() {
        lvCementPull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initIntent(CementContextActivity.class, position);
            }
        });
        ptrlCementPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            //下拉刷新
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
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
                                    aCache.put("page", page);
                                    page = 2;
                                    handler.sendEmptyMessage(0x002);
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    direState.getCementList().clear();
                                    List<Cement> cementList = gson.fromJson(data, new TypeToken<List<Cement>>() {
                                    }.getType());
                                    if (cementList.size() <= 0) {
                                        llCementThere.setVisibility(View.VISIBLE);
                                        ptrlCementPull.setVisibility(View.GONE);
                                        return;
                                    }
                                    List<Cement> cementOneList = cementList;
                                    for (int i = 0; i < cementOneList.size(); i++) {
                                        String image = cementOneList.get(i).getImg().replace("\\", "/");
                                        cementList.get(i).setImg(image);
                                    }
                                    direState.setCementList(cementList);
                                    if (cementAdapter != null) {
                                        cementAdapter.notifyDataSetChanged();
                                    }
                                    aCache.put("cement", data);
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
                                    page++;
                                    handler.sendEmptyMessage(0x002);
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    List<Cement> cementList = gson.fromJson(data, new TypeToken<List<Cement>>() {
                                    }.getType());
                                    if (cementList.size() <= 0) {
                                        llCementThere.setVisibility(View.VISIBLE);
                                        ptrlCementPull.setVisibility(View.GONE);
                                        return;
                                    }
                                    List<Cement> cementOneList = cementList;
                                    for (int i = 0; i < cementOneList.size(); i++) {
                                        String image = cementOneList.get(i).getImg().replace("\\", "/");
                                        cementList.get(i).setImg(image);
                                    }
                                    direState.getCementList().addAll(cementList);
                                    if (cementAdapter != null) {
                                        cementAdapter.notifyDataSetChanged();
                                    }
                                    String cement = gson.toJson(cementList);
                                    aCache.put("page", page);
                                    aCache.put("cement", cement);
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
        if (aCache.getAsString("cement") != null) {
            String data = aCache.getAsString("cement");
            page = (int) aCache.getAsObject("page");
            direState.getCementList().clear();
            List<Cement> cementList = gson.fromJson(data, new TypeToken<List<Cement>>() {
            }.getType());
            direState.setCementList(cementList);
            if (cementAdapter != null) {
                cementAdapter.notifyDataSetChanged();
            }
            if (cementList.size() <= 0) {
                handler.sendEmptyMessage(0x001);
            } else {
                handler.sendEmptyMessage(0x002);
            }
        }
        ptrlCementPull.setPullUpEnable(false);
        initPull();
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        //页数
        params.put("limit", page);
        //条数
        params.put("tatol", 10);
        //查询字样
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.GAME, true, "请稍候", params, new OnHttpRequest() {
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
                            aCache.put("page", page);
                            page = 2;
                            direState.getCementList().clear();
                            List<Cement> cementList = gson.fromJson(data, new TypeToken<List<Cement>>() {
                            }.getType());
                            Log.e(TAG, "---获取的数据---" + cementList.size());

                            List<Cement> cementOneList = cementList;
                            for (int i = 0; i < cementOneList.size(); i++) {
                                String image = cementOneList.get(i).getImg().replace("\\", "/");
                                cementList.get(i).setImg(image);
                            }
                            direState.setCementList(cementList);
                            if (cementAdapter != null) {
                                cementAdapter.notifyDataSetChanged();
                            }
                            aCache.put("cement", data);
                            if (cementList.size() <= 0) {
                                handler.sendEmptyMessage(0x001);
                            } else {
                                handler.sendEmptyMessage(0x002);
                            }
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
    }
}
