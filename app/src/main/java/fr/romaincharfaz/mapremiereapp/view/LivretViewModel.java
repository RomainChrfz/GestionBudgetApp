package fr.romaincharfaz.mapremiereapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.repositories.GainRepository;
import fr.romaincharfaz.mapremiereapp.repositories.LivretRepository;
import fr.romaincharfaz.mapremiereapp.repositories.UserRepository;

public class LivretViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private GainRepository gainRepository;
    private LivretRepository livretRepository;

    //private String currentUser;

    public LivretViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gainRepository = new GainRepository(application);
        livretRepository = new LivretRepository(application);
    }

    //public void init(String username) {
    //    if (this.currentUser != null) {
    //        return;
    //    }
    //    currentUser = userRepository.getUser(username);
    //}

    public void insert(Livret livret) { livretRepository.createLivret(livret); }

    public void update(Livret livret) { livretRepository.updateLivret(livret); }

    public void delete(Livret livret) { livretRepository.deleteLivret(livret); }

    public LiveData<List<Livret>> getAllLivrets() { return livretRepository.getLivrets(); }

    public LiveData<List<Livret>> getUserLivrets(String username) { return livretRepository.getUserLivrets(username); }
}
