package fr.romaincharfaz.mapremiereapp.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.database.accountDatabase;
import fr.romaincharfaz.mapremiereapp.database.dao.LivretDao;
import fr.romaincharfaz.mapremiereapp.model.Livret;

public class LivretRepository {
    private LivretDao livretDao;

    public LivretRepository(Application application) {
        accountDatabase database = accountDatabase.getInstance(application);
        livretDao = database.livretDao();
    }

    // --- GET ---
    public LiveData<List<Livret>> getUserLivrets(String userId) { return this.livretDao.getUserLivrets(userId); }

    public LiveData<List<Livret>> getLivrets() { return this.livretDao.getLivrets(); }

    // --- CREATE ---
    public void createLivret(Livret livret) {
        new InsertAsyncLivret(livretDao).execute(livret);
    }

    // --- DELETE ---
    public void deleteLivret(Livret livret) {
        new DeleteAsyncLivret(livretDao).execute(livret);
    }

    // --- UPDATE ---
    public void updateLivret(Livret livret) {
        new UpdateAsyncLivret(livretDao).execute(livret);
    }

    private static class InsertAsyncLivret extends AsyncTask<Livret, Void, Void> {
        private LivretDao livretDao;

        private InsertAsyncLivret(LivretDao livretDao) {
            this.livretDao = livretDao;
        }

        @Override
        protected Void doInBackground(Livret... livrets) {
            livretDao.createLivret(livrets[0]);
            return null;
        }
    }

    private static class DeleteAsyncLivret extends AsyncTask<Livret, Void, Void> {
        private LivretDao livretDao;

        private DeleteAsyncLivret(LivretDao livretDao) {
            this.livretDao = livretDao;
        }

        @Override
        protected Void doInBackground(Livret... livrets) {
            livretDao.deleteLivret(livrets[0].getId());
            return null;
        }
    }

    private static class UpdateAsyncLivret extends AsyncTask<Livret, Void, Void> {
        private LivretDao livretDao;

        private UpdateAsyncLivret(LivretDao livretDao) {
            this.livretDao = livretDao;
        }

        @Override
        protected Void doInBackground(Livret... livrets) {
            livretDao.updateLivret(livrets[0]);
            return null;
        }
    }

}
