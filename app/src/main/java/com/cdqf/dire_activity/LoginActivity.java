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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire.wxapi.QQFind;
import com.cdqf.dire.wxapi.QQLogin;
import com.cdqf.dire.wxapi.WXFind;
import com.cdqf.dire.wxapi.WxLogin;
import com.cdqf.dire_class.User;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 登录
 * Created by liu on 2017/11/15.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = LoginActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    //返回
    @BindView(R.id.rl_login_return)
    public RelativeLayout rlLoginReturn = null;

    //注册
    @BindView(R.id.tv_login_registered)
    public TextView tvLoginRegistered = null;

    //手机号
    @BindView(R.id.xet_login_phone)
    public XEditText xetLoginPhone = null;

    //登录密码
    @BindView(R.id.xet_loging_password)
    public XEditText xetLogingPassword = null;

    //登录
    @BindView(R.id.tv_login)
    public TextView tvLogin = null;

    //QQ
    @BindView(R.id.rl_login_qq)
    public RelativeLayout rlLoginQq = null;

    //微信
    @BindView(R.id.rl_login_wetch)
    public RelativeLayout rlLoginWetch = null;

    //隐私
    @BindView(R.id.cb_login_ment)
    public CheckBox cbLoginMent = null;

    @BindView(R.id.tv_login_agreement)
    public TextView tvLoginAgreement = null;

    //账号
    private String phone = null;

    private boolean isCheck = false;

    //密码
    private String password = null;

    private Gson gson = new Gson();

    /***忘记密码***/
    @BindView(R.id.tv_login_passwordno)
    public TextView tvLoginPasswordno = null;

    private ACache aCache = null;

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
        setContentView(R.layout.activity_login);

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
        aCache = ACache.get(context);
    }

    private void initView() {


    }

    private void initAdapter() {

    }

    private void initListener() {
        cbLoginMent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
            }
        });
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_login_return, R.id.tv_login_registered, R.id.tv_login,
            R.id.rl_login_qq, R.id.rl_login_wetch, R.id.tv_login_passwordno,
            R.id.tv_login_agreement})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_login_return:
                finish();
                break;
            //注册
            case R.id.tv_login_registered:
                initIntent(RegisteredOneActivity.class);
                break;
            //登录
            case R.id.tv_login:
                if (!isCheck) {
                    direState.initToast(context, "请勾选用户协议", true, 0);
                    return;
                }
                phone = xetLoginPhone.getText().toString();
                if (phone.length() <= 0) {
                    direState.initToast(context, "请输入账户", true, 0);
                    return;
                }
                password = xetLogingPassword.getText().toString();
                if (password.length() <= 0) {
                    direState.initToast(context, "请输入密码", true, 0);
                    return;
                }
                Map<String, Object> params = new HashMap<String, Object>();
                //手机号
                params.put("phone", phone);
                //？？？
                params.put("password", password);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(DirectAddaress.LOGIN, true, "登录中", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    String data = resultJSON.getString("Data");
                                    User user = gson.fromJson(data, User.class);
                                    user.setQQ(false);
                                    user.setLogin(0);
                                    direState.setUser(user);
                                    direState.setLogin(true);
                                    DirectPreferences.setUser(context, user);
                                    aCache.put("user", gson.toJson(user), ACache.TIME_DAY * 7);
                                    initIntent(MyActivity.class);
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
            //QQ登录
            case R.id.rl_login_qq:
                if (!isCheck) {
                    direState.initToast(context, "请勾选用户协议", true, 0);
                    return;
                }
                QQLogin.qqLogin(context, LoginActivity.this);
                break;
            //微信登录
            case R.id.rl_login_wetch:
                if (!isCheck) {
                    direState.initToast(context, "请勾选用户协议", true, 0);
                    return;
                }
                WxLogin.loginWx(context);
                break;
            //忘记密码
            case R.id.tv_login_passwordno:
                initIntent(ForPasswordActivity.class);
                break;
            //
            case R.id.tv_login_agreement:
                initIntent(MentActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, QQLogin.loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
     * QQ登录
     *
     * @param l
     */
    public void onEventMainThread(QQFind l) {
        Log.e(TAG, "---id---" + l.openID + "---nickName---" + l.nickName + "---头像---" + l.headImage);
        Map<String, Object> params = new HashMap<String, Object>();
        //id
        params.put("openid", l.openID);
        //名称
        params.put("nickname", l.nickName);
        //头像
        params.put("img", l.headImage);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.QQ_LOGIN, true, "登录中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---QQ登录---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            String data = resultJSON.getString("Data");
                            User user = gson.fromJson(data, User.class);
                            user.setQQ(true);
                            user.setLogin(1);
                            direState.setUser(user);
                            direState.setLogin(true);
                            DirectPreferences.setUser(context, user);
                            aCache.put("user", gson.toJson(user), ACache.TIME_DAY * 7);
                            initIntent(MyActivity.class);
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
                Log.e(TAG, "---QQ登录onOkHttpError---" + error);
                direState.initToast(context, error, true, 0);
            }
        });
    }

    /**
     * 微信登录
     *
     * @param l
     */
    public void onEventMainThread(WXFind l) {
        Log.e(TAG, "---微信登录---id---" + l.openid + "---nickName---" + l.nickname + "---头像---" + l.headimgurl);
        Map<String, Object> params = new HashMap<String, Object>();
        //id
        params.put("openid", l.openid);
        //名称
        params.put("nickname", l.nickname);
        //头像
        params.put("img", l.headimgurl);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.WX_LOGIN, true, "登录中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---微信登录---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            String data = resultJSON.getString("Data");
                            User user = gson.fromJson(data, User.class);
                            user.setQQ(true);
                            user.setLogin(1);
                            direState.setUser(user);
                            direState.setLogin(true);
                            DirectPreferences.setUser(context, user);
                            aCache.put("user", gson.toJson(user), ACache.TIME_DAY * 7);
                            initIntent(MyActivity.class);
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
                Log.e(TAG, "---微信登录onOkHttpError---" + error);
                direState.initToast(context, error, true, 0);
            }
        });
    }
}
