package com.mahzadiran.persiancalendar;

import android.app.Application;

import com.mahzadiran.persiancalendar.util.Utils;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.initUtils(getApplicationContext());
    }
}
