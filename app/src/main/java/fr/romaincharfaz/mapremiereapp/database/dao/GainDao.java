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

    @Query("SELECT * FROM Gain WHERE userId= :userId")
    LiveData<List<Gain>> getLiveGains(String userId);

    @Query("SELECT * FROM Gain WHERE userId= :userId")
    List<Gain> getGains(String userId);

    @Insert
    void insertGains(Gain gain);

    @Update
    void updateGain(Gain gain);

    @Query("DELETE FROM Gain WHERE id= :gainId")
    void deleteGain(long gainId);

    @Query("DELETE FROM Gain WHERE userId= :userId")
    void deleteLivretGains(String userId);

    @Query("DELETE FROM Gain")
    void deleteAllGains();
}
