package com.cdqf.dire_floatball;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cdqf.dire.R;
import com.cdqf.dire_activity.PosiListActivity;
import com.cdqf.dire_class.BlePosition;
import com.cdqf.dire_class.PosiList;
import com.cdqf.dire_class.PosiListFind;
import com.cdqf.dire_find.FloatBleFind;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectPreferences;

import de.greenrobot.event.EventBus;

public class FloatService extends Service {
    private static final String TAG = "FloatService";

    private DireState direState = DireState.getDireState();

    //定义浮动窗口布局
    private View mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private ImageView mFloatView;

    private EventBus eventBus = EventBus.getDefault();

    //屏幕宽度
    private int width = 0;

    //蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;

    //扫描
    private BluetoothAdapter.LeScanCallback blLeScanCallback = null;

    private Context context = null;

    private boolean isFloast = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        context = this;
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        width = direState.getDisPlyWidth(getApplicationContext());
        createFloatView();
    }

    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    private void createFloatView() {
        isFloast = true;
        wmParams = new WindowManager.LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        if (Build.VERSION.SDK_INT == 23) {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 152;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = inflater.inflate(R.layout.float_winds, null);
        //添加mFloatLayout
        Log.e(TAG, "添加");
        mWindowManager.addView(mFloatLayout, wmParams);

//        //浮动窗口按钮
        mFloatView = mFloatLayout.findViewById(R.id.tv_winds);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            boolean isClick;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        mFloatView.setBackgroundResource(R.drawable.ic_launcherc);
                        isClick = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isClick = true;
                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        wmParams.x = (int) event.getRawX()
                                - mFloatView.getMeasuredWidth() / 2;
                        // 减25为状态栏的高度
                        wmParams.y = (int) event.getRawY()
                                - mFloatView.getMeasuredHeight() / 2 - 75;
                        Log.e(TAG, "---x---" + wmParams.x + "---y---" + wmParams.y);

                        // 刷新
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        return true;
                    case MotionEvent.ACTION_UP:
//                        mFloatView.setBackgroundResource(R.drawable.circle_cyan);
                        if (wmParams.x <= (width / 2)) {
                            wmParams.x = 0;
                        } else {
                            wmParams.x = width;
                        }
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        return isClick;// 此处返回false则属于移动事件，返回true则释放事件，可以出发点击否。

                    default:
                        break;
                }
                return false;
            }
        });

        mFloatView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(FloatService.this, PositionActivity.class);
                Intent intent = new Intent(FloatService.this, PosiListActivity.class);
                startActivity(intent);
                if (mFloatLayout != null) {
                    //移除悬浮窗口
                    mWindowManager.removeViewImmediate(mFloatLayout);
                    isFloast = false;
                }
            }
        });
    }

//    /**
//     * 推送给用户，告诉用户当前所在景点名称
//     *
//     * @param name
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void notification(String name) {
//        String title = "箭向系统提示您";
//        String context = "您目前所在的景点为" + name;
//        NotificationManager managerCompat = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (managerCompat.areNotificationsEnabled()) {
//            Log.e(TAG, "---权限打开---");
//        } else {
//            Log.e(TAG, "---权限关闭---");
//        }
//        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            //只在Android O之上需要渠道
//            NotificationChannel notificationChannel = new NotificationChannel("channelid1", "channelname", NotificationManager.IMPORTANCE_HIGH);
//            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
//            managerCompat.createNotificationChannel(notificationChannel);
//        }
//        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(FloatService.this, "通知");
//        notiBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        notiBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                R.mipmap.ic_launcher));
//        notiBuilder.setContentTitle(title);
//        notiBuilder.setContentText(context);
//        notiBuilder.setWhen(System.currentTimeMillis());
//        notiBuilder.setOngoing(false);
//        notiBuilder.setAutoCancel(true);
//        notiBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
//        notiBuilder.setChannelId(getApplicationContext().getPackageName());
//        managerCompat.notify(0x12, notiBuilder.build());
//        Log.e(TAG, "---开启通知---");
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel channel = new NotificationChannel(
////                    FloatService.class.getPackage().getName(),
////                    TAG,
////                    NotificationManager.IMPORTANCE_DEFAULT
////            );
////            notifyManager.createNotificationChannel(channel);
////        }
//    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "---销毁---");
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeViewImmediate(mFloatLayout);
        }
        eventBus.unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 开始扫描蓝牙
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onEventMainThread(FloatBleFind o) {
        Log.e(TAG, "---获取的蓝牙地址---" + o.ble);
        for (BlePosition ble : direState.getBlePositionList()) {
            if (TextUtils.equals(ble.getBle(), o.ble)) {
                Log.e(TAG, "---当前地址---" + ble.getBle() + "---当前所属景区---" + ble.getBleName());
                direState.setAddress(ble.getBle());
                direState.setAddressName(ble.getBleName());
                direState.initToast(FloatService.this, "您当前所在景点为" + ble.getBleName(), false, 0);
                //保存当前节点
                for (PosiList posi : direState.getPosiListList()) {
                    if (TextUtils.equals(posi.getBle(), ble.getBle())) {
                        direState.getPosiListList().remove(posi);
                        break;
                    }
                }
                PosiList posiList = new PosiList(ble.getBleName(), ble.getBle());
                direState.getPosiListList().add(posiList);
                DirectPreferences.setPosiList(context, direState.getPosiListList());
                eventBus.post(new PosiListFind());
                break;
            }
        }

    }

    /**
     * 打开浮球
     *
     * @param o
     */
    public void onEventMainThread(OpenFind o) {
        if (mFloatLayout != null&&isFloast) {
            //移除悬浮窗口
            mWindowManager.removeViewImmediate(mFloatLayout);
        }
        createFloatView();
    }

    /**
     * 关闭浮球
     *
     * @param c
     */
    public void onEventMainThread(CloseFind c) {
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeViewImmediate(mFloatLayout);
            isFloast = false;
        }
    }
}
