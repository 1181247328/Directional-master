package com.cdqf.dire_state;

import android.content.Context;
import android.content.SharedPreferences;

import com.cdqf.dire_class.Live;
import com.cdqf.dire_class.User;
import com.cdqf.dire_class.UserLine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 保存
 */
public class DirectPreferences {
    private String TAG = DirectPreferences.class.getSimpleName();

    //用户信息
    private static final String LOG_USER = "dire_user";

    //用户的线路跟踪状态
    private static final String LIVE_USER = "Live_User";

    //当前用户正在进行的线路
    private static final String LINE_USER = "Line_User";

    private static Gson gson = new Gson();

    private static SharedPreferences seabedPreferences = null;

    private static SharedPreferences.Editor seabedEditor = null;

    /**
     * 保存用户的id,头像，昵称
     *
     * @param context
     * @param user
     */
    public static void setUser(Context context, User user) {
        seabedPreferences = context.getSharedPreferences(LOG_USER, 0);
        seabedEditor = seabedPreferences.edit();
        String loguser = gson.toJson(user);
        seabedEditor.putString("user", loguser);
        seabedEditor.commit();
    }

    public static User getUser(Context context) {
        seabedPreferences = context.getSharedPreferences(LOG_USER, 0);
        String logUser = seabedPreferences.getString("user", "");
        if (logUser.equals("")) {
            return null;
        }
        User user = gson.fromJson(logUser, User.class);
        return user;
    }

    /**
     * 删除登录用户的信息
     *
     * @param context
     */
    public static void clearUser(Context context) {
        seabedPreferences = context.getSharedPreferences(LOG_USER, 0);
        seabedEditor = seabedPreferences.edit().clear();
        seabedEditor.commit();
    }

    /**
     * 保存线路跟踪的状态
     *
     * @param context
     * @param userLine
     */
    public static void setUserLive(Context context, UserLine userLine) {
        seabedPreferences = context.getSharedPreferences(LIVE_USER, 0);
        seabedEditor = seabedPreferences.edit();
        String loguser = gson.toJson(userLine);
        seabedEditor.putString("live_user", loguser);
        seabedEditor.commit();
    }

    public static UserLine getUserLive(Context context) {
        seabedPreferences = context.getSharedPreferences(LIVE_USER, 0);
        String liveUser = seabedPreferences.getString("live_user", "");
        if (liveUser.equals("")) {
            return null;
        }
        UserLine user = gson.fromJson(liveUser, UserLine.class);
        return user;
    }

    /**
     * 删除路线跟踪状态
     *
     * @param context
     */
    public static void clearUserLive(Context context) {
        seabedPreferences = context.getSharedPreferences(LIVE_USER, 0);
        seabedEditor = seabedPreferences.edit().clear();
        seabedEditor.commit();
    }

    /**
     * 保存当前用户正在进行的线路
     *
     * @param context
     * @param liveList
     */
    public static void setUserLine(Context context, List<Live> liveList) {
        seabedPreferences = context.getSharedPreferences(LINE_USER, 0);
        seabedEditor = seabedPreferences.edit();
        String lineUser = gson.toJson(liveList, new TypeToken<List<Live>>() {
        }.getType());
        seabedEditor.putString("line_user", lineUser);
        seabedEditor.commit();
    }

    public static List<Live> getUserLine(Context context) {
        seabedPreferences = context.getSharedPreferences(LINE_USER, 0);
        String lineUser = seabedPreferences.getString("line_user", "");
        if (lineUser.equals("")) {
            return null;
        }
        List<Live> liveList = gson.fromJson(lineUser, new TypeToken<List<Live>>() {
        }.getType());
        return liveList;
    }

    /**
     * 删除当前
     *
     * @param context
     */
    public static void clearUserLine(Context context) {
        seabedPreferences = context.getSharedPreferences(LINE_USER, 0);
        seabedEditor = seabedPreferences.edit().clear();
        seabedEditor.commit();
    }
}
