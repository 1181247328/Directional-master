package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

/**
 * 换绑定手机第一步
 * Created by liu on 2017/11/14.
 */

public class PhoneChangeActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = PhoneChangeActivity.class.getSimpleName();

    public static PhoneChangeActivity phoneChangeActivity = null;

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private HttpRequestWrap httpRequestWrap = null;

    //返回
    private RelativeLayout rlPhoneChangeReturn = null;

    //国家和地区
    private LinearLayout llPhonechangeRegion = null;

    //地区
    private TextView tvPhonechangeRegion = null;

    //手机号码
    private XEditText xetPhonechangePhone = null;

    //下一步
    private TextView tvPhonechangeNext = null;

    private String ipAddress = null;

    private int consumerId;

    private String mobile = null;

    private int type = 0;

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
        setContentView(R.layout.activity_phonechange);

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
        httpRequestWrap = new HttpRequestWrap(context);
        phoneChangeActivity = this;
    }

    private void initView() {
        rlPhoneChangeReturn = this.findViewById(R.id.rl_phonechange_return);
        llPhonechangeRegion = this.findViewById(R.id.ll_phonechange_region);
        tvPhonechangeRegion = this.findViewById(R.id.tv_phonechange_region);
        xetPhonechangePhone = this.findViewById(R.id.xet_phonechange_phone);
        tvPhonechangeNext = this.findViewById(R.id.tv_phonechange_next);
    }

    private void initAdapter() {

    }

    private void initListener() {
        rlPhoneChangeReturn.setOnClickListener(this);
        llPhonechangeRegion.setOnClickListener(this);
        tvPhonechangeNext.setOnClickListener(this);
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_phonechange_return:
                finish();
                break;
            //国家和地区
            case R.id.ll_phonechange_region:
                break;
            //下一步
            case R.id.tv_phonechange_next:
                initIntent(PhoneChangeNextActivity.class);
//                mobile = xetPhonechangePhone.getText().toString();
//                if (mobile.length() <= 0) {
//                    return;
//                }
//                //判断是否是手机号码
//                if (!direState.checkMobileNumber(mobile)) {
//                    return;
//                }
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
