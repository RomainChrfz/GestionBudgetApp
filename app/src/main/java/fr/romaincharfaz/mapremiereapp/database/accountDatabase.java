package fr.romaincharfaz.mapremiereapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.romaincharfaz.mapremiereapp.database.dao.GainDao;
import fr.romaincharfaz.mapremiereapp.database.dao.LivretDao;
import fr.romaincharfaz.mapremiereapp.database.dao.UserDao;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.model.User;

@Database(entities = {Gain.class , Livret.class, User.class}, version = 6, exportSchema = false)
public abstract class accountDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile accountDatabase INSTANCE;

    // --- DAO ---
    public abstract GainDao gainDao();
    public abstract UserDao userDao();
    public abstract LivretDao livretDao();

    // --- INSTANCE ---
    public static accountDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (accountDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), accountDatabase.class, "MyDatabase.db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

}
