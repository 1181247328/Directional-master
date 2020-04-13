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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/***景点详情***/
public class DetailsActivity extends BaseActivity {

    private String TAG = DetailsActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private int position = 0;

    private int[] image = {
            R.mipmap.test_image_one,
            R.mipmap.test_image_two,
            R.mipmap.test_image_three,
    };

    private String[] name = {
            "英名廊",
            "烈士群雕",
            "突破湘江纪念碑",
    };

    private String[] nameDetails = {
            "英名廊由广西证监局、国海证券股份有限责任公司捐资修建，长32米，2013年8月对外开放，长廊内用黑色大理石镌刻收集到湘江战役中牺牲的2万多红军烈士的英名.",
            "烈士群雕，长46米，高11米，是目前全国最大的烈士纪念群雕，由四个大型头像和五组浮雕组成。四个大型头像分别代表长征队伍的四种典型人物：“男”、“女”、“老”、“幼”。五组浮雕的名称分别是：“救星”、“送别”、“远征”、“激战”、“永生”，它们再现了湘江战役的宏大历史场面.",
            "纪念碑高达34米，寓意湘江战役发生的时间——1934年。主碑上半部分是三支合抱在一起直插蓝天的步枪造型，体现了枪杆子里出政权的主题思想，三支步枪同时也分别代表了红一、红二、红四方面军。下半部分为一圆拱型建筑，象征着一个供烈士英灵长眠安息的陵墓."
    };

    //返回
    @BindView(R.id.rl_details_return)
    public RelativeLayout rlDetailsReturn = null;

    //名称
    @BindView(R.id.tv_details_name)
    public TextView tvDetailsName = null;

    @BindView(R.id.iv_details_back)
    public ImageView ivDetailsBack = null;

    @BindView(R.id.tv_details_context)
    public TextView tvDetailsContext = null;

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
        setContentView(R.layout.activity_details);

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
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
    }

    private void initView() {
    }

    private void initAdapter() {

    }

    private void initListener() {
    }

    private void initBack() {
        ivDetailsBack.setImageResource(image[position]);
        tvDetailsName.setText(name[position]);
        tvDetailsContext.setText(nameDetails[position]);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_details_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_details_return:
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
