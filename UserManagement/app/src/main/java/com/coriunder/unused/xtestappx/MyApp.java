package com.coriunder.unused.xtestappx;

import android.app.Application;

import com.coriunder.base.Coriunder;

/**
 * Created by 1 on 20.06.2016.
 */
public class MyApp extends Application {

    @Override
    public final void onCreate() {
        super.onCreate();
        Coriunder.init(getApplicationContext());
    }
}