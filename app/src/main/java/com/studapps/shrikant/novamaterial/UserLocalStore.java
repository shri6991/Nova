package com.studapps.shrikant.novamaterial;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shrikant on 6/21/2015.
 */
public class UserLocalStore {
    public static final String SP_NAME = "UserDetails";
    SharedPreferences sharedPreferences;

    UserLocalStore(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
    }

    public void updateImage(String imageuri) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("imageuri", imageuri);
        spEditor.apply();
    }

    public void storePersonal(User user) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("username", user.getUsername());
        spEditor.putString("name", user.getName());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("age", user.getAge());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("phone", user.getPhone());
        spEditor.apply();
    }

    public void storeProfessional(User user) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("position", user.getPosition());
        spEditor.putString("experience", user.getExperience());
        spEditor.putString("curloc", user.getCurloc());
        spEditor.putString("desloc", user.getDesloc());
        spEditor.putString("com1name", user.getCom1name());
        spEditor.putString("com1pos", user.getCom1pos());
        spEditor.putString("com1from", user.getCom1from());
        spEditor.putString("com1to", user.getCom1to());
        spEditor.putString("com1resp", user.getCom1resp());
        spEditor.putString("com2name", user.getCom2name());
        spEditor.putString("com2pos", user.getCom2pos());
        spEditor.putString("com2from", user.getCom2from());
        spEditor.putString("com2to", user.getCom2to());
        spEditor.putString("com2resp", user.getCom2resp());
        spEditor.putString("com3name", user.getCom3name());
        spEditor.putString("com3pos", user.getCom3pos());
        spEditor.putString("com3from", user.getCom3from());
        spEditor.putString("com3to", user.getCom3to());
        spEditor.putString("com3resp", user.getCom3resp());
        spEditor.apply();
    }

    public User getPersonalDetails(User user) {
        user.setUsername(sharedPreferences.getString("username", ""));
        user.setPassword(sharedPreferences.getString("password", ""));
        user.setName(sharedPreferences.getString("name", ""));
        user.setEmail(sharedPreferences.getString("email", ""));
        user.setAge(sharedPreferences.getString("age", ""));
        user.setPhone(sharedPreferences.getString("phone", ""));
        return user;
    }

    public void putAllDetails(User user) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("username", user.getUsername());
        spEditor.putString("name", user.getName());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("age", user.getAge());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("phone", user.getPhone());
        spEditor.putString("position", user.getPosition());
        spEditor.putString("experience", user.getExperience());
        spEditor.putString("curloc", user.getCurloc());
        spEditor.putString("desloc", user.getDesloc());
        spEditor.putString("imageuri", user.getImageUri());
        spEditor.putString("com1name", user.getCom1name());
        spEditor.putString("com1pos", user.getCom1pos());
        spEditor.putString("com1from", user.getCom1from());
        spEditor.putString("com1to", user.getCom1to());
        spEditor.putString("com1resp", user.getCom1resp());
        spEditor.putString("com2name", user.getCom2name());
        spEditor.putString("com2pos", user.getCom2pos());
        spEditor.putString("com2from", user.getCom2from());
        spEditor.putString("com2to", user.getCom2to());
        spEditor.putString("com2resp", user.getCom2resp());
        spEditor.putString("com3name", user.getCom3name());
        spEditor.putString("com3pos", user.getCom3pos());
        spEditor.putString("com3from", user.getCom3from());
        spEditor.putString("com3to", user.getCom3to());
        spEditor.putString("com3resp", user.getCom3resp());
        spEditor.apply();
    }

    public User getAllDetails() {
        String username, name, password, email, age, phone, position, experience, curloc, desloc, imageuri, com1name, com1pos, com1from, com1to, com1resp, com2name, com2pos, com2from, com2to, com2resp, com3name, com3pos, com3from, com3to, com3resp;
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        name = sharedPreferences.getString("name", "");
        email = sharedPreferences.getString("email", "");
        age = sharedPreferences.getString("age", "");
        phone = sharedPreferences.getString("phone", "");
        position = sharedPreferences.getString("position", "");
        experience = sharedPreferences.getString("experience", "");
        curloc = sharedPreferences.getString("curloc", "");
        desloc = sharedPreferences.getString("desloc", "");
        imageuri = sharedPreferences.getString("imageuri", "");
        com1name = sharedPreferences.getString("com1name", "");
        com1pos = sharedPreferences.getString("com1pos", "");
        com1from = sharedPreferences.getString("com1from", "");
        com1to = sharedPreferences.getString("com1to", "");
        com1resp = sharedPreferences.getString("com1resp", "");
        com2name = sharedPreferences.getString("com2name", "");
        com2pos = sharedPreferences.getString("com2pos", "");
        com2from = sharedPreferences.getString("com2from", "");
        com2to = sharedPreferences.getString("com2to", "");
        com2resp = sharedPreferences.getString("com2resp", "");
        com3name = sharedPreferences.getString("com3name", "");
        com3pos = sharedPreferences.getString("com3pos", "");
        com3from = sharedPreferences.getString("com3from", "");
        com3to = sharedPreferences.getString("com3to", "");
        com3resp = sharedPreferences.getString("com3resp", "");

        return new User(username, name, password, email, age, phone, position, experience, curloc, desloc, imageuri, com1name, com1pos, com1from, com1to, com1resp, com2name, com2pos, com2from, com2to, com2resp, com3name, com3pos, com3from, com3to, com3resp);
    }

    public void logOutUser() {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear();
        spEditor.apply();
    }
}

