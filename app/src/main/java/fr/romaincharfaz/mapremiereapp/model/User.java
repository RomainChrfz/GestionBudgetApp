package fr.romaincharfaz.mapremiereapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    private String username;
    private String password;
    private String emailAdress;
    private String urlPicture;
    private int userStatus;

    public User(String username, String password, String emailAdress, String urlPicture, int userStatus) {
        this.username = username;
        this.password = password;
        this.emailAdress = emailAdress;
        this.urlPicture = urlPicture;
        this.userStatus = userStatus;
    }

    // --- GETTER ---

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmailAdress() { return emailAdress; }
    public String getUrlPicture() { return urlPicture; }
    public int getUserStatus() { return userStatus; }

    // --- SETTER ---

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmailAdress(String emailAdress) { this.emailAdress = emailAdress; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setUserStatus(int userStatus) { this.userStatus = userStatus; }
}
