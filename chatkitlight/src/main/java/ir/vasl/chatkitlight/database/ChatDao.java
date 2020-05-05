package ir.vasl.chatkitlight.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.utils.Constants;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE chatId=:chatIdValue")
    DataSource.Factory<Integer, ConversationModel> getAll(String chatIdValue);

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE chatId=:chatIdValue AND conversationId=:conversationIdValue")
    ConversationModel getConversation(String chatIdValue, String conversationIdValue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConversationModel conversationModel);

    @Update
    void update(ConversationModel model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ConversationModel> conversationModels);

    @Delete
    void delete(ConversationModel conversationModel);

    @Query("DELETE FROM " + Constants.TABLE_NAME)
    void deleteAll();

}