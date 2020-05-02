package ir.vasl.chatkitlight.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ir.vasl.chatkitlight.model.ConversationModel;

@Database(entities = {ConversationModel.class}, version = 1, exportSchema = false)
public abstract class DBInterface extends RoomDatabase {
    public abstract ChatDao chat();

}
