package com.zxon.notificationdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.zxon.notificationdemo.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 15/2/10.
 */
public class NotificationMonitor extends NotificationListenerService{

    private static final int EVENT_UPDATE_CURRENT_NOS = 0;
    public static final String ACTION_NLS_CONTROL = "com.seven.notificationlistenerdemo.NLSCONTROL";
    public static List<StatusBarNotification> mCurrentNotifications = new ArrayList<StatusBarNotification>();
    public static int mCurrentNotificationsCounts = 0;
    public static StatusBarNotification mPostedNotification;
    public static StatusBarNotification mRemovedNotification;

    @Override
    public void onCreate() {
        LogUtil.d("onCreate...");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("onStartCommand...");
        updateCurrentNotifications();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("onBind...");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        LogUtil.d("onNotificationPosted...");
        updateCurrentNotifications();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    private void updateCurrentNotifications() {
        //tickerText contentIntent
        mCurrentNotifications.clear();
        try {
            StatusBarNotification[] activeNotifications = getActiveNotifications();
            if (activeNotifications != null && activeNotifications.length != 0) {
                for (StatusBarNotification sbn : activeNotifications) {
                    mCurrentNotifications.add(sbn);
                }
            }
            mCurrentNotificationsCounts = activeNotifications.length;
        } catch (Exception e) {
            LogUtil.d("Should not be here!!");
            e.printStackTrace();
        }

        LogUtil.d("mCurrentNotificationsCounts is : " + mCurrentNotificationsCounts);
        LogUtil.d("mCurrentNotifications are of size : " + mCurrentNotifications.size());
        if (mCurrentNotifications.size() != 0) {
            for (StatusBarNotification sbn : mCurrentNotifications) {
                Notification n = sbn.getNotification();
                PendingIntent pi = n.contentIntent;
                if (pi == null) {
                    continue;
                }
                String notificationCreator = pi.getCreatorPackage();
                LogUtil.d("the package is : " + notificationCreator);
                if (notificationCreator.equals("com.tencent.mm")) {
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                    LogUtil.d("finish open the weixin");
                } else {
                    LogUtil.d("not weixin");
                }

            }
        }

    }

}
