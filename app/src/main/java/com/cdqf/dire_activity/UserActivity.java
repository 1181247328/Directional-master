package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.UserAdapter;
import com.cdqf.dire_dilog.CodeDilogFragment;
import com.cdqf.dire_hear.PersonalDilogFragment;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.StatusBarCompat;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import de.greenrobot.event.EventBus;

/**
 * 用户信息
 */
public class UserActivity extends BaseActivity {

    private String TAG = UserActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    /**
     * 返回
     */
    @BindView(R.id.rl_user_return)
    public RelativeLayout rlUserReturn = null;

    /**
     * 集合
     */
    @BindView(R.id.lv_user_list)
    public ListView lvUserList = null;

    private UserAdapter userAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_user);

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
    }

    private void initView() {

    }

    private void initAdapter() {
        userAdapter = new UserAdapter(context);
        lvUserList.setAdapter(userAdapter);
    }

    private void initListener() {
        lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //头像
                    case 0:
                        PersonalDilogFragment personalDilogFragment = new PersonalDilogFragment();
                        personalDilogFragment.show(getSupportFragmentManager(), "更换头像");
                        break;
                    //昵称
                    case 1:
                        initIntent(NickNameActivity.class);
                        break;
                    //姓名
                    case 2:
                        initIntent(NameActivity.class);
                        break;
                    //性别
                    case 3:
                        SinglePicker<String> picker = new SinglePicker<>(UserActivity.this, new String[]{"男", "女"});
                        picker.setCanLoop(true);//不禁用循环
                        picker.setTopBackgroundColor(0xFFEEEEEE);
                        picker.setTopHeight(50);
                        picker.setTopLineColor(0xFF33B5E5);
                        picker.setTopLineHeight(1);
                        picker.setTitleText("请选择");
                        picker.setTitleTextColor(0xFF999999);
                        picker.setTitleTextSize(12);
                        picker.setCancelTextColor(0xFF33B5E5);
                        picker.setCancelTextSize(13);
                        picker.setSubmitTextColor(0xFF33B5E5);
                        picker.setSubmitTextSize(13);
                        picker.setSelectedTextColor(0xFFEE0000);
                        picker.setUnSelectedTextColor(0xFF999999);
                        LineConfig config = new LineConfig();
                        config.setColor(0xFFEE0000);//线颜色
                        config.setAlpha(140);//线透明度
//                config.setRatio((float) (1.0 / 8.0));//线比率
                        picker.setLineConfig(config);
                        picker.setItemWidth(200);
                        picker.setBackgroundColor(0xFFE1E1E1);
                        picker.setSelectedIndex(0);
                        picker.setOnItemPickListener(new OnItemPickListener<String>() {
                            @Override
                            public void onItemPicked(int index, String item) {

                            }
                        });
                        picker.show();
                        break;
                    //电子邮箱
                    case 4:
                        initIntent(EmailActivity.class);
                        break;
                    //手机号码
                    case 5:
                        initIntent(PhoneChangeActivity.class);
                        break;
                    //二维码
                    case 6:
                        CodeDilogFragment codeDilogFragment = new CodeDilogFragment();
                        codeDilogFragment.init("11111111");
                        codeDilogFragment.show(getSupportFragmentManager(), "二维码");
                        break;
                }
            }
        });
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    /**
     * 监听事件
     *
     * @param v
     */
    @OnClick({R.id.rl_user_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_user_return:
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
