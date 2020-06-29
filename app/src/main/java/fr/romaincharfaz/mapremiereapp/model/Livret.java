package fr.romaincharfaz.mapremiereapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;


@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "username", childColumns = "userId",onDelete = CASCADE, onUpdate = CASCADE))
public class Livret {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String userId;

    public Livret(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

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
