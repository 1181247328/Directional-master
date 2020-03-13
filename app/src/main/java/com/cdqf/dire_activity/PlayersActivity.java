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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.PlayersAdapter;
import com.cdqf.dire_dilog.WhyDilogFragment;
import com.cdqf.dire_find.PlayersAddFind;
import com.cdqf.dire_find.PlayersDeleteTwoFind;
import com.cdqf.dire_find.PlayersDeletelFind;
import com.cdqf.dire_find.TeamPullFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xw.repo.XEditText;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 添加队员
 */
public class PlayersActivity extends BaseActivity {

    private String TAG = PlayersActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();
    //返回
    @BindView(R.id.rl_players_return)
    public RelativeLayout rlPlayersReturn = null;

    //队员名称
    @BindView(R.id.xet_players_input)
    public XEditText xetPlayersInput = null;

    //添加队员
    @BindView(R.id.tv_players_add)
    public TextView tvPlayersAdd = null;

    @BindView(R.id.ll_players_there)
    public LinearLayout llPlayersThere = null;

    @BindView(R.id.lv_players_list)
    public ListView lvPlayersList = null;

    private PlayersAdapter playersAdapter = null;

    private String playersName = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvPlayersList.setVisibility(View.VISIBLE);
                    llPlayersThere.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvPlayersList.setVisibility(View.GONE);
                    llPlayersThere.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_players);

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
        playersAdapter = new PlayersAdapter(context);
        lvPlayersList.setAdapter(playersAdapter);
    }

    private void initListener() {

    }

    private void initBack() {
        for (int i = 0; i < direState.getTeam().getUsername1().size(); i++) {
            direState.getPlayersList().add(direState.getTeam().getUsername1().get(i));
        }
        if (direState.getTeam().getUsername1().size() > 0) {
            handler.sendEmptyMessage(0x001);
        }
        playersAdapter.notifyDataSetChanged();
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_players_return, R.id.tv_players_add})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_players_return:
                eventBus.post(new TeamPullFind());
                finish();
                break;
            //添加队员
            case R.id.tv_players_add:
                playersName = xetPlayersInput.getText().toString();

                //判断输入用户是否为空
                if (playersName.length() <= 0) {
                    direState.initToast(context, "添加队员不可为空", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(9, "提示", "您当前正在添加队员" + playersName + ",是否添加其为自己队员", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "添加队员");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        eventBus.post(new TeamPullFind());
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
     * 是否添加队员
     *
     * @param l
     */
    public void onEventMainThread(PlayersAddFind l) {

        Map<String, String> params = new HashMap<String, String>();

        //用户id
        params.put("user_id", String.valueOf(direState.getUser().getId()));

        //用户添加队员的名称
        params.put("name", playersName);

        OkHttpUtils
                .post()
                .url(DirectAddaress.PLAYERS_ADD)
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
                        JSONObject dataJSON = resultJSON.getJSONObject("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    xetPlayersInput.setText("");
                                    handler.sendEmptyMessage(0x001);
                                    direState.initToast(context, msg, true, 0);
                                    String usernamel = dataJSON.getString("username1");
                                    Log.e(TAG, "---队员信息---" + usernamel);
                                    List<String> playersList = gson.fromJson(usernamel, new TypeToken<List<String>>() {
                                    }.getType());
                                    direState.setPlayersList(playersList);
                                    if (playersAdapter != null) {
                                        playersAdapter.notifyDataSetChanged();
                                    } else {
                                        //TODO
                                    }
                                    break;
                                default:
                                    handler.sendEmptyMessage(0x002);
                                    direState.initToast(context, msg, true, 0);
                                    break;
                            }
                        } else {
                            switch (error_code) {
                                default:
                                    handler.sendEmptyMessage(0x002);
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
    }

    /**
     * 删除队员
     *
     * @param p
     */
    public void onEventMainThread(PlayersDeletelFind p) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(10, "提示", "您当前正在删除队员" + direState.getPlayersList().get(p.position) + ",是否删除", "否", "是", p.position);
        whyDilogFragment.show(getSupportFragmentManager(), "添加队员");
    }

    /**
     * 删除队员确定操作
     *
     * @param p
     */
    public void onEventMainThread(PlayersDeleteTwoFind p) {

        Map<String, String> params = new HashMap<String, String>();
        //用户id
        params.put("user_id", String.valueOf(direState.getUser().getId()));

        //用户删除队员的名称
        String name = direState.getPlayersList().get(p.position);
        Log.e(TAG, "---删除的队员---" + name);
        params.put("name", name);

        OkHttpUtils
                .post()
                .url(DirectAddaress.PLAYERS_DELETE)
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
                        String username1 = JSONObject.parseObject(resultJSON.getString("Data")).getString("username1");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    direState.initToast(context, msg, true, 0);
                                    List<String> playersList = gson.fromJson(username1, new TypeToken<List<String>>() {
                                    }.getType());
                                    direState.setPlayersList(playersList);
                                    if(playersAdapter!=null){
                                        playersAdapter.notifyDataSetChanged();
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
    }
}
