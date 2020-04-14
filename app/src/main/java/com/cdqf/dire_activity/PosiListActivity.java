package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cdqf.dire.R;
import com.cdqf.dire_class.PosiListFind;
import com.cdqf.dire_dilgadapter.PosiListAdapter;
import com.cdqf.dire_floatball.CloseFind;
import com.cdqf.dire_floatball.OpenFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.cdqf.dire_view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 景点经过列表
 */
public class PosiListActivity extends BaseActivity {

    private String TAG = PosiListActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private HttpRequestWrap httpRequestWrap = null;

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_posilist_return)
    public RelativeLayout rlPosilistReturn = null;

    //下拉刷新
    @BindView(R.id.srl_plant_pull)
    public VerticalSwipeRefreshLayout srlPlantPull = null;

    //景点列表
    @BindView(R.id.lv_posi_list)
    public ListView lvPosiList = null;

    private PosiListAdapter posiListAdapter = null;

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
        setContentView(R.layout.activity_posilist);

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
        posiListAdapter = new PosiListAdapter(context);
        lvPosiList.setAdapter(posiListAdapter);
    }

    private void initListener() {
        srlPlantPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        srlPlantPull.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        lvPosiList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    srlPlantPull.setEnabled(true);
                } else {
                    srlPlantPull.setEnabled(false);
                }
            }
        });
        lvPosiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initIntent(PositionActivity.class,position);
            }
        });
    }

    private void initBack() {
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity,int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("address", direState.getPosiListList().get(
                (direState.getPosiListList().size()-1)-position).getBle());
        startActivity(intent);
    }

    @OnClick({R.id.rl_posilist_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_posilist_return:
                eventBus.post(new OpenFind());
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        eventBus.post(new OpenFind());
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
        eventBus.post(new CloseFind());
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
     * 刷新适配器
     *
     * @param p
     */
    public void onEventMainThread(PosiListFind p) {
        if (posiListAdapter != null) {
            posiListAdapter.notifyDataSetChanged();
        }
    }
}
