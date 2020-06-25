package fr.romaincharfaz.mapremiereapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM User WHERE username= :userId")
    LiveData<User> getLiveUser(String userId);

    @Query("SELECT * FROM User WHERE username= :userId")
    User getUser(String userId);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getUsers();

    @Query("SELECT password FROM User WHERE username= :userId")
    String getPassword(String userId);

}
