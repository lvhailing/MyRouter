package com.ziroom.myrouter;

import android.app.Application;

import com.ziroom.routermiddleware.RouterMiddleware;

/**
 * Created by lvhl3 on 2017/12/15.
 * 类简单说明:
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        RouterMiddleware.getInstance().init(this);
        super.onCreate();
    }
}
