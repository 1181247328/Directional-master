package com.cdqf.dire_state;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cdqf.dire_floatball.OpenFind;

import de.greenrobot.event.EventBus;

/**
 * 继承FragmentActivity
 * Created by admin on 2016/4/19.
 */
public class BaseActivity extends FragmentActivity {

    private static String TAG = BaseActivity.class.getSimpleName();
    //实例化activity管理器
    private ActivityManager manager = ActivityManager.getActivityManager(this);

    private EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将哪个activity保存进管理器
        manager.putActivity(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "---恢复---");
        eventBus.post(new OpenFind());
    }

    /**
     * 销毁操作时删除掉所有的activity直接退出程序
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除操作
        manager.removeActivity(this);
    }

    /**
     * 通过它来触发删除所有的actvity
     */
    public void exit() {
        manager.exit();
    }

    public void remove(Activity activity) {
        manager.removeActivity(activity);
    }

    public void remove(String activity) {
        manager.removeActivity(activity);
    }
}
