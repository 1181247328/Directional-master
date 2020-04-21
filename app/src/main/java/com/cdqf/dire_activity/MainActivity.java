package com.cdqf.dire_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.cdqf.dire.R;
import com.cdqf.dire_adapter.PersonalAdapter;
import com.cdqf.dire_ble.BleBroadcastReceiver;
import com.cdqf.dire_ble.BleCloseFind;
import com.cdqf.dire_ble.BleOpenFind;
import com.cdqf.dire_ble.BleService;
import com.cdqf.dire_ble.DeviceFind;
import com.cdqf.dire_ble.ParseUUID;
import com.cdqf.dire_class.Tencent;
import com.cdqf.dire_class.User;
import com.cdqf.dire_class.Venues;
import com.cdqf.dire_find.BleFind;
import com.cdqf.dire_find.BleOneFind;
import com.cdqf.dire_find.ExitFind;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_floatball.FloatService;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.DirectPreferences;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_txmap.TXMapLoad;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.cdqf.dire_utils.OnResponseHandler;
import com.cdqf.dire_utils.RequestHandler;
import com.cdqf.dire_utils.RequestStatus;
import com.cdqf.dire_view.ListViewForScrollView;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.map.geolocation.TencentPoi;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 主MainActivity
 */
public class MainActivity extends BaseActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private HttpRequestWrap httpRequestWrap = null;

    private Gson gson = new Gson();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ACache aCache = null;

    //侧滑菜单
    @BindView(R.id.dl_main_menu)
    public DrawerLayout dlMainMenu = null;

    //用户信息
    @BindView(R.id.tv_main_user)
    public RelativeLayout tvMainUser = null;

    @BindView(R.id.rl_mian_layout_five)
    public LinearLayout rlMianLayoutFive = null;

    //高德地图
    @BindView(R.id.mv_map)
    public MapView mMapView = null;

    //头像(用于侧滑的缩放)
    @BindView(R.id.rcrl_main_hear)
    public RCRelativeLayout rcrlMainHear = null;

    @BindView(R.id.hear)
    public ImageView hear = null;

    //输入信息
    @BindView(R.id.xet_main_input)
    public TextView xetMainInput = null;

    //扫码
    @BindView(R.id.iv_main_scanning)
    public ImageView ivMainScanning = null;

    //点标检测
    @BindView(R.id.rl_main_layout_four)
    public RCRelativeLayout rlMainLayoutFour = null;

    //定位
    @BindView(R.id.rl_main_layout_three)
    public RCRelativeLayout rlMainLayoutThree = null;

    //寻队组队
    @BindView(R.id.ll_main_team)
    public LinearLayout llMainTeam = null;

    //订单管理
    @BindView(R.id.ll_main_management)
    public LinearLayout llMainManagement = null;

    //线路
    @BindView(R.id.rcrl_main_line)
    public RCRelativeLayout rcrlMainLine = null;

    //头像
    @BindView(R.id.iv_main_hear)
    public ImageView ivMainHear = null;

    //昵称
    @BindView(R.id.tv_main_nickname)
    public TextView tvMainNickname = null;

    //二维码
    @BindView(R.id.rl_main_code)
    public RelativeLayout rlMainCode = null;

    //信息列表
    @BindView(R.id.lv_main_list)
    public ListViewForScrollView lvMainList = null;

    //线路预览
    @BindView(R.id.rl_main_layout_five)
    public RelativeLayout rlMainLayoutFive = null;

    private EventBus eventBus = EventBus.getDefault();

    //抽屉菜单对象
    private ActionBarDrawerToggle drawerbar = null;

    private PersonalAdapter personalAdapter = null;

    //蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;

    public List<TencentPoi> tencentPoiList = new CopyOnWriteArrayList<>();

    private List<Tencent> tencentList = new CopyOnWriteArrayList<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //扫描
    private BluetoothAdapter.LeScanCallback blLeScanCallback = null;

    private BluetoothLeScanner mBluetoothLeScanner = null;

    private ScanCallback mFiveScanCallback;

    private int position = 0;

    private AMap aMap = null;

    private UiSettings uiSettings = null;

    private MyLocationStyle myLocationStyle = null;

    //定位当前的坐标
    private LatLng latLngDire = null;

    private int dire = 0;

    private GeocodeSearch geocodeSearch = null;

    private String city = "桂林市";

    private int resumed;

    private Intent intent = null;

    /****新版新增加****/
    @BindView(R.id.mzbv_main_list)
    public MZBannerView mzbvMainList = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_main);

        //API>=20以上用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

        initAgo();

        mMapView.onCreate(savedInstanceState);

        initView();

        initAdapter();

        initListener();

        initBack();

    }

    private void initAgo() {
        //上下文
        context = this;
        Log.e(TAG, "---SHA1---" + direState.sHA1(context));
        //蓝牙地址所代表的集合
        direState.bleAddrss(context);
        //网络请求实例化
        httpRequestWrap = new HttpRequestWrap(context);
        httpRequestWrap.setMethod(HttpRequestWrap.POST);
        //请求权限
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                },
                1);

        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        aCache = ACache.get(context);

        imageLoader = direState.getImageLoader(context);

        //注册注解
        ButterKnife.bind(this);
        if (DirectPreferences.getPosiList(context) != null) {
            direState.setPosiListList(DirectPreferences.getPosiList(context));
        }
    }

    /**
     * 配置底部导航栏
     */
    private void initView() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (uiSettings == null) {
            uiSettings = aMap.getUiSettings();
        }
    }

    private void initAdapter() {
        personalAdapter = new PersonalAdapter(context);
        lvMainList.setAdapter(personalAdapter);
    }

    private void initListener() {

    }

    private void initBack() {
        User user = gson.fromJson(aCache.getAsString("user"), User.class);
        if (user != null) {
            direState.setUser(user);
            direState.setLogin(true);
        } else {
            direState.setLogin(false);
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        BleBroadcastReceiver bleBroadcastReceiver = new BleBroadcastReceiver();
        registerReceiver(bleBroadcastReceiver, intentFilter);
        drawerbar = new ActionBarDrawerToggle(this, dlMainMenu, R.mipmap.ic_launcher, R.mipmap.ic_launcher) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.e(TAG, "---侧滑打开---");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.e(TAG, "---侧滑关闭---");
                dlMainMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                        Gravity.LEFT);
            }
        };
        dlMainMenu.addDrawerListener(drawerbar);
        dlMainMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.LEFT);
        /***蓝牙***/
        isBle();
//        if (direState.getWelcome() == 0) {
//            initIntent(WelcomeActivity.class);
//            direState.setWelcome(1);
//        } else if (direState.getWelcome() == 1) {
//            //TODO
//        } else {
//            //TODO
//        }
        uiSettings.setZoomControlsEnabled(false);
        initLocationStyle();
        distanceQuery();

        intent = new Intent(MainActivity.this, FloatService.class);
        //启动FloatViewService
        startService(intent);

        //授权开启悬浮窗
        try {
            if (!permissionCheck(context)) {
                permissionApplyInternal(context);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        initBanner();

    }

    private void initBanner() {
        List<String> urlsList = new ArrayList<String>();
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587363916983&di=e4016258434f9eba03eb56082860922c&imgtype=0&src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110111%2F292-110111035J3100.jpg");
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587363916983&di=e4016258434f9eba03eb56082860922c&imgtype=0&src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110111%2F292-110111035J3100.jpg");
        urlsList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587363916983&di=e4016258434f9eba03eb56082860922c&imgtype=0&src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110111%2F292-110111035J3100.jpg");
        mzbvMainList.setPages(urlsList, new MZHolderCreator<BannerViewHolder>() {

            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mzbvMainList.setDelayedTime(3000);
        mzbvMainList.setIndicatorVisible(false);
        mzbvMainList.setDuration(1000);
        mzbvMainList.start();
    }


    /**
     * 判断悬浮窗是否授权
     *
     * @param context
     * @return
     */
    private boolean permissionCheck(Context context) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return result;
    }

    /**
     * 引导用户前往授权
     *
     * @param context
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void permissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    //定位蓝点
    private void initLocationStyle() {
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                Log.e(TAG, "---城市---" + regeocodeResult.getRegeocodeAddress().getCity());
                city = regeocodeResult.getRegeocodeAddress().getCity();
                direState.setCity(city);
                venues(city);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));//地图缩放
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(200); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    //维度
                    double latitude = location.getLatitude();
                    //经度
                    double longitude = location.getLongitude();
                    Log.e(TAG, "---纬度---" + latitude + "---经度---" + longitude + "---城市---");
                    latLngDire = new LatLng(latitude, longitude);
                    LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                    if (dire <= 2) {
                        dire++;
                        //代表定位当前
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDire, 16));
                        direState.setLatLonPoint(latLonPoint);
                        if (dire == 2) {
                            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                            geocodeSearch.getFromLocationAsyn(query);
                        }
                    }
                }
            }
        });


    }

    public void initPoi() {
        PoiSearch.Query query = new PoiSearch.Query("店", "", "028");
        query.setPageSize(10);
        query.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(context, query);
        LatLonPoint latLonPoint = new LatLonPoint(30.672024, 104.085349);
        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 500));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Log.e("---POI搜索---", "---返回的poi数---" + poiResult.getPois().size() + "---验证码---" + i);
                for (int number = 0; number < poiResult.getPois().size(); number++) {
                    Log.e("---POI搜索---", "---经度---" + poiResult.getPois().get(number).getLatLonPoint().getLongitude() +
                            "---纬度---" + poiResult.getPois().get(number).getLatLonPoint().getLatitude() +
                            "---名称---" + poiResult.getPois().get(number).getTitle() +
                            "---地址---" + poiResult.getPois().get(number).getSnippet());
                    //将位置显示在地图上
                    View view = LayoutInflater.from(context).inflate(R.layout.map_view, null);
                    TextView tvMapTitle = view.findViewById(R.id.tv_map_title);
                    tvMapTitle.setText(poiResult.getPois().get(number).getTitle());
                    MarkerOptions markerOption = new MarkerOptions();
                    LatLng latLng = new LatLng(poiResult.getPois().get(number).getLatLonPoint().getLatitude(), poiResult.getPois().get(number).getLatLonPoint().getLongitude());
                    markerOption.position(latLng);
                    markerOption.icon(BitmapDescriptorFactory.fromView(view));
                    markerOption.zIndex(number);
                    aMap.addMarker(markerOption);
                    aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Log.e(TAG, "---Marker---" + Math.round(marker.getZIndex()));
                            return false;
                        }
                    });
                }

//                poiItem = poiResult.getPois();
//                venues();
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });

        poiSearch.searchPOIAsyn();
    }

    public void distanceQuery() {
        LatLonPoint latLngOne = new LatLonPoint(30.672174, 104.085141);
        LatLonPoint latLngTwo = new LatLonPoint(30.672355, 104.086519);
        LatLonPoint latLngThree = new LatLonPoint(30.671178, 104.090398);
        LatLonPoint latLngFour = new LatLonPoint(30.660833, 104.084057);
        List<LatLonPoint> latLngList = new ArrayList<>();
        latLngList.add(latLngOne);
        latLngList.add(latLngTwo);
        latLngList.add(latLngThree);
        DistanceSearch distanceSearch = new DistanceSearch(context);
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                for (int j = 0; j < distanceResult.getDistanceResults().size(); j++) {
                    Log.e(TAG, "---起点为---" + distanceResult.getDistanceResults().get(j).getOriginId()
                            + "---终点为---" + distanceResult.getDistanceResults().get(j).getDestId()
                            + "---之间距离---" + distanceResult.getDistanceResults().get(j).getDistance());
                }
            }
        });
        DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
        distanceQuery.setOrigins(latLngList);
        distanceQuery.setDestination(latLngFour);
        //直线距离
        distanceQuery.setType(DistanceSearch.TYPE_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
    }

    public void venues(String city) {
        httpRequestWrap.setCallBack(new RequestHandler(context, 1, "请稍候", new OnResponseHandler() {
            @Override
            public void onResponse(String result, RequestStatus status) {
                Log.e(TAG, "---会馆列表---" + result);
                if (status == RequestStatus.SUCCESS) {
                    if (result != null) {
                        JSONObject resultJSON = JSON.parseObject(result);
                        boolean isStatus = resultJSON.getBoolean("Status");
                        int error_code = resultJSON.getInteger("error_code");
                        String msg = resultJSON.getString("msg");
                        String data = resultJSON.getString("Data");
                        if (isStatus) {
                            switch (error_code) {
                                //获取成功
                                case 0:
                                    direState.getVenuesList().clear();
                                    List<Venues> venuesList = gson.fromJson(data, new TypeToken<List<Venues>>() {
                                    }.getType());
                                    direState.setVenuesList(venuesList);
                                    //初始化
                                    for (int i = 0; i < direState.getVenuesList().size(); i++) {
                                        View view = LayoutInflater.from(context).inflate(R.layout.map_view, null);
                                        TextView tvMapTitle = view.findViewById(R.id.tv_map_title);
                                        tvMapTitle.setText(direState.getVenuesList().get(i).getTitle());
                                        //显示地图
                                        MarkerOptions markerOption = new MarkerOptions();
                                        double latitude = Double.parseDouble(direState.getVenuesList().get(i).getLatitude());
                                        double longitude = Double.parseDouble(direState.getVenuesList().get(i).getLongitude());
                                        LatLng latLng = new LatLng(latitude, longitude);
                                        markerOption.position(latLng);
                                        markerOption.icon(BitmapDescriptorFactory.fromView(view));
                                        markerOption.zIndex(i);
                                        aMap.addMarker(markerOption);
                                        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker) {
                                                Log.e(TAG, "---Marker---" + Math.round(marker.getZIndex()));
                                                return false;
                                            }
                                        });
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        direState.initToast(context, "获取数据失败,请检查网络", true, 0);
                    }
                } else {
                    direState.initToast(context, "请求失败,请检查网络", true, 0);
                }
            }
        }));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("city_name", city);
        httpRequestWrap.send(DirectAddaress.SITE, map);
    }


    public void load() {
        TXMapLoad.setLevel(4);
        String error = TXMapLoad.init(this);
        Log.e(TAG, "---error---" + error);
    }

    @SuppressLint("MissingPermission")
    public void isBle() {
        boolean isBle = isBluetoothAdapter();
        Log.e(TAG, "---" + isBle);
        if (isBle) {
            return;
        } else {
            isEnabled();
        }
        direState.setmBluetoothAdapter(mBluetoothAdapter);
        Intent serviceIntent = new Intent(MainActivity.this, BleService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        checkBluetooth();
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int type) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position, int isRoute) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        intent.putExtra("isRoute", isRoute);
        startActivity(intent);
    }

    private void initIntentId(Class<?> activity, int live_id) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("live_id", live_id);
        startActivity(intent);
    }

    /**
     * 判断是否有蓝牙
     */
    private boolean isBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "请升级版本至4.0.3", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    private void isEnabled() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        } else {
            direState.initToast(context, "当前蓝牙已经打开", true, 0);
        }
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
                            Log.e(TAG, "---" + device.getName() + "---" + device.getAddress());
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

    /**
     * 监听事件
     *
     * @param v
     */
    @SuppressLint("MissingPermission")
    @OnClick({R.id.rcrl_main_hear, R.id.iv_main_scanning, R.id.rl_main_layout_four, R.id.rl_main_layout_three, R.id.xet_main_input,
            R.id.ll_main_team, R.id.ll_main_management, R.id.rcrl_main_line, R.id.rl_main_code, R.id.tv_main_user, R.id.rl_main_layout_five})
    public void onClick(View v) {
        switch (v.getId()) {
            //头像
            case R.id.rcrl_main_hear:
//                dlMainMenu.openDrawer(Gravity.LEFT);
//                dlMainMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
//                        Gravity.LEFT);
//                initIntent(LoginActivity.class);
                if (direState.isLogin()) {
                    initIntent(MyActivity.class);
                } else {
                    initIntent(LoginActivity.class);
                }
                break;
            //扫码
            case R.id.iv_main_scanning:
                break;
            //搜索
            case R.id.xet_main_input:
                initIntent(SearchActivity.class);
                break;
            //点标检测
            case R.id.rl_main_layout_four:
                isEnabled();
                break;
            //定位
            case R.id.rl_main_layout_three:
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDire, 16));
                break;
            //发现活动
            case R.id.ll_main_team:
                initIntent(PeriodActivity.class);
                break;
            //发现景区
            case R.id.ll_main_management:
                initIntent(ScenicActivity.class);
                break;
            //线路
            case R.id.rcrl_main_line:
                initIntent(CementActivity.class);
                break;
            //二维码
            case R.id.rl_main_code:
                break;
            //用户信息
            case R.id.tv_main_user:
                initIntent(UserActivity.class);
                break;
            //线路预览
            case R.id.rl_main_layout_five:
                initIntent(CementActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "---requestCode---" + requestCode + "---resultCode---" + resultCode);
        if (requestCode == 0 && resultCode == 0) {
            Log.e(TAG, "---程序已退出---");
            direState.initToast(context, "程序退出", true, 0);
            exit();
        } else if (requestCode == 0 && resultCode == -1) {
            Log.e(TAG, "---扫描蓝牙---");
            checkBluetooth();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION) {
                    initLocationStyle();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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
        mMapView.onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
        mzbvMainList.pause();
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
        mMapView.onResume();
        mzbvMainList.start();
    }

    @Override
    protected void onDestroy() {
        // 销毁悬浮窗
//        Intent intent = new Intent(MainActivity.this, FloatService.class);
        //终止FloatViewService
//        stopService(intent);
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        eventBus.unregister(this);
        mMapView.onDestroy();

    }

    class BannerViewHolder implements MZViewHolder<String> {

        //图片
        private ImageView ivMainImage = null;

        //景点名称
        private TextView tvMainName = null;

        //查看详情
        private LinearLayout llMainDetails = null;

        //播放声音
        private RelativeLayout rlMainPlay = null;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_main_banner, null);
            ivMainImage = view.findViewById(R.id.iv_main_image);
            tvMainName = view.findViewById(R.id.tv_main_name);
            llMainDetails = view.findViewById(R.id.ll_main_details);
            rlMainPlay = view.findViewById(R.id.rl_main_play);
            return view;
        }

        @Override
        public void onBind(Context context, int i, String s) {
            imageLoader.displayImage(s, ivMainImage, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
        }
    }

    /**
     * 退出
     *
     * @param c
     */
    public void onEventMainThread(ExitFind c) {
        finish();
        aCache.clear();
        exit();
    }

    /**
     * 扫描到ble后但已经选择路线
     *
     * @param b
     */
    public void onEventMainThread(BleFind b) {
        direState.setBleList(b.bleList);
        for (String ble : b.bleList) {
            Log.e(TAG, "---扫描到不重复蓝牙地址----" + ble);
        }
    }

    /**
     * 扫描到蓝牙后但没选择线路的情况
     *
     * @param b
     */
    public void onEventMainThread(BleOneFind b) {
        position = b.position;
    }

    /**
     * 关闭蓝牙
     *
     * @param close
     */
    public void onEventMainThread(BleCloseFind close) {
        exit();
    }

    /**
     * 开启蓝牙
     *
     * @param open
     */
    public void onEventMainThread(BleOpenFind open) {
        checkBluetooth();
    }

    /**
     * 是否登录
     *
     * @param m
     */
    public void onEventMainThread(MainLoginFind m) {
        initIntent(LoginActivity.class);
    }
}
