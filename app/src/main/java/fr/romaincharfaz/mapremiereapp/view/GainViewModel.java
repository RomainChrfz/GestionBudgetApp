package fr.romaincharfaz.mapremiereapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.repositories.GainRepository;

public class GainViewModel extends AndroidViewModel {
    private GainRepository gainRepository;


    public GainViewModel(@NonNull Application application) {
        super(application);
        gainRepository = new GainRepository(application);
    }

    public void insert(Gain gain) { gainRepository.createGain(gain); }

    public void update(Gain gain) { gainRepository.updateGain(gain); }

    public void delete(Gain gain) { gainRepository.deleteGain(gain); }

    public void deleteLivret(Livret livret) { gainRepository.deleteLivretGains(livret); }

    public void deleteAllGains() { gainRepository.deleteAllGains(); }

    public LiveData<List<Gain>> getAllGains(long id,String username) { return gainRepository.getLiveGains(id, username); }

    public List<Gain> getGains(long id, String username) { return gainRepository.getGains(id, username); }
}
