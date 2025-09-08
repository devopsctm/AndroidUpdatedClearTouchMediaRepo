package com.hem.cleartouchmedia;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApplication extends Application {

    private static int foregroundActivities = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(Activity activity) {
                foregroundActivities++;
            }

            @Override
            public void onActivityResumed(Activity activity) {}

            @Override
            public void onActivityPaused(Activity activity) {}

            @Override
            public void onActivityStopped(Activity activity) {
                foregroundActivities--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {}
        });
    }

    public static boolean isAppInForeground() {
        return foregroundActivities > 0;
    }
}
