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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.SignAdapter;
import com.cdqf.dire_class.Live;
import com.cdqf.dire_dilog.SignDilogFragment;
import com.cdqf.dire_find.BleFind;
import com.cdqf.dire_find.SignFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 签到
 */
public class SignActivity extends BaseActivity {

    private String TAG = SignActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    //返回
    @BindView(R.id.rl_cardone_return)
    public RelativeLayout rlCardoneReturn = null;

    //开始活动
    @BindView(R.id.tv_cardone_start)
    public TextView tvCardoneStart = null;

    //图片
    @BindView(R.id.iv_sign_address)
    public ImageView ivSignAddress = null;

    //开始地点
    @BindView(R.id.tv_sign_address)
    public TextView tvSignAddress = null;

    //开始时间
    @BindView(R.id.tv_sign_time)
    public TextView tvSignTime = null;

    //活动物品
    @BindView(R.id.mgv_sign_list)
    public MyGridView mgvSignList = null;

    private SignAdapter signAdapter = null;

    private String device = "";

    //是否签到
    private boolean isSign = false;

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
        setContentView(R.layout.activity_sign);

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
        signAdapter = new SignAdapter(context);
        mgvSignList.setAdapter(signAdapter);
    }

    private void initListener() {

    }

    private void initBack() {
        direState.getUserLine().setAgree(true);
        //保存节点信息
        DirectPreferences.setUserLive(context, direState.getUserLine());
        //开始地点
        tvSignAddress.setText("开始地点：" + direState.getLiveList().get(0).getContent());
        tvSignTime.setText("开始时间：" + direState.getLiveList().get(0).getDesc());
        //通过跟踪记录的当前线路的当前点的id来查找当前点的蓝牙地址
        for (Live l : direState.getLiveList()) {
            if (l.getId() == direState.getUserLine().getLinePositionId()) {
                device = l.getDevice();
                break;
            }
        }
        //判断是不是已经扫描到这个点了
        boolean isBle = false;
        for (int i = 0; i < direState.getBleList().size(); i++) {
            if (TextUtils.equals(direState.getBleList().get(i), device)) {
                isBle = true;
                break;
            }
        }
        if (isBle) {
            isSign = true;
            //扫到
            initDilog();
        } else {
            //未扫到
            isSign = false;
        }
    }

    private void initDilog() {
        SignDilogFragment signDilogFragment = new SignDilogFragment();
        signDilogFragment.show(getSupportFragmentManager(), "配对坐标成功");
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.rl_cardone_return, R.id.tv_cardone_start})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_cardone_return:
                finish();
                break;
            //开始活动
            case R.id.tv_cardone_start:
                if (isSign) {
                    SignDilogFragment signDilogFragment = new SignDilogFragment();
                    signDilogFragment.show(getSupportFragmentManager(), "配对坐标");
                } else {
                    direState.initToast(context, "请签到后再开始活动", true, 0);
                }
                break;
            default:
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
     * 扫描到ble后但已经选择路线
     *
     * @param b
     */
    public void onEventMainThread(BleFind b) {
        direState.setBleList(b.bleList);
        for (int i = 0; i < direState.getBleList().size(); i++) {
            Log.e(TAG, "---" + direState.getBleList().get(i));
        }
        boolean isBle = false;
        for (int i = 0; i < direState.getBleList().size(); i++) {
            if (TextUtils.equals(direState.getBleList().get(i), device)) {
                isBle = true;
                break;
            }
        }

        if (isBle) {
            //扫到
            isSign = true;
            initDilog();
        } else {
            //未扫到
            isSign = false;
        }
    }

    /**
     * 开始活动
     *
     * @param b
     */
    public void onEventMainThread(SignFind b) {
        //下一个点
        int number = 2;
        //签到下一个点的id
        direState.getUserLine().setLinePositionId(direState.getLiveList().get(number - 1).getId());
        //签到下一个点代表的数字
        int sort = 0;
        sort = direState.getLiveList().get(number - 1).getSort();
        //当前是否签到
        direState.getUserLine().setSign(true);
        //所完成的节点数量
        direState.getUserLine().setNumber(number - 1);
        //保存节点信息
        DirectPreferences.setUserLive(context, direState.getUserLine());
        //跳转节点
        switch (sort) {
            case 0:
                break;
            case 1:
//                initIntent(CardOneActivity.class);
                break;
            case 2:
//                initIntent(CardTwoActivity.class);
                initIntent(CardThreeActivity.class);
                break;
            case 3:
                initIntent(CardThreeActivity.class);
                break;
            default:
                direState.initToast(context, "您选择的点没有游戏", true, 0);
                break;
        }
    }
}
