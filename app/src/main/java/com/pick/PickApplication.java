package com.pick;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 *  Volley 삭제를 요구한다!
 */
public class PickApplication extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    public static Context getItemContext() {
        return mContext;
    }
}
