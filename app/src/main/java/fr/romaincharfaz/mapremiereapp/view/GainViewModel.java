package fr.romaincharfaz.mapremiereapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.repositories.GainRepository;
import fr.romaincharfaz.mapremiereapp.repositories.UserRepository;

public class GainViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private GainRepository gainRepository;

    //private String currentUser;

    public GainViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gainRepository = new GainRepository(application);
    }

    //public void init(String username) {
    //    if (this.currentUser != null) {
    //        return;
    //    }
    //    currentUser = userRepository.getUser(username);
    //}

    public void insert(Gain gain) { gainRepository.createGain(gain); }

    public void update(Gain gain) { gainRepository.updateGain(gain); }

    public void delete(Gain gain) { gainRepository.deleteGain(gain); }

    public void deleteLivret(Livret livret) { gainRepository.deleteLivretGains(livret); }

    public void deleteAllGains() { gainRepository.deleteAllGains(); }

    public LiveData<List<Gain>> getAllGains(String username) { return gainRepository.getLiveGains(username); }

    public List<Gain> getGains(String username) { return gainRepository.getGains(username); }
}
