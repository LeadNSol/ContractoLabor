package com.leadn.contractolabor.utils.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.leadn.contractolabor.utils.AppContextHelper;


public class SharedPreferenceHelper implements ISharedPreferenceHelper {
    private static final String PREF_NAME = "PREF_NAME";
    private static final String PREF_USER_NAME = "PREF_USER_NAME";
    private static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    private static final String PREF_USER_SEQ_ID = "PREF_USER_SEQ_ID";
    private static final String PREF_PHONE_NUMBER = "PREF_PHONE_NUMBER";
    private static final String PREF_IMAGE_URL = "PREF_IMAGE_URL";
    private static final String PREF_PASSWORD = "PREF_PASSWORD";
    private static final String PREF_USER_LOGGED_IN_DATA = "PREF_USER_LOGGED_IN_DATA";

    private final SharedPreferences mPreferences;

    private static SharedPreferenceHelper mHelper;

    public static SharedPreferenceHelper getHelper() {
        if (mHelper == null) {
            synchronized (SharedPreferenceHelper.class) {
                mHelper = new SharedPreferenceHelper();
            }
        }
        return mHelper;
    }

    private SharedPreferenceHelper() {
        this.mPreferences = AppContextHelper.getAppContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        mPreferences.edit().putString(PREF_PHONE_NUMBER, phoneNumber).apply();
    }

    @Override
    public String getPhoneNumber() {
        return mPreferences.getString(PREF_PHONE_NUMBER, null);
    }

    @Override
    public void clearPreference() {
        mPreferences.edit().clear().apply();
    }

    @Override
    public void setUserLoggedInData(String userLoggedInData) {
        mPreferences.edit().putString(PREF_USER_LOGGED_IN_DATA, userLoggedInData).apply();
    }

    @Override
    public String getUserLoggedInData() {
        return mPreferences.getString(PREF_USER_LOGGED_IN_DATA, null);
    }


}
