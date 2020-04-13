package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.ViewPagerAdapter;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.library.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class GuideActivity extends FragmentActivity {

    private String TAG = GuideActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private HttpRequestWrap httpRequestWrap = null;

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private List<View> viewList = null;

    private int[] resIds = {R.layout.guide_one, R.layout.guide_two, R.layout.guide_three};

    private View view = null;

    @BindView(R.id.page_how_to)
    public ViewPager viewPager = null;

    @BindView(R.id.indicator)
    public CirclePageIndicator circlePageIndicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_guide);

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
        viewList = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            View view = getLayoutInflater().inflate(resIds[i], null);
            viewList.add(view);
        }
        view = getLayoutInflater().inflate(R.layout.guide_four, null);
    }

    private void initView() {
    }

    private void initAdapter() {

    }

    private void initListener() {
        view.findViewById(R.id.rcrl_guide_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initIntent(MainActivity.class);
                GuideActivity.this.finish();
            }
        });
    }

    private void initBack() {
        viewList.add(view);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        circlePageIndicator.setViewPager(viewPager);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
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
