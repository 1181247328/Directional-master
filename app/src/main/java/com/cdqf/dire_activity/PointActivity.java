package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.dire.R;
import com.cdqf.dire_class.Point;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 内容
 */
public class PointActivity extends BaseActivity {

    private String TAG = PointActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private ACache aCache = null;

    //返回
    @BindView(R.id.rl_point_return)
    public RelativeLayout rlPointReturn = null;

    @BindView(R.id.tv_point_name)
    public TextView tvPointName = null;

    @BindView(R.id.vp_point_pager)
    public ViewPager vpPointPager = null;

    private PointAdapter pointAdapter = null;

    private int position = 0;

    private String title = "";

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
        setContentView(R.layout.activity_point);

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
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
    }

    private void initView() {
        vpPointPager.setOffscreenPageLimit(1);
    }

    private void initAdapter() {
        pointAdapter = new PointAdapter(context);
        vpPointPager.setAdapter(pointAdapter);
    }

    private void initListener() {
        vpPointPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e(TAG, "---onPageSelected---" + i);
                tvPointName.setText(direState.getPointList().get(i).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initBack() {
//        tvPointName.setText(direState.getCementList().get(position).getTitle());
        title = direState.getCementList().get(position).getTitle();
        if (aCache.getAsString(title) != null) {
            String data = aCache.getAsString(title);
            direState.getPointList().clear();
            List<Point> pointList = gson.fromJson(data, new TypeToken<List<Point>>() {
            }.getType());
            direState.setPointList(pointList);
            if (pointAdapter != null) {
                pointAdapter.notifyDataSetChanged();
            }
            tvPointName.setText(direState.getPointList().get(0).getTitle());
        }
        initPull(true);
    }

    /**
     * 活动详情
     */
    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        //活动id
        params.put("activity_id", direState.getCementList().get(position).getId());
        Log.e(TAG, "---活动id---" + direState.getCementList().get(position).getId());
        //查询字样
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.GAME_DETAILS_CONTEXT, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse--内容---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                String data = resultJSON.getString("Data");
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            direState.getPointList().clear();
                            List<Point> pointList = gson.fromJson(data, new TypeToken<List<Point>>() {
                            }.getType());
//                            List<Point> pointOneList = pointList;
//                            pointList.clear();
//                            for (int i = 0; i < pointOneList.size(); i++) {
//                                List<String> imageList = pointOneList.get(i).getImg_url();
//                                pointOneList.get(i).getImg_url().clear();
//                                for (int j = 0; j < imageList.size(); i++) {
//                                    String image = imageList.get(j).replace("/", "\\");
//                                    pointOneList.get(i).getImg_url().add(image);
//                                }
//
//                            }
//                            pointList.addAll(pointOneList);
                            direState.setPointList(pointList);
                            if (pointAdapter != null) {
                                pointAdapter.notifyDataSetChanged();
                            }
                            tvPointName.setText(direState.getPointList().get(0).getTitle());
                            aCache.put(title, data);
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
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_point_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_point_return:
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
//        pointAdapter.getBaPointCarousel().startAutoPlay();//开始轮播
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

    class PointAdapter extends PagerAdapter {

        private String TAG = PointAdapter.class.getSimpleName();

        private DireState direState = DireState.getDireState();

        private Context context = null;

        private ImageLoader imageLoader = null;

        //测试内容
        private String text = "广西药用植物园位于广西壮族自治区南宁市厢竹大道，创建于1959年，占地面积202公顷，是中国对外（国际）开放的二十一个大型植物园之一。建园52年来，广西药用植物园共收集、保存活体药用植物品种5600多种，其中珍稀濒危药用植物100多种。广西药用植物园坐落在距南宁市区8公里的东郊山峦，占地200多万平方米，是中国及东南亚地区最大的药用植物园之一。园内林木苍翠，藤蔓纵横，加上棚架点缀其间，小桥流水更添雅趣。该园已成立了旅游公司，正式推出广西药用植物园一日游，可以大规模地接待游客。广西药用植物园是一座容游览、科研、教学和生产于一体的综合性园地。广西药用植物园位于广西壮族自治区南宁市厢竹大道，创建于1959年，占地面积202公顷，是中国对外（国际）开放的二十一个大型植物园之一。建园52年来，广西药用植物园共收集、保存活体药用植物品种5600多种，其中珍稀濒危药用植物100多种。广西药用植物园坐落在距南宁市区8公里的东郊山峦，占地200多万平方米，是中国及东南亚地区最大的药用植物园之一。园内林木苍翠，藤蔓纵横，加上棚架点缀其间，小桥流水更添雅趣。该园已成立了旅游公司，正式推出广西药用植物园一日游，可以大规模地接待游客。广西药用植物园是一座容游览、科研、教学和生产于一体的综合性园地。\";\n广西药用植物园位于广西壮族自治区南宁市厢竹大道，创建于1959年，占地面积202公顷，是中国对外（国际）开放的二十一个大型植物园之一。建园52年来，广西药用植物园共收集、保存活体药用植物品种5600多种，其中珍稀濒危药用植物100多种。广西药用植物园坐落在距南宁市区8公里的东郊山峦，占地200多万平方米，是中国及东南亚地区最大的药用植物园之一。园内林木苍翠，藤蔓纵横，加上棚架点缀其间，小桥流水更添雅趣。该园已成立了旅游公司，正式推出广西药用植物园一日游，可以大规模地接待游客。广西药用植物园是一座容游览、科研、教学和生产于一体的综合性园地。\";\n";

        public PointAdapter(Context context) {
            this.context = context;
            imageLoader = direState.getImageLoader(context);
        }

        @Override
        public int getCount() {
            return direState.getPointList().size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_point, null);
            //图片
            Banner baPointCarousel = v.findViewById(R.id.ba_point_item_carousel);
            bannerCarousel(baPointCarousel, position);
            //标题
            TextView tvPointItemTitle = v.findViewById(R.id.tv_point_item_title);
            tvPointItemTitle.setText(direState.getPointList().get(position).getTitle());
            //内容
            HtmlTextView htvPointItemContent = v.findViewById(R.id.htv_point_item_content);
            htvPointItemContent.setHtml("  " + direState.getPointList().get(position).getDesc(), new HtmlHttpImageGetter(htvPointItemContent));
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        private void bannerCarousel(Banner baPointCarousel, int position) {
            baPointCarousel.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            baPointCarousel.setImageLoader(new GlideImageLoader());
            baPointCarousel.setImages(direState.getPointList().get(position).getImg_url());
            baPointCarousel.setBannerAnimation(Transformer.Default);
            baPointCarousel.isAutoPlay(true);
            baPointCarousel.setDelayTime(3000);
            baPointCarousel.setIndicatorGravity(BannerConfig.CENTER);
            baPointCarousel.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {

                }
            });
            baPointCarousel.start();
        }

        class GlideImageLoader extends com.youth.banner.loader.ImageLoader {

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String image = (String) path;
                String images = image.replace("\\", "/");
                imageLoader.displayImage(
                        DirectAddaress.ADDRESS + images,
                        imageView,
                        direState.getImageLoaderOptions(
                                R.mipmap.not_loaded,
                                R.mipmap.not_loaded,
                                R.mipmap.not_loaded));
            }
        }
    }
}
