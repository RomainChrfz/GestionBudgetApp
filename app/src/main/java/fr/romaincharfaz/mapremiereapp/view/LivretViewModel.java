package fr.romaincharfaz.mapremiereapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.repositories.LivretRepository;

public class LivretViewModel extends AndroidViewModel {
    private LivretRepository livretRepository;

    public LivretViewModel(@NonNull Application application) {
        super(application);
        livretRepository = new LivretRepository(application);
    }

    public void insert(Livret livret) { livretRepository.createLivret(livret); }

    public void update(Livret livret) { livretRepository.updateLivret(livret); }

    public void delete(Livret livret) { livretRepository.deleteLivret(livret); }

    public LiveData<List<Livret>> getAllLivrets() { return livretRepository.getLivrets(); }

    public LiveData<List<Livret>> getUserLivrets(String username) { return livretRepository.getUserLivrets(username); }
}
