package com.nova.hro.novamaterial;

/**
 * Created by shrikant on 6/21/2015.
 */
public class User {
    private String username;
    private String name;
    private String password;
    private String email;
    private String age;
    private String phone;
    private String position;
    private String experience;
    private String desloc;
    private String curloc;
    private String imageUri;
    private String com1name, com2name, com3name, com1pos, com2pos, com3pos, com1from, com2from, com3from, com1to, com2to, com3to, com1resp, com2resp, com3resp;

    public User(String username, String name, String password, String email, String age, String phone, String position, String experience, String curloc, String desloc, String imageUri, String com1name, String com1pos, String com1from, String com1to, String com1resp, String com2name, String com2pos, String com2from, String com2to, String com2resp, String com3name, String com3pos, String com3from, String com3to, String com3resp) {
        this.age = age;
        this.curloc = curloc;
        this.desloc = desloc;
        this.email = email;
        this.experience = experience;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.position = position;
        this.username = username;
        this.imageUri = imageUri;
        this.com1name = com1name;
        this.com1pos = com1pos;
        this.com1from = com1from;
        this.com1to = com1to;
        this.com1resp = com1resp;
        this.com2name = com2name;
        this.com2pos = com2pos;
        this.com2from = com2from;
        this.com2to = com2to;
        this.com2resp = com2resp;
        this.com3name = com3name;
        this.com3pos = com3pos;
        this.com3from = com3from;
        this.com3to = com3to;
        this.com3resp = com3resp;
    }

    //Personal Only
    public User(String username, String name, String password, String email, String age, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
    }

    //Login only
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = "";
        this.age = "";
        this.email = "";
        this.position = "";
        this.phone = "";
        this.experience = "";
        this.curloc = "";
        this.desloc = "";
        this.imageUri = "";
    }

    //Default
    public User() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.age = null;
        this.curloc = null;
        this.desloc = null;
        this.name = null;
        this.position = null;
        this.experience = null;
        this.imageUri = null;

    }


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurloc() {
        return curloc;
    }

    public void setCurloc(String curloc) {
        this.curloc = curloc;
    }

    public String getDesloc() {
        return desloc;
    }

    public void setDesloc(String desloc) {
        this.desloc = desloc;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCom3resp() {
        return com3resp;
    }

    public void setCom3resp(String com3resp) {
        this.com3resp = com3resp;
    }

    public String getCom2resp() {
        return com2resp;
    }

    public void setCom2resp(String com2resp) {
        this.com2resp = com2resp;
    }

    public String getCom1resp() {
        return com1resp;
    }

    public void setCom1resp(String com1resp) {
        this.com1resp = com1resp;
    }

    public String getCom3to() {
        return com3to;
    }

    public void setCom3to(String com3to) {
        this.com3to = com3to;
    }

    public String getCom2to() {
        return com2to;
    }

    public void setCom2to(String com2to) {
        this.com2to = com2to;
    }

    public String getCom1to() {
        return com1to;
    }

    public void setCom1to(String com1to) {
        this.com1to = com1to;
    }

    public String getCom3from() {
        return com3from;
    }

    public void setCom3from(String com3from) {
        this.com3from = com3from;
    }

    public String getCom2from() {
        return com2from;
    }

    public void setCom2from(String com2from) {
        this.com2from = com2from;
    }

    public String getCom1from() {
        return com1from;
    }

    public void setCom1from(String com1from) {
        this.com1from = com1from;
    }

    public String getCom3pos() {
        return com3pos;
    }

    public void setCom3pos(String com3pos) {
        this.com3pos = com3pos;
    }

    public String getCom2pos() {
        return com2pos;
    }

    public void setCom2pos(String com2pos) {
        this.com2pos = com2pos;
    }

    public String getCom1pos() {
        return com1pos;
    }

    public void setCom1pos(String com1pos) {
        this.com1pos = com1pos;
    }

    public String getCom3name() {
        return com3name;
    }

    public void setCom3name(String com3name) {
        this.com3name = com3name;
    }

    public String getCom2name() {
        return com2name;
    }

    public void setCom2name(String com2name) {
        this.com2name = com2name;
    }

    public String getCom1name() {
        return com1name;
    }

    public void setCom1name(String com1name) {
        this.com1name = com1name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
