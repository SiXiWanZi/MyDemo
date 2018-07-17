package com.whut.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/06/27
 *     desc   :
 * </pre>
 */
public class SharedPref implements ICache {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static String SP_NAME = "config";
    private static SharedPref instance;

    public static void init(String spName) {
        if(TextUtils.isEmpty(spName)) {
            SP_NAME = spName;
        }

    }

    private SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        editor = sharedPreferences.edit();
    }

    public static SharedPref getInstance(Context context) {
        if(instance == null) {
            Class var1 = SharedPref.class;
            synchronized(SharedPref.class) {
                if(instance == null) {
                    instance = new SharedPref(context.getApplicationContext());
                }
            }
        }

        return instance;
    }

    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void clear() {
        editor.clear().apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value.longValue());
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value.booleanValue());
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public Object get(String key) {
        return null;
    }

    public void put(String key, Object value) {
    }
}
