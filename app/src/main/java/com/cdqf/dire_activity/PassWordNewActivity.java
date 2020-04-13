package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 新密码
 */
public class PassWordNewActivity extends BaseActivity {

    private String TAG = PassWordNewActivity.class.getSimpleName();

    private Context context = null;

    public static PassWordNewActivity passWordNewActivity = null;

    private DireState direState = DireState.getDireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_newpassword_return)
    public RelativeLayout rlnewpasswordReturn = null;

    //新密码
    @BindView(R.id.xet_newpassword_phone)
    public XEditText xetNewpasswordPhone = null;

    //确定
    @BindView(R.id.xet_newpawwword_password)
    public XEditText xetNewpawwwordPassword = null;

    //下一步
    @BindView(R.id.tv_newpawwword_determine)
    public TextView tvNewpawwwordDetermine = null;

    //密码
    private String pwd = null;

    //确认密码
    private String conPwd = null;

    //手机
    private String mobile = null;

    private String yzm = null;

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
        setContentView(R.layout.activity_newpassword);

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
        passWordNewActivity = this;
        ButterKnife.bind(this);
        Intent intent = getIntent();
        //手机
        mobile = intent.getStringExtra("phone");
        yzm = intent.getStringExtra("yzm");
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


    @OnClick({R.id.rl_newpassword_return, R.id.tv_newpawwword_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_newpassword_return:
                finish();
                break;
            //确定
            case R.id.tv_newpawwword_determine:
                pwd = xetNewpasswordPhone.getText().toString();
                //密码不能为空
                if (pwd.length() <= 0) {
                    direState.initToast(context, "密码不能为空", true, 0);
                    return;
                }
                conPwd = xetNewpawwwordPassword.getText().toString();
                //请确认密码
                if (conPwd.length() <= 0) {
                    direState.initToast(context, "确认密码不能为空", true, 0);
                    return;
                }
                //比较密码是否一致
                if (!TextUtils.equals(pwd, conPwd)) {
                    direState.initToast(context, "两次密码不一致", true, 0);
                    return;
                }
                Map<String, Object> params = new HashMap<String, Object>();
                //手机号
                params.put("phone", mobile);
                //密码
                params.put("fistpassword", pwd);
                //确认密码
                params.put("lastpassword", conPwd);
                //验证码
                params.put("yzm", yzm);
                Log.e(TAG, "---phone---" + mobile + "---fistpassword---" + pwd + "---lastpassword---" + conPwd + "---yzm---" + yzm);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(DirectAddaress.PASSWORD, true, "找回中", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        JSONArray data = resultJSON.getJSONArray("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    direState.initToast(context, msg, true, 0);
                                    if (ForPasswordActivity.forPasswordActivity != null) {
                                        ForPasswordActivity.forPasswordActivity.finish();
                                    }
                                    finish();
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
                });
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
