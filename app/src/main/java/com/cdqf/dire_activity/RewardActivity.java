package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire.wxapi.QQLogin;
import com.cdqf.dire_dilog.ShareDilogFragment;
import com.cdqf.dire_find.ShareQQFind;
import com.cdqf.dire_find.ShareZnoeFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 完成奖励
 */
public class RewardActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = RewardActivity.class.getSimpleName();

    public static PhoneChangeActivity phoneChangeActivity = null;

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private HttpRequestWrap httpRequestWrap = null;

    //返回
    @BindView(R.id.rl_reward_return)
    public RelativeLayout rlRewardReturn = null;

    //分享
    @BindView(R.id.rl_reward_share)
    public RelativeLayout rlRewardShare = null;

    //标题
    @BindView(R.id.tv_reward_title)
    public TextView tvRewardTitle = null;

    //队长
    @BindView(R.id.tv_reward_captain)
    public TextView tvRewardCaptain = null;

    //队员
    @BindView(R.id.tv_reward_players)
    public TextView tvRewardPlayers = null;

    //内容
    @BindView(R.id.tv_reward_context)
    public TextView tvRewardContext = null;

    //查看定向过程
    @BindView(R.id.tv_reward_directional)
    public TextView tvRewardDirectional = null;

    @BindView(R.id.ll_reward_context_one)
    public RelativeLayout llRewardContextOne = null;

    private int live_id = 0;

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
        setContentView(R.layout.activity_reward);

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
        Intent intent = getIntent();
        live_id = intent.getIntExtra("live_id", 0);
        DirectPreferences.clearUserLine(context);
        DirectPreferences.clearUserLive(context);
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        initPull();
    }

    private void initPull() {
        Map<String, String> params = new HashMap<String, String>();

        //用户id
        params.put("user_id", String.valueOf(direState.getUser().getId()));

        //用户线路的id
        params.put("live_id", String.valueOf(live_id));

        Log.e(TAG, "---user_id---" + String.valueOf(direState.getUser().getId()) + "---live_id---" + String.valueOf(live_id));
        OkHttpUtils
                .post()
                .url(DirectAddaress.REWARD)
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
                        JSONObject dataJson = resultJSON.getJSONObject("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    //标题
                                    tvRewardTitle.setText(dataJson.getString("title"));
                                    //队长
//                                    tvRewardCaptain.setText(dataJson.getString("captain"));
                                    tvRewardCaptain.setText(direState.getUser().getNickname());
                                    //内容
                                    tvRewardContext.setText("       " + dataJson.getString("comment"));
                                    break;
                                default:
                                    direState.initToast(context, msg, true, 0);
                                    break;
                            }
                        } else {
                            switch (error_code) {
                                case 301:
                                    //标题
                                    tvRewardTitle.setText(dataJson.getString("title"));
                                    //队长
//                                    tvRewardCaptain.setText(dataJson.getString("captain"));
                                    tvRewardCaptain.setText(direState.getUser().getNickname());
                                    //内容
                                    tvRewardContext.setText("       " + dataJson.getString("comment"));
                                    break;
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

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_reward_return, R.id.rl_reward_share, R.id.tv_reward_directional})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_reward_return:
                finish();
                break;
            //分享
            case R.id.rl_reward_share:
                String file = direState.viewSaveToImage(llRewardContextOne);
                Log.e(TAG, "---组件名保存的图片---" + file);
                ShareDilogFragment shareDilogFragment = new ShareDilogFragment();
                shareDilogFragment.setFile(file);
                shareDilogFragment.show(getSupportFragmentManager(), "分享");
                break;
            case R.id.tv_reward_directional:
                initIntent(MedalActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, QQLogin.shareImageiUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
     * QQ好友
     *
     * @param qq
     */
    public void onEventMainThread(ShareQQFind qq) {
        QQLogin.qqShare(RewardActivity.this, qq.fileImage);
    }

    /**
     * QQ空间
     *
     * @param znoe
     */
    public void onEventMainThread(ShareZnoeFind znoe) {
        String title = tvRewardTitle.getText().toString();
        String context = tvRewardContext.getText().toString();
        QQLogin.qqShareZone(RewardActivity.this, title, context, znoe.fileImage);
    }
}