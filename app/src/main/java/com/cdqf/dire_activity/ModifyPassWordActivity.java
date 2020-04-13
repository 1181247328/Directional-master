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

import com.cdqf.dire.R;
import com.cdqf.dire_find.ModifyPwdFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 修改密码
 */
public class ModifyPassWordActivity extends BaseActivity {

    private String TAG = ModifyPassWordActivity.class.getSimpleName();

    private Context context = null;

    public static ModifyPassWordActivity modifyPassWordActivity = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    //返回
    @BindView(R.id.rl_modifypassword_return)
    public RelativeLayout rlModifypasswordReturn = null;

    //旧密码
    @BindView(R.id.xet_modifypassword_old)
    public XEditText xetModifypasswordOld = null;

    //新密码
    @BindView(R.id.xet_modifypassword_phone)
    public XEditText xetModifypasswordPhone = null;

    //请确认
    @BindView(R.id.xet_modifypawwword_password)
    public XEditText xetModifypawwwordPassword = null;

    //下一步
    @BindView(R.id.tv_modifypawwword_determine)
    public TextView tvModifypawwwordDetermine = null;

    private String old;

    private String passWordOne;

    private String PassWordTwo;

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
        setContentView(R.layout.activity_modifypassword);

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
        modifyPassWordActivity = this;
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


    @OnClick({R.id.rl_modifypassword_return, R.id.tv_modifypawwword_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_modifypassword_return:
                finish();
                break;
            //确定
            case R.id.tv_modifypawwword_determine:
//                old = xetModifypasswordOld.getText().toString();
//                if (old.length() <= 0) {
//                    direState.initToast(context, "旧密码不能为空", true, 0);
//                    return;
//                }
//                if (!TextUtils.equals("", old)) {
//                    direState.initToast(context, "旧密码错误", true, 0);
//                    return;
//                }
                passWordOne = xetModifypasswordPhone.getText().toString();
                if (passWordOne.length() <= 0) {
                    direState.initToast(context, "新密码不能为空", true, 0);
                    return;
                }
                PassWordTwo = xetModifypawwwordPassword.getText().toString();
                if (PassWordTwo.length() <= 0) {
                    direState.initToast(context, "确认密码不能为空", true, 0);
                    return;
                }

                if (!TextUtils.equals(passWordOne, PassWordTwo)) {
                    direState.initToast(context, "两次输入密码不一致", true, 0);
                    return;
                }
                eventBus.post(new ModifyPwdFind(PassWordTwo));
                finish();
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
