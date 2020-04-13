package com.cdqf.dire.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cdqf.dire_state.DireState;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * QQ登录
 * Created by liu on 2017/7/19.
 */

public class QQLogin {

    private static String TAG = QQLogin.class.getSimpleName();

    public static Tencent mTencent;

    public static IUiListener loginListener;

    private static IUiListener userInfoListener;

    public static IUiListener shareiUiListener;

    public static IUiListener shareImageiUiListener;

    public static IUiListener shareZoneiUiListener;

    private static String openID;

    private static DireState direState = DireState.getDireState();

    private static EventBus eventBus = EventBus.getDefault();

    /***
     * 特别注意一点,特别注意一点,特别注意一点
     * 在你调用的activity下要加入以下这段代码
     * @Override
     *     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     *         if (requestCode == Constants.REQUEST_LOGIN) {
     *             Tencent.onActivityResultData(requestCode, resultCode, data,QQLogin.loginListener);
     *         }
     *         super.onActivityResult(requestCode, resultCode, data);
     *     }
     */

    /**
     * QQ登录
     *
     * @param context
     * @param activity
     */
    public static void qqLogin(final Context context, final Activity activity) {
        if (!isQQClientAvailable(context)) {
            Toast.makeText(context, "请先安装QQ", Toast.LENGTH_SHORT).show();
            return;
        }
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, context);
        //创建QQ登录回调接口
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //登录成功后调用的方法
                JSONObject jo = (JSONObject) o;
                Log.e(TAG, jo.toString());

                try {
                    openID = jo.getString("openid");
                    String accessToken = jo.getString("access_token");
                    String expires = jo.getString("expires_in");
                    Log.e(TAG, "---openID---" + openID);
                    mTencent.setOpenId(openID);
                    mTencent.setAccessToken(accessToken, expires);
                    //获取用户信息
                    QQToken qqToken = mTencent.getQQToken();
                    UserInfo userInfo = new UserInfo(activity, qqToken);
                    userInfo.getUserInfo(userInfoListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                //登录失败后回调该方法
                Log.e("LoginError:", uiError.toString());
                direState.initToast(context, "登录失败,请重新登录", true, 0);
            }

            @Override
            public void onCancel() {
                //取消登录后回调该方法
                direState.initToast(context, "取消登录", true, 0);
            }
        };
        userInfoListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (o == null) {
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) o;
                    Log.e(TAG, "---用户信息---" + jo.toString());
                    String nickName = jo.getString("nickname");
                    String headImage = jo.getString("figureurl_qq_2").replace("\"", "");
                    //成功登录后要注销QQ登录
                    mTencent.logout(activity);
                    eventBus.post(new QQFind(openID, nickName, headImage));
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        };
        mTencent.login(activity, Constants.QQ_SCOPE, loginListener);
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    //QQ分享

    /**
     * @param activity
     * @param targetUrl 这条分享消息被好友点击后的跳转URL
     * @param title     分享的标题
     * @param imageUrl  分享的图片URL
     * @param summary   分享的消息摘要，最长50个字
     */
    public static void qqShare(Activity activity, String targetUrl, String title, String imageUrl, String summary) {
        if (!isQQClientAvailable(activity)) {
            Toast.makeText(activity, "请先安装QQ", Toast.LENGTH_SHORT).show();
            return;
        }
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity);
        shareiUiListener = new IUiListener() {

            @Override
            public void onComplete(Object o) {
                Log.e(TAG, "---onComplete---");
            }

            @Override
            public void onError(UiError uiError) {
                Log.e(TAG, "---onError---" + uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "---onCancel---");
            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                imageUrl);
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        mTencent.shareToQQ(activity, bundle, shareiUiListener);
    }

    /**
     * 分享纯图片到QQ好友
     *
     * @param activity
     * @param imageUrl
     */
    public static void qqShare(Activity activity, String imageUrl) {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity);
        shareImageiUiListener = new IUiListener() {

            @Override
            public void onComplete(Object o) {
                Log.e(TAG, "---onComplete---");
            }

            @Override
            public void onError(UiError uiError) {
                Log.e(TAG, "---onError---" + uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "---onCancel---");
            }
        };
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);// 设置分享类型为纯图片分享
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);// 需要分享的本地图片URL
        mTencent.shareToQQ(activity, bundle, shareImageiUiListener);
    }

    /**
     * 分享到QQ空间
     *
     * @param activity
     * @param imageUrl
     */
    public static void qqShareZone(Activity activity,String title,String context,String imageUrl) {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity);
        shareZoneiUiListener = new IUiListener() {

            @Override
            public void onComplete(Object o) {
                Log.e(TAG, "---onComplete---");
            }

            @Override
            public void onError(UiError uiError) {
                Log.e(TAG, "---onError---" + uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "---onCancel---");
            }
        };
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);// 设置分享类型为纯图片分享
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 标题
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, context);// 摘要
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.qfgood.cn");// 内容地址
        ArrayList<String> imgUrlList = new ArrayList<>();
        imgUrlList.add(imageUrl);
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);//
        mTencent.shareToQzone(activity, bundle, shareZoneiUiListener);
    }
}
