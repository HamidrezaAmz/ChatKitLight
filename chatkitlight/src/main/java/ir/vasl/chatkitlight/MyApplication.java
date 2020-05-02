package ir.vasl.chatkitlight;

import android.app.Application;

public class MyApplication extends Application {

    static Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Application getContext() {
        return context;
    }
}
