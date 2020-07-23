package ir.vasl.chatkitlight.database;

import android.app.Application;

import androidx.room.Room;

import ir.vasl.chatkitlight.utils.Constants;

public class DatabaseLayer {

    private static DatabaseLayer DatabaseLayer = null;
    private static Application application;
    private ChatKitDatabase chatKitDatabase;

    private DatabaseLayer() {
        chatKitDatabase = Room.databaseBuilder(application, ChatKitDatabase.class, Constants.DATABASE_NAME).allowMainThreadQueries().addMigrations(DbMigration.MIGRATION_1_2).build();
        DatabaseLayer = this;
    }

    public static DatabaseLayer getInstance(Application app) {
        application = app;
        if (DatabaseLayer == null)
            DatabaseLayer = new DatabaseLayer();
        return DatabaseLayer;
    }

    public ChatKitDatabase getChatKitDatabase() {
        return chatKitDatabase;
    }
}