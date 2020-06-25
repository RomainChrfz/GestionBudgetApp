package fr.romaincharfaz.mapremiereapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Livret.class, parentColumns = "name", childColumns = "userId"))
public class Gain {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private double gainValue;
    private String description;
    private String date;
    private String urlJustif;
    private String userId;

    //public Gain() { }

    public Gain(double gainValue, String description, String date, String urlJustif, String userId) {
        this.gainValue = gainValue;
        this.description = description;
        this.date = date;
        this.urlJustif = urlJustif;
        this.userId = userId;
    }

    // --- GETTER ---

    public long getId() { return id; }
    public double getGainValue() { return gainValue; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getUrlJustif() { return urlJustif; }
    public String getUserId() { return userId; }

    // --- SETTER ---

    public void setId(long id) { this.id = id; }
    public void setGainValue(double gainValue) { this.gainValue = gainValue; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setUrlJustif(String urlJustif) { this.urlJustif = urlJustif; }
    public void setUserId(String userId) { this.userId = userId; }

}
