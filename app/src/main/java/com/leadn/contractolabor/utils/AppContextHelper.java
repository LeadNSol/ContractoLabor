package com.leadn.contractolabor.utils;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class AppContextHelper extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        MultiDex.install(this); // for less than 20
    }

    public static Context getAppContext() {
        return mContext;
    }
}
