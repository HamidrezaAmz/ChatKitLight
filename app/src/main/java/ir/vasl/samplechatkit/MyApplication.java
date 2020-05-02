package ir.vasl.samplechatkit;

import android.app.Application;

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
