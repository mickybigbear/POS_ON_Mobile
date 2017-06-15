package com.example.sin.projectone;

import android.app.Application;
import android.content.Context;

/**
 * Created by nanth on 12/2/2016.
 */

public class ApplicationHelper extends Application {
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return applicationContext;
    }
}
