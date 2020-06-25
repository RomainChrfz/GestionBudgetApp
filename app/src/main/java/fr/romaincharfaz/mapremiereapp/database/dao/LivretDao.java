package fr.romaincharfaz.mapremiereapp.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.Livret;

@Dao
public interface LivretDao {

    @Query("SELECT * FROM Livret")
    LiveData<List<Livret>> getLivrets();

    @Query("SELECT * FROM Livret WHERE userId= :username")
    LiveData<List<Livret>> getUserLivrets(String username);

    @Insert
    void createLivret(Livret livret);

    @Update
    void updateLivret(Livret livret);

    @Query("DELETE FROM Livret WHERE name= :livretname")
    void deleteLivret(String livretname);

}
