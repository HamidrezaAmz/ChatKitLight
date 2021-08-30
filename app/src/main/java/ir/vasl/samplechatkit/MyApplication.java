package ir.vasl.samplechatkit;

import android.app.Application;

import ir.vasl.samplechatkit.helper.LocaleHelper;

public class MyApplication extends Application {

    static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        setLocale();
    }

    public static Application getApp(){
        return app;
    }

    private void setLocale() {
        LocaleHelper.setApplicationLanguage(this);
    }
}
