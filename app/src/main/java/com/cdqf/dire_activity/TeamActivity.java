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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.TeamAdapter;
import com.cdqf.dire_class.Team;
import com.cdqf.dire_find.TeamPullFind;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我的队伍
 */
public class TeamActivity extends BaseActivity {

    private String TAG = TeamActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    @BindView(R.id.srl_team_refresh)
    public SwipeRefreshLayout srlTeamRefresh = null;

    //返回
    @BindView(R.id.rl_team_return)
    public RelativeLayout rlTeamReturn = null;

    @BindView(R.id.ll_team_there)
    public LinearLayout llTeamThere = null;

    @BindView(R.id.lv_team_list)
    public ListView lvTeamList = null;

    //添加
    @BindView(R.id.tv_team_add)
    public TextView tvTeamAdd = null;

    private TeamAdapter teamAdapter = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvTeamList.setVisibility(View.VISIBLE);
                    llTeamThere.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvTeamList.setVisibility(View.GONE);
                    llTeamThere.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_team);

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
        lvTeamList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (lvTeamList != null && lvTeamList.getChildCount() > 0) {
                    boolean firstItemVisible = lvTeamList.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = lvTeamList.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                srlTeamRefresh.setEnabled(enable);
            }
        });
        srlTeamRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlTeamRefresh.setRefreshing(true);
                initPull(false);
            }
        });
    }

    private void initAdapter() {
        teamAdapter = new TeamAdapter(context);
        lvTeamList.setAdapter(teamAdapter);
    }

    private void initListener() {

    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isPull) {
        Map<String, Object> params = new HashMap<String, Object>();
        //用户id
        int user_id = direState.getUser().getId();
        Log.e(TAG, "---user_id---" + user_id);
        params.put("user_id", user_id);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.DATA, isPull, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                String data = resultJSON.getString("Data");
                if (srlTeamRefresh != null) {
                    srlTeamRefresh.setRefreshing(false);
                }
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            Log.e(TAG, "---我的队伍---" + data);
                            handler.sendEmptyMessage(0x001);
                            Team team = gson.fromJson(data, Team.class);
                            direState.setTeam(team);
//                            List<Team> teamList = gson.fromJson(data, new TypeToken<List<Team>>() {
//                            }.getType());
//                            direState.setTeamList(teamList);
                            if (teamAdapter != null) {
                                teamAdapter.notifyDataSetChanged();
                            }
                            break;
                        default:
                            handler.sendEmptyMessage(0x002);
                            break;
                    }
                } else {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            handler.sendEmptyMessage(0x002);
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

    @OnClick({R.id.rl_team_return, R.id.tv_team_add})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_team_return:
                finish();
                break;
            //添加
            case R.id.tv_team_add:
                initIntent(PlayersActivity.class);
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

    public void onEventMainThread(TeamPullFind t) {
        initPull(true);
    }
}