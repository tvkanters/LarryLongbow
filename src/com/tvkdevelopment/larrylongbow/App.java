package com.tvkdevelopment.larrylongbow;

import android.app.Application;
import android.content.Context;

/**
 * Helper class to get the context.
 */
public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        App.sContext = getApplicationContext();
    }

    public static Context getContext() {
        return App.sContext;
    }

}
