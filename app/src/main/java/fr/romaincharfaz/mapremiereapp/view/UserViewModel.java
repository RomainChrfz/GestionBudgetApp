package fr.romaincharfaz.mapremiereapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;


    public UserViewModel( @NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.getUsers();
    }

    public void insert(User user) { userRepository.createUser(user); }

    public User get(String username) { return userRepository.getUser(username); }

    public LiveData<User> getLive(String username) { return userRepository.getLiveUser(username); }

    public LiveData<List<User>> getAllUsers () { return allUsers; }

    public void update(User user) { userRepository.updateUser(user); }

}
