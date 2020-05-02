package ir.vasl.chatkitlight.data;

import android.app.Application;

import androidx.room.Room;

import ir.vasl.chatkitlight.MyApplication;

public class DBLayer {

    private static DBLayer DBLayer = null;
    private static Application application;
    private DBInterface db;

    private DBLayer() {
        db = Room.databaseBuilder(application, DBInterface.class, "chatkit_db").allowMainThreadQueries().build();
        DBLayer = this;
    }

    public static DBLayer getInstance(Application app) {
        application = app;
        if (DBLayer == null)
            DBLayer = new DBLayer();
        return DBLayer;
    }

    public DBInterface getDb() {
        return db;
    }
}