package fr.romaincharfaz.mapremiereapp.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.database.accountDatabase;
import fr.romaincharfaz.mapremiereapp.database.dao.GainDao;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.Livret;

public class GainRepository {
    private String username;
    private long id;
    private GainDao gainDao;
    private LiveData<List<Gain>> allGains;

    public GainRepository(Application application) {
        accountDatabase database = accountDatabase.getInstance(application);
        gainDao = database.gainDao();
        allGains = gainDao.getLiveGains(id,username);
    }

    // --- GET ---
    public LiveData<List<Gain>> getLiveGains(long userId, String usernameId) { return this.gainDao.getLiveGains(userId, usernameId); }

    public List<Gain> getGains(long userId, String usernameId) { return this.gainDao.getGains(userId, usernameId); }

    // --- CREATE ---
    public void createGain(Gain gain) {
        new InsertAsyncGain(gainDao).execute(gain);
    }

    // --- DELETE ---
    public void deleteLivretGains(Livret livret) { new DeleteLivretAsyncGain(gainDao).execute(livret); }

    public void deleteGain(Gain gain) {
        new DeleteAsyncGain(gainDao).execute(gain);
    }

    public void deleteAllGains() {
        new DeleteAllAsyncGain(gainDao).execute();
    }

    // --- UPDATE ---
    public void updateGain(Gain gain) {
        new UpdateAsyncGain(gainDao).execute(gain);
    }

    private static class InsertAsyncGain extends AsyncTask<Gain, Void, Void> {
        private GainDao gainDao;

        private InsertAsyncGain(GainDao gainDao) {
            this.gainDao = gainDao;
        }

        @Override
        protected Void doInBackground(Gain... gains) {
            gainDao.insertGains(gains[0]);
            return null;
        }
    }

    private static class DeleteAsyncGain extends AsyncTask<Gain, Void, Void> {
        private GainDao gainDao;

        private DeleteAsyncGain(GainDao gainDao) {
            this.gainDao = gainDao;
        }

        @Override
        protected Void doInBackground(Gain... gains) {
            gainDao.deleteGain(gains[0].getId());
            return null;
        }
    }

    private static class DeleteLivretAsyncGain extends AsyncTask<Livret, Void, Void> {
        private GainDao gainDao;

        private DeleteLivretAsyncGain(GainDao gainDao) {
            this.gainDao = gainDao;
        }

        @Override
        protected Void doInBackground(Livret... livrets) {
            gainDao.deleteLivretGains(livrets[0].getId(), livrets[0].getUserId());
            return null;
        }
    }

    private static class UpdateAsyncGain extends AsyncTask<Gain, Void, Void> {
        private GainDao gainDao;

        private UpdateAsyncGain(GainDao gainDao) {
            this.gainDao = gainDao;
        }

        @Override
        protected Void doInBackground(Gain... gains) {
            gainDao.updateGain(gains[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncGain extends AsyncTask<Void, Void, Void> {
        private GainDao gainDao;

        private DeleteAllAsyncGain(GainDao gainDao) {
            this.gainDao = gainDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gainDao.deleteAllGains();
            return null;
        }
    }

}
