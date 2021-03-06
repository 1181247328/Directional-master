package com.cdqf.dire_floatball;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cdqf.dire.R;
import com.cdqf.dire_activity.PositionActivity;
import com.cdqf.dire_ble.BleService;
import com.cdqf.dire_ble.ParseUUID;
import com.cdqf.dire_find.FloatBleFind;
import com.cdqf.dire_state.DireState;

import java.util.List;
import java.util.UUID;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        width = direState.getDisPlyWidth(getApplicationContext());
        createFloatView();
    }

    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
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
        Log.e(TAG,"添加");
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
//                Toast.makeText(FloatService.this, "一百块都不给我！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FloatService.this, PositionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "---销毁---");
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
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
    public void onEventMainThread(FloatBleFind o) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetooth();
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

                        }
                    }
                }
            };

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.startLeScan(blLeScanCallback);
            }
        }
    }

    /**
     * 打开浮球
     *
     * @param o
     */
    public void onEventMainThread(OpenFind o) {
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
            mWindowManager.removeView(mFloatLayout);
        }
    }
}
