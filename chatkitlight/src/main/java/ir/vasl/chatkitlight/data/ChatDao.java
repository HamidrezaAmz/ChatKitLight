package ir.vasl.chatkitlight.data;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ir.vasl.chatkitlight.model.ConversationModel;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chatkit_db WHERE id = 0")
    DataSource.Factory<Integer, ConversationModel> getAll2();

    @Query("SELECT * FROM chatkit_db")
    DataSource.Factory<Integer, ConversationModel> getAll();

    @Query("SELECT * FROM chatkit_db")
    List<ConversationModel> getAllSimple();

    //@Insert ...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConversationModel sample);

    @Update
    void update(ConversationModel model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ConversationModel> sample);

    @Delete
    void delete(ConversationModel sample);

}