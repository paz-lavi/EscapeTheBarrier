package com.example.hw2;


import java.util.Date;

public class Record  implements Comparable<Record>{
   private String Name;
   private int Score;
    private double Latitude;
    private double Longitude;
    private Date date;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public Record(String name, int score, double latitude, double longitude) {
        Name = name;
        Score = score;
        Latitude = latitude;
        Longitude = longitude;
        date = new Date();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getScore() {
        return Score;
    }

    @Override
    public String toString() {
        return
                "Name='" + Name  +
                ", Score=" + Score +
                ", date=" + date ;
    }

    public void setScore(int score) {
        Score = score;
    }

    public Record(String name, int score) {
        Name = name;
        Score = score;
    }


    @Override
    public int compareTo(Record o) {
        return  o.getScore() - this.getScore() ;
    }
}
