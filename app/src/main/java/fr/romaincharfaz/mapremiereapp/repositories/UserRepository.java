package fr.romaincharfaz.mapremiereapp.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.database.accountDatabase;
import fr.romaincharfaz.mapremiereapp.database.dao.UserDao;
import fr.romaincharfaz.mapremiereapp.model.User;

public class UserRepository {
    private UserDao userDao ;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        accountDatabase database = accountDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getUsers();
    }

    // --- GET USER(S) ---
    public User getUser(String userId) { return this.userDao.getUser(userId); }

    public LiveData<User> getLiveUser(String userId) { return this.userDao.getLiveUser(userId); }

    public LiveData<List<User>> getUsers() { return allUsers; }

    public String getPassword(String userId) { return this.userDao.getPassword(userId); }

    // --- CREATE USER ---
    public void createUser(User user) {
        new InsertUserAsync(userDao).execute(user);
    }

    // --- UPDATE USER ---
    public void updateUser(User user) { new UpdateUserAsync(userDao).execute(user); }

    private static class InsertUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.createUser(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.updateUser(users[0]);
            return null;
        }
    }

}
