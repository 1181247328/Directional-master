package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_dilog.ShareDilogFragment;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_view.GoTopScrollView;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 活动详情
 */
public class CementContextActivity extends BaseActivity {

    private String TAG = CementContextActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    private ACache aCache = null;

    @BindView(R.id.srl_cements_pull)
    public SwipeRefreshLayout srlCementsPull = null;

    //返回
    @BindView(R.id.rl_cements_return)
    public RelativeLayout rlCementsReturn = null;

    //分享
    @BindView(R.id.rl_cements_share)
    public RelativeLayout rlCementsShare = null;

    //滚动条
    @BindView(R.id.tgsc_cments_scv)
    public GoTopScrollView tgscCmentsScv = null;

    //置顶
    @BindView(R.id.iv_cments_top)
    public ImageView ivCmentsTop = null;

    //图像
    @BindView(R.id.iv_cements_hear)
    public ImageView ivCementsHear = null;

    //依次穿越
    @BindView(R.id.rcrl_cements_through)
    public RCRelativeLayout rcrlCementsThrough = null;

    //数量
    @BindView(R.id.rcrl_cements_nodes)
    public RCRelativeLayout rcrlCementsNodes = null;

    @BindView(R.id.tv_cements_nodes)
    public TextView tvCementsNodes = null;

    //标题
    @BindView(R.id.tv_cements_title)
    public TextView tvCementsTitle = null;

    //时间
    @BindView(R.id.tv_cements_timer)
    public TextView tvCementsTimer = null;

    //地址
    @BindView(R.id.tv_cements_address)
    public TextView tvCementsAddress = null;

    @BindView(R.id.htv_cements_content)
    public HtmlTextView htvCementsContent = null;

    private int position = 0;

    private String title = "";

    //测试内容
    private String text = "广西药用植物园位于广西壮族自治区南宁市厢竹大道，创建于1959年，占地面积202公顷，是中国对外（国际）开放的二十一个大型植物园之一。建园52年来，广西药用植物园共收集、保存活体药用植物品种5600多种，其中珍稀濒危药用植物100多种。广西药用植物园坐落在距南宁市区8公里的东郊山峦，占地200多万平方米，是中国及东南亚地区最大的药用植物园之一。园内林木苍翠，藤蔓纵横，加上棚架点缀其间，小桥流水更添雅趣。该园已成立了旅游公司，正式推出广西药用植物园一日游，可以大规模地接待游客。广西药用植物园是一座容游览、科研、教学和生产于一体的综合性园地。";


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
        setContentView(R.layout.activity_cementcontext);

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
        aCache = ACache.get(context);
        imageLoader = direState.getImageLoader(context);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlCementsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlCementsPull.setRefreshing(true);
                initPull(false);
            }
        });
        tgscCmentsScv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                srlCementsPull.setEnabled(tgscCmentsScv.getScrollY() == 0);
            }
        });
    }

    private void initBack() {
        tgscCmentsScv.setImgeViewOnClickGoToFirst(ivCmentsTop);
        tgscCmentsScv.setScreenHeight(direState.getDisPlyHeight(context));
        title = direState.getCementList().get(position).getTitle();
        if (aCache.getAsString("context" + title) != null) {
            JSONObject data = aCache.getAsJSONObject("context" + title);
            parsing(data);
        }
        initPull(true);
    }

    private void parsing(JSONObject data) {
        //图片
        String img = data.getString("img").replace("\\", "/");
        imageLoader.displayImage(DirectAddaress.ADDRESS + img, ivCementsHear, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        //标题
        String title = data.getString("title");
        tvCementsTitle.setText(title);
        //点数
        String nodes = data.getString("nodes");
//        tvCementsNodes.setText(nodes + "个点标");
        //时间
        String start_time = data.getString("start_time");
        tvCementsTimer.setText(start_time);
        //地点
        String name = data.getString("name");
        tvCementsAddress.setText(name);
        //详情
        String desc = data.getString("desc");
        htvCementsContent.setHtml("  " + desc, new HtmlHttpImageGetter(htvCementsContent));
    }

    /**
     * 活动详情
     */
    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        //活动id
        params.put("id", direState.getCementList().get(position).getId());
        //查询字样
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.GAME_DETAILS, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---" + response);
                if (srlCementsPull != null) {
                    srlCementsPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                JSONObject data = resultJSON.getJSONObject("Data");
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            parsing(data);
                            aCache.put("context" + title, data);
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (error_code) {
                        default:

                            break;
                    }
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlCementsPull != null) {
                    srlCementsPull.setRefreshing(false);
                }
            }
        });
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

    @OnClick({R.id.rl_cements_return, R.id.rcrl_cements_through, R.id.rl_cements_share, R.id.rcrl_cements_nodes})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_cements_return:
                finish();
                break;
            //依次穿越
            case R.id.rcrl_cements_through:
                initIntent(ThroughActivity.class, position);
                break;
            //分享
            case R.id.rl_cements_share:
                ShareDilogFragment shareDilogFragment = new ShareDilogFragment();
                shareDilogFragment.show(getSupportFragmentManager(), "分享");
                break;
            default:
                //TODO
                break;
            //点标
            case R.id.rcrl_cements_nodes:
                initIntent(PointActivity.class, position);
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