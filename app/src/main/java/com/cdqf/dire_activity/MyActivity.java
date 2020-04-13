package com.cdqf.dire_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.OrderAdapter;
import com.cdqf.dire_adapter.OtherAdapter;
import com.cdqf.dire_dilog.WhyDilogFragment;
import com.cdqf.dire_find.LoginCancelFind;
import com.cdqf.dire_find.MyContactFind;
import com.cdqf.dire_find.MyEditFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DataCleanManager;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 个人中心
 */
public class MyActivity extends BaseActivity {

    private String TAG = MyActivity.class.getSimpleName();

    public static MyActivity myActivity = null;

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_my_return)
    public RelativeLayout rlMyReturn = null;

    //设置
    @BindView(R.id.tv_my_set)
    public TextView tvMySet = null;

    //个人信息
    @BindView(R.id.ll_my_layoutone)
    public LinearLayout llMyLayoutone = null;

    //头像
    @BindView(R.id.iv_my_hear)
    public ImageView ivMyHear = null;

    //名称
    @BindView(R.id.tv_my_name)
    public TextView tvMyName = null;

    //电话
    @BindView(R.id.tv_my_account)
    public TextView tvMyAccount = null;

    //退出登录
    @BindView(R.id.tv_my_cancel)
    public TextView tvMyCancel = null;

    //订单
    @BindView(R.id.mgv_my_list)
    public MyGridView mgvMyList = null;

    private OrderAdapter orderAdapter = null;

    //其它功能
    @BindView(R.id.mgv_my_other)
    public MyGridView mgvMyOther = null;

    private OtherAdapter otherAdapter = null;

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
        setContentView(R.layout.activity_my);

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
        myActivity = this;
        imageLoader = direState.getImageLoader(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {
        //订单
        orderAdapter = new OrderAdapter(context);
        mgvMyList.setAdapter(orderAdapter);
        //其它功能
        otherAdapter = new OtherAdapter(context);
        mgvMyOther.setAdapter(otherAdapter);
    }

    private void initListener() {
        mgvMyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (true) {
                    direState.initToast(context, "暂未开通", true, 0);
                    return;
                }
                initIntent(MyOrderActivity.class, position);
            }
        });
        mgvMyOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //我的线路
                    case 0:
                        if (true) {
                            direState.initToast(context, "暂未开通", true, 0);
                            return;
                        }
                        initIntent(LineActivity.class);
                        break;
                    //我的队伍
                    case 1:
                        if (true) {
                            direState.initToast(context, "暂未开通", true, 0);
                            return;
                        }
                        initIntent(TeamActivity.class);
                        break;
                    //二维码
                    case 2:
                        if (true) {
                            direState.initToast(context, "暂未开通", true, 0);
                            return;
                        }
                        break;
                    //我的卡包
                    case 3:
                        if (true) {
                            direState.initToast(context, "暂未开通", true, 0);
                            return;
                        }
                        initIntent(MedalActivity.class);
                        break;
                    //联系我们
                    case 4:
                        WhyDilogFragment whyTwoDilogFragment = new WhyDilogFragment();
                        whyTwoDilogFragment.setInit(4, "联系客服", "您正在联系客服,您的通话有可能被录音,是否通话", "否", "是");
                        whyTwoDilogFragment.show(getSupportFragmentManager(), "联系我们");
                        break;
                    //关于我们
                    case 5:
                        initIntent(AboutActivity.class);
                        break;
                    //清空缓存
                    case 6:
                        WhyDilogFragment whyFourDilogFragment = new WhyDilogFragment();
                        try {
                            whyFourDilogFragment.setInit(8, "清空缓存", "当前缓存为" + DataCleanManager.getTotalCacheSize(context) + ",清空缓存将导致下载的内容删除,是否确定.", "否", "是");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        whyFourDilogFragment.show(getSupportFragmentManager(), "清空缓存");
                        break;
                    //退出登录
                    case 7:
                        WhyDilogFragment whyFiveDilogFragment = new WhyDilogFragment();
                        whyFiveDilogFragment.setInit(1, "退出登录", "退出登录将清空现有的登录信息,请慎重操作", "取消", "退出");
                        whyFiveDilogFragment.show(getSupportFragmentManager(), "退出登录");
                        break;
                }
            }
        });
    }

    private void initBack() {
        if (!direState.getUser().isQQ()) {
            imageLoader.displayImage(DirectAddaress.ADDRESS + direState.getUser().getImg(), ivMyHear, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        } else {
            imageLoader.displayImage(direState.getUser().getImg(), ivMyHear, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        }
        tvMyName.setText(direState.getUser().getNickname());
        if (direState.getUser().getPhone() != null) {
            tvMyAccount.setText(direState.phoneEmpty(direState.getUser().getPhone()));
        } else {
            tvMyAccount.setText("暂无");
        }
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @OnClick({R.id.rl_my_return, R.id.tv_my_set, R.id.ll_my_layoutone, R.id.tv_my_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_my_return:
                finish();
                break;
            //设置
            case R.id.tv_my_set:
                initIntent(PersonalActivity.class);
                break;
            //个人信息
            case R.id.ll_my_layoutone:
                initIntent(PersonalActivity.class);
                break;
            //退出登录
            case R.id.tv_my_cancel:
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
     * 联系我们
     *
     * @param m
     */
    @SuppressLint("MissingPermission")
    public void onEventMainThread(MyContactFind m) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13551021774"));
        startActivity(intent);
    }

    public void onEventMainThread(MyEditFind m) {
        initBack();
    }

    /**
     * 退出登录
     *
     * @param l
     */
    public void onEventMainThread(LoginCancelFind l) {
        DirectPreferences.clearUser(context);
        direState.setLogin(false);
        direState.setUser(null);
        initIntent(LoginActivity.class);
        finish();
    }
}