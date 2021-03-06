package com.example.mikol.portfolio;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class BoundService extends Service {

    private String customMessage;

    private Toast toast;
    private Timer timer;
    private TimerTask timerTask;

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
                showToast("Adding a project");
        }
    }

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        BoundService getMyService() {
            return BoundService.this;
        }
    }


    private void showToast(String text) {
        toast.setText(text);
        toast.show();
    }

    private void writeToLogs(String message) {
        Log.d("HelloServices", message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        writeToLogs("Called onCreate() method.");
        timer = new Timer();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeToLogs("Called onStartCommand() methond");
        clearTimerSchedule();
        initTask();
        timer.scheduleAtFixedRate(timerTask, 10*1000, 10 * 1000);
        showToast("Your bound service has been started");
        return super.onStartCommand(intent, flags, startId);
    }

    private void clearTimerSchedule() {
        if (timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {
        timerTask = new MyTimerTask();
    }

    @Override
    public void onDestroy() {
        writeToLogs("Called onDestroy() method");
        clearTimerSchedule();
        showToast("Your bound service has been stopped");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }
}
