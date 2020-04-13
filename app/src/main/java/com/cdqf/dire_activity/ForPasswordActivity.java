package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_state.WIFIGpRs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 忘记密码
 */
public class ForPasswordActivity extends BaseActivity {

    private String TAG = ForPasswordActivity.class.getSimpleName();

    private Context context = null;

    public static ForPasswordActivity forPasswordActivity = null;

    private DireState direState = DireState.getDireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_forpassword_return)
    public RelativeLayout rlForpasswordReturn = null;

    //手机号
    @BindView(R.id.xet_forpassword_phone)
    public XEditText xetForpasswordPhone = null;

    //验证码
    @BindView(R.id.tv_forpassword_obtain)
    public TextView tvForpasswordObtain = null;

    //输入验证码
    @BindView(R.id.xet_forpassword_code)
    public XEditText xetForpasswordCode = null;

    //下一步
    @BindView(R.id.tv_forpassword_next)
    public TextView tvForpasswordNext = null;

    private Timer timer = null;

    private int time = 60;

    private String mobile = "";

    private String mobilePhone = null;

    private String obtainIp = null;

    private String code = null;

    private String codeOne = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time--;
            if (time == 0) {
                time = 60;
                tvForpasswordObtain.setText("获取验证码");
                timer.cancel();
            } else {
                tvForpasswordObtain.setText("重新发送(" + time + ")s");
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
        setContentView(R.layout.activity_forpassword);

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
        forPasswordActivity = this;
        ButterKnife.bind(this);
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, String phone, String code) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("phone", phone);
        intent.putExtra("yzm", code);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.rl_forpassword_return, R.id.tv_forpassword_obtain, R.id.tv_forpassword_next})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_forpassword_return:
                finish();
                break;
            //验证码
            case R.id.tv_forpassword_obtain:
                mobile = xetForpasswordPhone.getText().toString();
                mobilePhone = mobile;
                if (mobile.length() <= 0) {
                    direState.initToast(context, "请输入手机号", true, 0);
                    return;
                }
                //判断是否是手机号码
                if (!direState.checkMobileNumber(mobile)) {
                    direState.initToast(context, "请输入正确的手机号", true, 0);
                    return;
                }
                //判断手机是否有网
                if (!WIFIGpRs.isNetworkConnected(context)) {
                    direState.initToast(context, "暂无网络,无法注册", true, 0);
                }
                //判断是GPRS还是wifi
                if (WIFIGpRs.isWifi(context)) {
                    obtainIp = WIFIGpRs.getWiFiIp(context);
                } else {
                    obtainIp = WIFIGpRs.getGpRsIp();
                }
                Map<String, Object> params = new HashMap<String, Object>();
                //手机号
                params.put("mobile", mobile);
                //？？？
                params.put("mbbc", "r_zcdl");
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(DirectAddaress.CODE, true, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        JSONObject data = resultJSON.getJSONObject("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    direState.initToast(context, msg, true, 0);
                                    codeOne = data.getString("code");
                                    tvForpasswordObtain.setText("重新发送(" + timer + ")s");
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.sendEmptyMessage(0x001);
                                        }
                                    }, 1, 1000);
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
                });
                break;
            //下一步
            case R.id.tv_forpassword_next:
                String code = xetForpasswordCode.getText().toString();
                if (mobile.length() <= 0) {
                    direState.initToast(context, "请填写手机号", true, 0);
                    return;
                }
                if (code.length() <= 0) {
                    direState.initToast(context, "请填写验证码", true, 0);
                    return;
                }
                if (!TextUtils.equals(mobile, mobilePhone)) {
                    direState.initToast(context, "两次的手机号不一致", true, 0);
                    return;
                }
                if (!TextUtils.equals(code, codeOne)) {
                    direState.initToast(context, "请正确的验证码", true, 0);
                    return;
                }
                initIntent(PassWordNewActivity.class, mobilePhone, code);
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
        if (timer != null){
            timer.cancel();
        }
    }

}
