package com.horadadiversao.util;

import android.app.ActivityManager;

import java.util.List;

/**
 * Created by $tenio nobre$ on 13/12/2015.
 */
public class MusicActivityControl {

    public static void stopMusic(Object systemService, String packageName) {

        ActivityManager activityManager = (ActivityManager) systemService;
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(packageName)) {
            isActivityFound = true; // Activity belongs to your app is in foreground.
        }

        if (!isActivityFound) {
            BackgroundSoundService.pauseMusic();
        }
    }


}
