package ir.vasl.samplechatkit;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setLocale();
    }

    private void setLocale() {
        LocaleHelper.setApplicationLanguage(this);
    }
}
