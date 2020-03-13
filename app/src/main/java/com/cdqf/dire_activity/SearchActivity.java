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
import com.cdqf.dire_adapter.SearchAdapter;
import com.cdqf.dire_class.Venues;
import com.cdqf.dire_dilog.WhyDilogFragment;
import com.cdqf.dire_find.SearchFind;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    private String TAG = SearchActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    //返回
    @BindView(R.id.rl_search_return)
    public RelativeLayout rlSearchReturn = null;

    //搜索内容
    @BindView(R.id.xet_search_input)
    public XEditText xetSearchInput = null;

    //搜索
    @BindView(R.id.tv_search_x)
    public TextView tvSearchx = null;

    //无搜索信息
    @BindView(R.id.ll_search_there)
    public LinearLayout llSearchThere = null;

    //集合
    @BindView(R.id.ptrl_search_pull)
    public PullToRefreshLayout ptrlSearchPull = null;

    private ListView lvSearchPull = null;

    private SearchAdapter searchAdapter = null;

    private String search = "";

    private Gson gson = new Gson();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    llSearchThere.setVisibility(View.VISIBLE);
                    ptrlSearchPull.setVisibility(View.GONE);
                    break;
                case 0x002:
                    llSearchThere.setVisibility(View.GONE);
                    ptrlSearchPull.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_search);

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
        lvSearchPull = (ListView) ptrlSearchPull.getPullableView();
    }

    private void initAdapter() {
        searchAdapter = new SearchAdapter(context);
        lvSearchPull.setAdapter(searchAdapter);
    }

    private void initListener() {
        lvSearchPull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(11, "提示", "您当前选择的会馆为是否前往", "否", "是", position);
                whyDilogFragment.show(getSupportFragmentManager(), "选择会馆");
            }
        });
        ptrlSearchPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
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
        ptrlSearchPull.setPullDownEnable(false);
        ptrlSearchPull.setPullUpEnable(false);

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position, int isRoute) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        intent.putExtra("isRoute", isRoute);
        startActivity(intent);
    }

    @OnClick({R.id.rl_search_return, R.id.tv_search_x})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_search_return:
                finish();
                break;
            //搜索
            case R.id.tv_search_x:
                search = xetSearchInput.getText().toString();
                if (search.length() <= 0) {
                    direState.initToast(context, "请输入要查询的内容", true, 0);
                    return;
                }
                Map<String, Object> params = new HashMap<String, Object>();
                //查询字样
                params.put("msg", search);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(DirectAddaress.SEARCH, true, "查找中", params, new OnHttpRequest() {
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
                                    handler.sendEmptyMessage(0x002);
                                    List<Venues> searchList = gson.fromJson(data, new TypeToken<List<Venues>>() {
                                    }.getType());
                                    direState.setSearchList(searchList);
                                    if (searchAdapter != null) {
                                        searchAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                default:
                                    handler.sendEmptyMessage(0x001);
                                    break;
                            }
                        } else {
                            switch (error_code) {
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
     * 搜索中会馆前往
     *
     * @param s
     */
    public void onEventMainThread(SearchFind s) {
        initIntent(RouteActivity.class, s.position, 1);
    }
}
