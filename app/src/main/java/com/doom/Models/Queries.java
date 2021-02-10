package com.doom.Models;

public class Queries {
    String mood, status, chatterId, gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Queries(){}

    public Queries(String mood, String status) {
        this.mood = mood;
        this.status = status;
    }

    public Queries(String mood, String status, String chatterId) {
        this.mood = mood;
        this.status = status;
        this.chatterId = chatterId;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChatterId() {
        return chatterId;
    }

    public void setChatterId(String chatterId) {
        this.chatterId = chatterId;
    }
}
