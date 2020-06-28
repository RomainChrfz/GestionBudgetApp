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
    private int day;
    private int month;
    private int year;
    private int category;
    private String urlJustif;
    private String userId;

    //public Gain() { }

    public Gain(double gainValue, String description, int day, int month, int year, int category, String urlJustif, String userId) {
        this.gainValue = gainValue;
        this.description = description;
        this.day = day;
        this.month = month;
        this.year = year;
        this.category = category;
        this.urlJustif = urlJustif;
        this.userId = userId;
    }

    // --- GETTER ---

    public long getId() { return id; }
    public double getGainValue() { return gainValue; }
    public String getDescription() { return description; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getCategory() { return category; }
    public String getUrlJustif() { return urlJustif; }
    public String getUserId() { return userId; }

    // --- SETTER ---

    public void setId(long id) { this.id = id; }
    public void setGainValue(double gainValue) { this.gainValue = gainValue; }
    public void setDescription(String description) { this.description = description; }
    public void setDay(int day) { this.day = day; }
    public void setMonth(int month) { this.month = month; }
    public void setYear(int year) { this.year = year; }
    public void setCategory(int category) { this.category = category; }
    public void setUrlJustif(String urlJustif) { this.urlJustif = urlJustif; }
    public void setUserId(String userId) { this.userId = userId; }

}
