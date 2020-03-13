package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.cdqf.dire.R;
import com.cdqf.dire_class.Through;
import com.cdqf.dire_okhttp.OKHttpRequestWrap;
import com.cdqf.dire_okhttp.OnHttpRequest;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 依次穿越
 */
public class ThroughActivity extends FragmentActivity {

    private String TAG = ThroughActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_through_return)
    public RelativeLayout rlThroughReturn = null;

    //地图
    @BindView(R.id.mv_through_map)
    public MapView mvThroughMap = null;

    private AMap aMap = null;

    private int dire = 0;

    private MyLocationStyle myLocationStyle = null;

    private String[] name = {
            "成都启锋科技有限公司",
            "大安东路桥",
            "红星桥",
            "市二医院"
    };

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_through);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

        initAgo();

        mvThroughMap.onCreate(savedInstanceState);

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
        aMap = mvThroughMap.getMap();
    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));//地图缩放
//        final LatLng latLngOne = new LatLng(30.672174, 104.085141);
////        LatLng latLngTwo = new LatLng(30.672355, 104.086519);
////        LatLng latLngThree = new LatLng(30.671178, 104.090398);
////        LatLng latLngFour = new LatLng(30.660833, 104.084057);
////        List<LatLng> latLngList = new ArrayList<>();
////        latLngList.add(latLngOne);
////        latLngList.add(latLngTwo);
////        latLngList.add(latLngThree);
////        latLngList.add(latLngFour);
//        for (int i = 0; i < name.length; i++) {
//            View view = LayoutInflater.from(context).inflate(R.layout.map_view, null);
//            TextView tvMapTitle = view.findViewById(R.id.tv_map_title);
//            tvMapTitle.setText(name[i]);
//            MarkerOptions markerOption = new MarkerOptions();
//            markerOption.position(latLngList.get(i));
//            markerOption.icon(BitmapDescriptorFactory.fromView(view));
//            markerOption.zIndex(i);
//            aMap.addMarker(markerOption);
//        }
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOne, 16));
//        aMap.addPolyline(new PolylineOptions().addAll(latLngList).width(5).color(ContextCompat.getColor(context, R.color.main_team_back)));
        initPull(true);
    }

    /**
     * 点标详情
     */
    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        //活动id
        params.put("id", direState.getCementList().get(position).getId());
        //查询字样
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(DirectAddaress.THROUGH, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                boolean isStatus = resultJSON.getBoolean("Status");
                int error_code = resultJSON.getInteger("error_code");
                String msg = resultJSON.getString("msg");
                String data = resultJSON.getString("Data");
                if (isStatus) {
                    switch (error_code) {
                        //获取成功
                        case 0:
                            List<Through> throughList = gson.fromJson(data, new TypeToken<List<Through>>() {
                            }.getType());
                            if (throughList.size() <= 0) {
                                return;
                            }
                            List<LatLng> latLngList = new ArrayList<>();
                            for (int i = 0; i < throughList.size(); i++) {
                                View view = LayoutInflater.from(context).inflate(R.layout.map_view, null);
                                TextView tvMapTitle = view.findViewById(R.id.tv_map_title);
                                String name = throughList.get(i).getTitle();
                                if (i == 0) {
                                    name += "(起点)";
                                } else if (i == throughList.size() - 1) {
                                    name += "(终点)";
                                } else {
                                    //TODO
                                }
                                tvMapTitle.setText(name);
                                double latitude = Double.parseDouble(throughList.get(i).getLatitude());
                                double longitude = Double.parseDouble(throughList.get(i).getLongitude());
                                LatLng latLng = new LatLng(latitude, longitude);
                                latLngList.add(latLng);
                                MarkerOptions markerOption = new MarkerOptions();
                                markerOption.position(latLng);
                                markerOption.icon(BitmapDescriptorFactory.fromView(view));
                                markerOption.zIndex(i);
                                aMap.addMarker(markerOption);
                            }
                            double latitude = Double.parseDouble(throughList.get(0).getLatitude());
                            double longitude = Double.parseDouble(throughList.get(0).getLongitude());
                            LatLng latLng = new LatLng(latitude, longitude);
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            aMap.addPolyline(new PolylineOptions().addAll(latLngList).width(5).color(ContextCompat.getColor(context, R.color.main_team_back)));
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

    @OnClick({R.id.rl_through_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_through_return:
                finish();
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
        mvThroughMap.onPause();
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
        mvThroughMap.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        mvThroughMap.onDestroy();
    }
}
