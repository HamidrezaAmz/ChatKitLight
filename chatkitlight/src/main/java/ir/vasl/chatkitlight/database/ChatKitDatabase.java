package ir.vasl.chatkitlight.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ir.vasl.chatkitlight.model.ConversationModel;

@Database(entities = {ConversationModel.class}, version = 7, exportSchema = false)
public abstract class ChatKitDatabase extends RoomDatabase {
    public abstract ChatDao getChatDao();
}
