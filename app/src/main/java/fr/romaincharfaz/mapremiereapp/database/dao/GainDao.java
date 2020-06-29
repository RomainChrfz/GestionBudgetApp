package fr.romaincharfaz.mapremiereapp.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.Gain;

@Dao
public interface GainDao {

    @Query("SELECT * FROM Gain WHERE userId= :userId AND usernameId= :usernameId")
    LiveData<List<Gain>> getLiveGains(long userId, String usernameId);

    @Query("SELECT * FROM Gain WHERE userId= :userId AND usernameId= :usernameId")
    List<Gain> getGains(long userId, String usernameId);

    @Insert
    void insertGains(Gain gain);

    @Update
    void updateGain(Gain gain);

    @Query("DELETE FROM Gain WHERE id= :gainId")
    void deleteGain(long gainId);

    @Query("DELETE FROM Gain WHERE userId= :userId AND usernameId= :usernameId")
    void deleteLivretGains(long userId, String usernameId);

    @Query("DELETE FROM Gain")
    void deleteAllGains();
}
