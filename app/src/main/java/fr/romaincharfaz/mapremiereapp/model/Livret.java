package fr.romaincharfaz.mapremiereapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "username", childColumns = "userId"))
public class Livret {

    @PrimaryKey
    @NonNull
    private String name;
    private String userId;

    public Livret(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
