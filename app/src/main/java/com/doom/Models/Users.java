package com.doom.Models;

public class Users {
    String username,mail,gender;


    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public Users(String username, String mail, String gender) {
        this.username = username;
        this.mail = mail;
        this.gender = gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


}
