package ir.vasl.chatkitlight.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.utils.Constants;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE chatId=:chatIdValue ORDER BY time DESC")
    DataSource.Factory<Integer, ConversationModel> getAll(String chatIdValue);

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE chatId=:chatIdValue ORDER BY id DESC")
    List<ConversationModel> getAllSimple(String chatIdValue);

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE chatId=:chatIdValue AND conversationId=:conversationIdValue")
    ConversationModel getConversation(String chatIdValue, String conversationIdValue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConversationModel conversationModel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIgnore(ConversationModel conversationModel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIgnore(List<ConversationModel> conversationModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ConversationModel> conversationModels);


    @Update
    void update(ConversationModel model);

    @Query("UPDATE " + Constants.TABLE_NAME + " SET "
            + " conversationId=:conversationIDNew "
            + " WHERE conversationId =:conversationIDOld")
    void swapId(String conversationIDOld, String conversationIDNew);

    @Query("UPDATE " + Constants.TABLE_NAME + " SET "
            + " conversationId=:conversationIDNew "
            + " WHERE conversationId =:conversationIDOld AND chatId=:chatID")
    void swapId(String conversationIDOld, String conversationIDNew, String chatID);

    @Delete
    void delete(ConversationModel conversationModel);

    @Query("DELETE FROM " + Constants.TABLE_NAME + " WHERE conversationId =:conversationId")
    void delete(String conversationId);

    @Query("DELETE FROM " + Constants.TABLE_NAME)
    void deleteAll();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert2(ConversationModel obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert2(List<ConversationModel> obj);

    @Update
    int update2(ConversationModel obj);

    @Update
    void update2(List<ConversationModel> obj);

    @Delete
    void delete2(ConversationModel obj);


}