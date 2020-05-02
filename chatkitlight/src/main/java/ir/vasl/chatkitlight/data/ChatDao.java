package ir.vasl.chatkitlight.data;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import ir.vasl.chatkitlight.model.ConversationModel;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chatkit_db")
    DataSource.Factory<Integer, ConversationModel> getAll();

    @Query("SELECT * FROM chatkit_db")
    List<ConversationModel> getAllSimple();

    //@Insert ...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConversationModel sample);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ConversationModel> sample);

    //@Delete ...

}