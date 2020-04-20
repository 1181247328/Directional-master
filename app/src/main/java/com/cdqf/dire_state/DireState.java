package com.cdqf.dire_state;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.cdqf.dire.R;
import com.cdqf.dire_city.OrderMail;
import com.cdqf.dire_class.Ble;
import com.cdqf.dire_class.BlePosition;
import com.cdqf.dire_class.Cement;
import com.cdqf.dire_class.Choice;
import com.cdqf.dire_class.DetailsGame;
import com.cdqf.dire_class.Live;
import com.cdqf.dire_class.Medal;
import com.cdqf.dire_class.Period;
import com.cdqf.dire_class.Point;
import com.cdqf.dire_class.PosiList;
import com.cdqf.dire_class.Route;
import com.cdqf.dire_class.Scenic;
import com.cdqf.dire_class.Team;
import com.cdqf.dire_class.User;
import com.cdqf.dire_class.UserLine;
import com.cdqf.dire_class.UserLive;
import com.cdqf.dire_class.Venues;
import com.cdqf.dire_dilog.LocationDilogFragment;
import com.cdqf.dire_service.Province;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 状态层
 * Created by liu on 2017/7/14.
 */

public class DireState {

    private String TAG = DireState.class.getSimpleName();

    //中间层
    private static DireState direState = new DireState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private List<Fragment> fragments = new CopyOnWriteArrayList<Fragment>();


    public static DireState getDireState() {
        return direState;
    }

    private Map<Integer, Boolean> framentsMap = new HashMap<Integer, Boolean>();


    private XProgressDialog xProgressDialog = null;

    //头像
    private Bitmap headBitmap = null;

    private boolean isPrssor = true;

    //省市区
    private List<Province> provinceList = new CopyOnWriteArrayList<Province>();

    private List<OrderMail> orderMailList = new CopyOnWriteArrayList<OrderMail>();

    //支付方式
    private int pay = -1;

    //会馆列表
    private List<Venues> venuesList = new CopyOnWriteArrayList<>();

    //会馆中路线列表
    private List<Route> routeList = new CopyOnWriteArrayList<>();

    //会馆中节点路线
    private List<Live> liveList = new CopyOnWriteArrayList<>();

    //节点中题目
    private List<Choice> choiceList = new CopyOnWriteArrayList<>();

    //用户是否登录
    private boolean isLogin = false;

    //用户
    private User user = new User();

    //省
    private List<String> options1Items = new ArrayList<>();

    //市
    private List<List<String>> options2Items = new ArrayList<>();

    //区
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private BluetoothAdapter mBluetoothAdapter = null;

    private Map<Integer, Ble> bleMapList = new HashMap<>();

    private Map<Integer, Boolean> titleList = new HashMap<>();

    //是否选择了路线
    private boolean isRoute = false;

    //是否同意了用户协议
    private boolean isAgree = false;

    public List<String> bleList = new CopyOnWriteArrayList<>();

    //当前用户所在的位置
    private int routePosition = -1;

    //当前线路是否通关
    private boolean isCustoms = false;

    private UserLine userLine = new UserLine();

    //用户线路
    private List<UserLive> userLiveList = new CopyOnWriteArrayList<>();

    //个人队伍
    private List<Team> teamList = new CopyOnWriteArrayList<>();

    //队员名单
    private List<String> playersList = new CopyOnWriteArrayList<>();

    private Team team = new Team();

    //搜索
    private List<Venues> searchList = new CopyOnWriteArrayList<>();

    //赛事活动
    private List<Cement> cementList = new CopyOnWriteArrayList<>();

    //活动点标
    private List<Point> pointList = new CopyOnWriteArrayList<>();

    //是否进入了欢迎页0=欢迎页1=正常页
    private int welcome = 0;

    //保存经纬度
    private LatLonPoint latLonPoint = null;

    //发现活动
    private List<Period> periodList = new CopyOnWriteArrayList<>();

    //发现景区
    private List<Scenic> scenicList = new CopyOnWriteArrayList<>();

    //活动详情
    private DetailsGame detailsGame = new DetailsGame();

    private String city = "";

    //勋章
    private List<Medal> medalList = new CopyOnWriteArrayList<>();

    //蓝牙所对应的景区
    private List<BlePosition> blePositionList = new CopyOnWriteArrayList<>();

    //确保当前所在景区的蓝牙地址
    private String address = "";

    //当前所在景区的名称
    private String addressName = "";

    //经过的景区
    private List<PosiList> posiListList = new CopyOnWriteArrayList<>();

    /**
     * 提示信息
     *
     * @param context
     * @param toast
     */
    public void initToast(Context context, String toast, boolean isShort, int type) {
        Toast initToast = null;
        if (isShort) {
            initToast = Toast.makeText(context, toast, Toast.LENGTH_SHORT);
        } else {
            initToast = Toast.makeText(context, toast, Toast.LENGTH_LONG);
        }

        switch (type) {
            case 0:
                break;
            //显示中间
            case 1:
                initToast.setGravity(Gravity.CENTER, 0, 0);
                initToast.show();
                break;
            //顶部显示
            case 2:
                initToast.setGravity(Gravity.TOP, 0, 0);
                break;
        }
        initToast.show();
    }

    public void initToast(Context context, String toast, boolean isShort, int type, int timer) {
        Toast initToast = null;
        if (isShort) {
            initToast = Toast.makeText(context, toast, Toast.LENGTH_SHORT);
        } else {
            initToast = Toast.makeText(context, toast, timer);
        }

        switch (type) {
            case 0:
                break;
            //显示中间
            case 1:
                initToast.setGravity(Gravity.CENTER, 0, 0);
                initToast.show();
                break;
            //顶部显示
            case 2:
                initToast.setGravity(Gravity.TOP, 0, 0);
                break;
        }
        initToast.show();
    }

    public String getPlantString(Context context, int resId) {
        return context.getResources().getString(resId);
    }


    /**
     * @param loading 加载图片时的图片
     * @param empty   没图片资源时的默认图片
     * @param fail    加载失败时的图片
     * @return
     */
    public DisplayImageOptions getImageLoaderOptions(int loading, int empty, int fail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loading)
                .showImageForEmptyUri(empty)
                .showImageOnFail(fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return options;
    }

    /**
     * 为头像而准备
     *
     * @param loading
     * @param empty
     * @param fail
     * @return
     */
    public DisplayImageOptions getHeadImageLoaderOptions(int loading, int empty, int fail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loading)
                .showImageForEmptyUri(empty)
                .showImageOnFail(fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        return options;
    }

    /**
     * 保存图片的配置
     *
     * @param context
     * @param cache   "imageLoaderworld/Cache"
     */
    public ImageLoaderConfiguration getImageLoaderConfing(Context context, String cache) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, cache);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100)
                .discCache(new UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                .writeDebugLogs()
                .build();
        return config;
    }

    /**
     * 初始化imageLoad
     *
     * @param context
     * @return
     */
    public ImageLoaderConfiguration getConfiguration(Context context) {
        ImageLoaderConfiguration configuration = getImageLoaderConfing(context, "imageLoaderword/Chace");
        return configuration;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本有错";
        }
    }

    /**
     * 权限
     */
    public void permission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CALL_PHONE,},
                        0
                );
            }
        }
    }

    /**
     * webView处理
     *
     * @param wvDrunkCarrier
     */
    public WebSettings webSettings(WebView wvDrunkCarrier) {
        WebSettings wsDrunkCarrier = wvDrunkCarrier.getSettings();
        //自适应屏幕
        wsDrunkCarrier.setUseWideViewPort(true);
        wsDrunkCarrier.setLoadWithOverviewMode(true);
        //支持网页放大缩小
        wsDrunkCarrier.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wsDrunkCarrier.setUseWideViewPort(true);
        wsDrunkCarrier.setLoadWithOverviewMode(true);
        wsDrunkCarrier.setSavePassword(true);
        wsDrunkCarrier.setSaveFormData(true);
        wsDrunkCarrier.setJavaScriptEnabled(true);
        wsDrunkCarrier.setGeolocationEnabled(true);
        wsDrunkCarrier.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        wsDrunkCarrier.setDomStorageEnabled(true);
//        wsDrunkCarrier.setBuiltInZoomControls(true);
//        wsDrunkCarrier.setSupportZoom(true);
//        wsDrunkCarrier.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //把图片加载放在最后来加载
        wsDrunkCarrier.setBlockNetworkImage(false);
        //可以加载javascript
        wsDrunkCarrier.setJavaScriptEnabled(true);
        //设置缓存模式
        wsDrunkCarrier.setAppCacheEnabled(true);
        wsDrunkCarrier.setAllowFileAccess(true);
        wsDrunkCarrier.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启 DOM storage API 功能
        wsDrunkCarrier.setDomStorageEnabled(true);
        //开启 database storage API 功能
        wsDrunkCarrier.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        wsDrunkCarrier.setAppCacheEnabled(false);
        //是否调用jS中的代码
        wsDrunkCarrier.setJavaScriptEnabled(true);
        wsDrunkCarrier.setJavaScriptCanOpenWindowsAutomatically(true);
        wsDrunkCarrier.setAllowFileAccess(true);
        //支持多点触摸
        wsDrunkCarrier.setBuiltInZoomControls(false);
        wsDrunkCarrier.setDefaultTextEncodingName("UTF-8");
        //自动加载图片
        wsDrunkCarrier.setLoadsImagesAutomatically(true);
        wsDrunkCarrier.setLoadWithOverviewMode(true);
        wsDrunkCarrier.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wsDrunkCarrier.setUseWideViewPort(true);
        wsDrunkCarrier.setSaveFormData(true);
        wsDrunkCarrier.setSavePassword(true);
        return wsDrunkCarrier;
    }

    public ImageLoader getImageLoader(Context context) {
        imageLoader.init(getConfiguration(context));
        return imageLoader;
    }

    /**
     * 判断是不是手机号码
     *
     * @param str
     * @return
     */
    public boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("[1][3456789]\\d{9}"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }

    public boolean passwordJudge(Context context, String password, String passwordTwo) {
        if (password.length() <= 0) {
            initToast(context, "请输入新密码", true, 0);
            return false;
        }
        if (password.length() >= 17) {
            initToast(context, "密码长度不大于16", true, 0);
            return false;
        }
        if (passwordTwo.length() <= 0) {
            initToast(context, "请确认密码", true, 0);
            return false;
        }
        if (passwordTwo.length() >= 17) {
            initToast(context, "确认密码长度不大于16", true, 0);
            return false;
        }
        if (!TextUtils.equals(password, passwordTwo)) {
            initToast(context, "两次密码不一致", true, 0);
            return false;
        }
        return true;
    }

    /**
     * 将Bitmap保存在本地
     *
     * @param bitmap
     */
    public void saveBitmapFile(Bitmap bitmap, String uri) {
        File file = new File(uri);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一张图片
     *
     * @param context
     */
    public void headFail(Context context, String path) {
        direState.setHeadBitmap(null);
        if (TextUtils.isEmpty(path)) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            file.delete();
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public int getRandom() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码，11位数字，1开通，第二位数必须是3456789这些数字之一 *
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            // Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Pattern regex = Pattern.compile("^1[345789]\\d{9}$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;

        }
        return flag;
    }

    public void dialogProgress(final Context context, String Toast) {
        xProgressDialog = new XProgressDialog(context, Toast);
        xProgressDialog.show();
        isPrssor = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (xProgressDialog != null) {
                    if (isPrssor) {
                        initToast(context, "加载失败,请检查网络", true, 0);
                    }
                    xProgressDialog.dismiss();
                }
            }
        }, 6000);
    }

    public void dialogProgressClose() {
        if (xProgressDialog != null) {
            isPrssor = false;
            xProgressDialog.dismiss();
        }
    }

    /**
     * 判断是不是网络地址
     *
     * @param url
     * @return
     */
    public boolean isUrl(String url) {
        return url.indexOf("http://") != -1;
    }

    /**
     * 获取年月时分
     *
     * @param onDay
     * @param start
     * @param end
     * @return
     */
    public String getOnDay(String onDay, int start, int end) {
        return onDay.substring(start, end);
    }

    /**
     * 分享
     *
     * @param context
     * @param content
     */
    public void initShar(Context context, String content) {
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT, content);
        intent1.setType("text/plain");
        context.startActivity(Intent.createChooser(intent1, getPlantString(context, R.string.share)));
    }

    /**
     * 判断是否是正确的车牌号
     *
     * @param license
     * @return
     */
    public boolean licensePlate(String license) {
        // 验证规则
        String regEx = "^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(license);
        return matcher.matches();
    }

    public void dilog(FragmentActivity activity, String name, int position) {
        LocationDilogFragment locationDilogFragment = new LocationDilogFragment();
        locationDilogFragment.setPosition(position);
        locationDilogFragment.show(activity.getSupportFragmentManager(), name);
    }

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 手机号码隐藏中间
     */
    public String phoneEmpty(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 获取屏幕宽度
     */
    public int getDisPlyWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     */
    public int getDisPlyHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 将一个view转化为图片
     *
     * @param view
     */

    public String viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        Bitmap cachebmp = loadBitmapFromView(view);
        FileOutputStream fos;
        String imagePath = "";
        try {
            boolean isHasSDCard = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                File sdRoot = Environment.getExternalStorageDirectory();
                Log.e(TAG, sdRoot.toString());
                File file = new File(sdRoot, Calendar.getInstance().getTimeInMillis() + ".png");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else throw new Exception("创建文件失败!");
            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
        return imagePath;
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    /**
     * 当前高德地图的sha
     * @param context
     * @return
     */
    public static String sHA1(Context context) {
        try {
            PackageInfo info = null;
            try {
                info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 收集蓝牙地址所以应的景区
     *
     * @param context
     */
    public void bleAddrss(Context context) {
        Resources res = context.getResources();
        //蓝牙地址
        String[] ble = res.getStringArray(R.array.ble_address);
        //蓝牙地址所对应的景区
        String[] blePosition = res.getStringArray(R.array.ble_address_position);
        //保存在数组中
        for (int i = 0; i < ble.length; i++) {
            BlePosition bles = new BlePosition(ble[i], blePosition[i]);
            blePositionList.add(bles);
        }
    }

    /**
     * 界面设置状态栏字体颜色
     */
    public void changeStatusBarTextImgColor(Activity activity, boolean isBlack) {
        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public Map<Integer, Boolean> getFramentsMap() {
        return framentsMap;
    }

    public void setFramentsMap(Map<Integer, Boolean> framentsMap) {
        this.framentsMap = framentsMap;
    }

    public Bitmap getHeadBitmap() {
        return headBitmap;
    }

    public void setHeadBitmap(Bitmap headBitmap) {
        this.headBitmap = headBitmap;
    }

    public List<OrderMail> getOrderMailList() {
        return orderMailList;
    }

    public void setOrderMailList(List<OrderMail> orderMailList) {
        this.orderMailList = orderMailList;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getOptions1Items() {
        return options1Items;
    }

    public void setOptions1Items(List<String> options1Items) {
        this.options1Items = options1Items;
    }

    public List<List<String>> getOptions2Items() {
        return options2Items;
    }

    public void setOptions2Items(List<List<String>> options2Items) {
        this.options2Items = options2Items;
    }

    public List<List<List<String>>> getOptions3Items() {
        return options3Items;
    }

    public void setOptions3Items(List<List<List<String>>> options3Items) {
        this.options3Items = options3Items;
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public Map<Integer, Ble> getBleMapList() {
        return bleMapList;
    }

    public void setBleMapList(Map<Integer, Ble> bleMapList) {
        this.bleMapList = bleMapList;
    }

    public Map<Integer, Boolean> getTitleList() {
        return titleList;
    }

    public void setTitleList(Map<Integer, Boolean> titleList) {
        this.titleList = titleList;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public void setRoute(boolean route) {
        isRoute = route;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }

    public List<Venues> getVenuesList() {
        return venuesList;
    }

    public void setVenuesList(List<Venues> venuesList) {
        this.venuesList = venuesList;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public List<String> getBleList() {
        return bleList;
    }

    public void setBleList(List<String> bleList) {
        this.bleList = bleList;
    }

    public List<Live> getLiveList() {
        return liveList;
    }

    public void setLiveList(List<Live> liveList) {
        this.liveList = liveList;
    }

    public List<Choice> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<Choice> choiceList) {
        this.choiceList = choiceList;
    }

    public int getRoutePosition() {
        return routePosition;
    }

    public void setRoutePosition(int routePosition) {
        this.routePosition = routePosition;
    }

    public boolean isCustoms() {
        return isCustoms;
    }

    public void setCustoms(boolean customs) {
        isCustoms = customs;
    }

    public UserLine getUserLine() {
        return userLine;
    }

    public void setUserLine(UserLine userLine) {
        this.userLine = userLine;
    }

    public List<UserLive> getUserLiveList() {
        return userLiveList;
    }

    public void setUserLiveList(List<UserLive> userLiveList) {
        this.userLiveList = userLiveList;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public List<String> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<String> playersList) {
        this.playersList = playersList;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Venues> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<Venues> searchList) {
        this.searchList = searchList;
    }

    public List<Cement> getCementList() {
        return cementList;
    }

    public void setCementList(List<Cement> cementList) {
        this.cementList = cementList;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }

    public int getWelcome() {
        return welcome;
    }

    public void setWelcome(int welcome) {
        this.welcome = welcome;
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    public DetailsGame getDetailsGame() {
        return detailsGame;
    }

    public void setDetailsGame(DetailsGame detailsGame) {
        this.detailsGame = detailsGame;
    }

    public List<Scenic> getScenicList() {
        return scenicList;
    }

    public void setScenicList(List<Scenic> scenicList) {
        this.scenicList = scenicList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Medal> getMedalList() {
        return medalList;
    }

    public void setMedalList(List<Medal> medalList) {
        this.medalList = medalList;
    }

    public List<BlePosition> getBlePositionList() {
        return blePositionList;
    }

    public void setBlePositionList(List<BlePosition> blePositionList) {
        this.blePositionList = blePositionList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public List<PosiList> getPosiListList() {
        return posiListList;
    }

    public void setPosiListList(List<PosiList> posiListList) {
        this.posiListList = posiListList;
    }
}

