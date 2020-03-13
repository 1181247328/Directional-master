package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_class.User;
import com.cdqf.dire_dilog.WhyDilogFragment;
import com.cdqf.dire_find.ModifyPwdFind;
import com.cdqf.dire_find.MyEditFind;
import com.cdqf.dire_find.PersonalReturnFind;
import com.cdqf.dire_find.PersonalSaveFind;
import com.cdqf.dire_hear.FileUtil;
import com.cdqf.dire_hear.HearDilogFragment;
import com.cdqf.dire_hear.ShelvesImageFind;
import com.cdqf.dire_okhttp.OKHttpStringCallback;
import com.cdqf.dire_okhttp.OnOkHttpResponseHandler;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_state.Telephone;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 个人信息
 */
public class PersonalActivity extends BaseActivity {

    private String TAG = PersonalActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    //返回
    @BindView(R.id.rl_personal_return)
    public RelativeLayout rlPersonalReturn = null;

    //保存
    @BindView(R.id.tv_personal_set)
    public TextView tvPersonalSet = null;

    //头像
    @BindView(R.id.rcrl_personal_hear)
    public RCRelativeLayout rcrlPersonalHear = null;

    //头像
    @BindView(R.id.iv_personal_hear)
    public ImageView ivPersonalHear = null;

    //昵称
    @BindView(R.id.xet_personal_nickname)
    public XEditText xetPersonalNickname = null;

    //修改密码
    @BindView(R.id.tv_personal_password)
    public TextView tvPersonalPassword = null;

    //地址
    @BindView(R.id.tv_personal_address)
    public TextView tvPersonalAddress = null;

    //登录时间
    @BindView(R.id.tv_personal_login)
    public TextView tvPersonalLogin = null;

    private Gson gson = new Gson();

    private String pwd;

    private boolean isHear = false;

    private boolean isPwd = false;

    private ACache aCache = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    tvPersonalSet.setVisibility(View.VISIBLE);
                    break;
                case 0x002:
                    tvPersonalSet.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_personal);

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
        xetPersonalNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nickTwo = xetPersonalNickname.getText().toString();
                String nickOne = direState.getUser().getNickname();
                if (!TextUtils.equals(nickOne, nickTwo)) {
                    handler.sendEmptyMessage(0x001);
                }
            }
        });
    }


    private void initBack() {
        String login = "";
        if (direState.getUser().getLogin() == 0) {
            login = DirectAddaress.ADDRESS + direState.getUser().getImg();
        } else if (direState.getUser().getLogin() == 1) {
            login = direState.getUser().getImg();
        } else {
            //TODO
        }
        imageLoader.displayImage(login, ivPersonalHear, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        xetPersonalNickname.setText(direState.getUser().getNickname());
        tvPersonalPassword.setText("......");
        String address = direState.getCity();
        if (Telephone.isChinaMobile(context)) {
            address += "-移动";
        } else if (Telephone.isChinaUnicom(context)) {
            address += "-联通";
        } else if (Telephone.isChinaTelecom(context)) {
            address += "-电信";
        } else {
            Log.e(TAG, "---电话归属无---");
            //TODO
        }
        tvPersonalAddress.setText(address);
        tvPersonalLogin.setText(direState.getUser().getCreate_time());
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_personal_return, R.id.tv_personal_set, R.id.rcrl_personal_hear, R.id.tv_personal_password})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_personal_return:
                WhyDilogFragment whyDilogOneFragment = new WhyDilogFragment();
                whyDilogOneFragment.setInit(2, "提示", "您正在进行保存操作,如果现在退出,将清空所有数据,是否退出", "取消", "退出");
                whyDilogOneFragment.show(getSupportFragmentManager(), "返回确定");
                break;
            //保存
            case R.id.tv_personal_set:
                WhyDilogFragment whyDilogTwoFragment = new WhyDilogFragment();
                whyDilogTwoFragment.setInit(3, "提示", "您正在进行保存操作,保存完毕后将退出当前页", "取消", "保存");
                whyDilogTwoFragment.show(getSupportFragmentManager(), "保存信息");
                break;
            //头像
            case R.id.rcrl_personal_hear:
                HearDilogFragment hearDilogFragment = new HearDilogFragment();
                hearDilogFragment.show(getSupportFragmentManager(), "更换头像");
                break;
            //修改密码
            case R.id.tv_personal_password:
                initIntent(ModifyPassWordActivity.class);
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
     * 退出操作
     *
     * @param p
     */
    public void onEventMainThread(PersonalReturnFind p) {
        finish();
    }

    /**
     * 头像
     *
     * @param s
     */
    public void onEventMainThread(ShelvesImageFind s) {
        isHear = true;
        ivPersonalHear.setImageBitmap(s.bitmap);
        handler.sendEmptyMessage(0x001);
    }

    /**
     * 修改密码
     *
     * @param m
     */
    public void onEventMainThread(ModifyPwdFind m) {
        isPwd = true;
        pwd = m.pwd;
        handler.sendEmptyMessage(0x002);
    }


    /**
     * 保存
     *
     * @param p
     */
    public void onEventMainThread(PersonalSaveFind p) {
        Map<String, String> params = new HashMap<String, String>();
        //用户id
        int id = direState.getUser().getId();
        params.put("id", String.valueOf(id));
        //昵称
        String nickNameOne = direState.getUser().getNickname();
        String nickNameTwo = xetPersonalNickname.getText().toString();
        if (TextUtils.equals(nickNameOne, nickNameTwo)) {
            params.put("nickname", nickNameOne);
        } else {
            params.put("nickname", nickNameTwo);
        }
        //修改密码
        if (isPwd) {
            params.put("pwd", pwd);
        } else {
            params.put("pwd", "");
        }
        Log.e(TAG, "---是否上传图片---" + isHear);
        if (isHear) {
            OkHttpUtils
                    .post()
                    .url(DirectAddaress.EDITUSER)
                    .addFile("img", "head.png", new File(FileUtil.IMG_CACHE4))
                    .params(params)
                    .build()
                    .execute(new OKHttpStringCallback(context, true, "保存中", new OnOkHttpResponseHandler() {
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
                                        direState.initToast(context, msg, true, 0);
                                        User user = gson.fromJson(data, User.class);
                                        direState.setUser(user);
                                        DirectPreferences.setUser(context, user);
                                        aCache.put("user", gson.toJson(user), ACache.TIME_DAY * 7);
                                        eventBus.post(new MyEditFind());
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
                    }));
        } else {
            OkHttpUtils
                    .post()
                    .url(DirectAddaress.EDITUSER)
                    .params(params)
                    .build()
                    .execute(new OKHttpStringCallback(context, true, "保存中", new OnOkHttpResponseHandler() {
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
                                        direState.initToast(context, msg, true, 0);
                                        User user = gson.fromJson(data, User.class);
                                        direState.setUser(user);
                                        DirectPreferences.setUser(context, user);
                                        aCache.put("user", gson.toJson(user), ACache.TIME_DAY * 7);
                                        eventBus.post(new MyEditFind());
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
                    }));
        }
    }
}