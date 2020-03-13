package com.cdqf.dire_activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.PlannignDetailsAdapter;
import com.cdqf.dire_ble.BleService;
import com.cdqf.dire_ble.DeleteFind;
import com.cdqf.dire_ble.DeviceFind;
import com.cdqf.dire_ble.ParseUUID;
import com.cdqf.dire_class.Ble;
import com.cdqf.dire_find.BleFind;
import com.cdqf.dire_find.DetailsFind;
import com.cdqf.dire_find.LoadsFind;
import com.cdqf.dire_find.PlannignFind;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_txmap.TXMapLoad;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.cdqf.dire_view.ItemDecoration;
import com.cdqf.dire_view.ScrollInterceptScrollView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.codeboy.android.aligntextview.AlignTextView;

public class VenuesActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = VenuesActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private HttpRequestWrap httpRequestWrap = null;

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_pldetails_return)
    public RelativeLayout rlPldetailsReturn = null;

    //滚动条
    @BindView(R.id.sv_pldetails_sc)
    public ScrollInterceptScrollView svPldetailsSc = null;

    //地图
    @BindView(R.id.mv_map)
    public MapView mvMap = null;

    //路线名
    @BindView(R.id.tv_pldetails_route)
    public TextView tvPldetailsRoute = null;

    //路线详情
    @BindView(R.id.atv_pldetails_route)
    public AlignTextView atvPldetailsRoute = null;

    //图像
    @BindView(R.id.tv_pldetails_planebak)
    public TextView tvPldetailsPlanebak = null;

    @BindView(R.id.rv_pldetails_view)
    public RecyclerView rvPldetailsView = null;

    private PlannignDetailsAdapter plannignDetailsAdapter = null;

    private TencentMap tencentMap = null;

    private View view = null;

    //扫描
    private BluetoothAdapter.LeScanCallback blLeScanCallback = null;

    private BluetoothLeScanner mBluetoothLeScanner = null;

    private ScanCallback mFiveScanCallback;

    private String[] bleOne = {
            "C2:01:6D:00:03:C2",
            "C2:01:6D:00:03:CF",
            "C2:01:6D:00:03:C9",
//            "1E:95:8C:D4:C9:F5",
    };

    private List<String> bleList = new ArrayList<>();

    private Map<Integer, Ble> bleMapList = new HashMap<>();

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
        setContentView(R.layout.activity_venues);

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
        httpRequestWrap = new HttpRequestWrap(context);
        httpRequestWrap.setMethod(HttpRequestWrap.POST);
        imageLoader = direState.getImageLoader(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        tencentMap = mvMap.getMap();
        //标点自定义
        view = LayoutInflater.from(context).inflate(R.layout.map_view, null);
    }

    private void initAdapter() {
        rvPldetailsView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvPldetailsView.addItemDecoration(new ItemDecoration(this, 50));
        plannignDetailsAdapter = new PlannignDetailsAdapter(context);
        rvPldetailsView.setAdapter(plannignDetailsAdapter);

    }

    private void initListener() {
        rlPldetailsReturn.setOnClickListener(this);
    }

    private void initBack() {
        svPldetailsSc.smoothScrollTo(0, 0);
        load();
        initPull();
        eventBus.post(new DeleteFind());
        checkBluetooth();
    }

    public void load() {
        TXMapLoad.setLevel(4);
        String error = TXMapLoad.init(this);
        Log.e(TAG, "---error---" + error);
    }

    private void initPull() {

    }

    /**
     * 检查蓝牙状态,包括版本的判断以及是否符合蓝牙连接条件
     */
    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkBluetooth() {
        //判断机型版本是否符合连接条件true = 不符合
        Boolean isLowVersionSDK = Build.VERSION.SDK_INT < 19;
        if (isLowVersionSDK) {
            Log.e(TAG, "---当前机型版本true---" + Build.VERSION.SDK_INT);
            return;
        } else {
            Log.e(TAG, "---当前机型版本false---" + Build.VERSION.SDK_INT);
            blLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    List<UUID> uuidList = ParseUUID.paresUUIDthree(scanRecord);
                    for (UUID uuid : uuidList) {
                        if (uuid.equals(BleService.UUID_EBOYLIGHT_LED)) {
                            eventBus.post(new DeviceFind(device));
                        }
                    }
                }
            };

            if (direState.getmBluetoothAdapter() != null) {
                direState.getmBluetoothAdapter().startLeScan(blLeScanCallback);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_pldetails_return:
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
        eventBus.unregister(this);
        direState.getBleMapList().clear();
    }

    /**
     * 扫描到ble
     *
     * @param b
     */
    public void onEventMainThread(BleFind b) {
//        direState.dilog(VenuesActivity.this, "连接");
//        Log.e(TAG, "---" + b.bleAddress);
//        for (int i = 0; i < bleOne.length; i++) {
//            if (TextUtils.equals(bleOne[i], b.bleAddress)) {
//                Ble ble = new Ble();
//                ble.setAddress(b.bleAddress);
//                if (direState.getTitleList().size() > 0) {
//                    if(i < direState.getTitleList().size()) {
//                        ble.setTitle(direState.getTitleList().get(i));
//                    } else {
//                        ble.setTitle(false);
//                    }
//                } else {
//                    if (i == 0) {
//                        ble.setTitle(true);
//                    } else {
//                        ble.setTitle(false);
//                    }
//                }
//                bleMapList.put(i, ble);
//                direState.setBleMapList(bleMapList);
//                Log.e(TAG, "---" + i + "---" + bleMapList.get(i).getAddress() + "---" + bleMapList.get(i).isTitle());
//            }
//        }
//        if (bleMapList.size() == 3) {
//            plannignDetailsAdapter.notifyDataSetChanged();
//        }
    }

    public void onEventMainThread(PlannignFind p) {
        direState.dilog(VenuesActivity.this, "连接", p.position);
    }

    public void onEventMainThread(DetailsFind p) {
        Intent intent = new Intent(context,DetailsActivity.class);
        intent.putExtra("position",p.position);
        context.startActivity(intent);
    }

    public void onEventMainThread(LoadsFind l) {
        //设置周边信息
        tencentMap.setHandDrawMapEnable(true);
        //设置地图中心点
        tencentMap.setCenter(new LatLng(l.latitude, l.longitude));
        //设置缩放级别
        tencentMap.setZoom(20);
        Marker marker = tencentMap.addMarker(new MarkerOptions()
                .position(new LatLng(l.latitude, l.longitude))
                .markerView(view)
                .tag(0)
                .icon(BitmapDescriptorFactory
                        .defaultMarker())
                .draggable(true));
        marker.showInfoWindow();
    }


}

