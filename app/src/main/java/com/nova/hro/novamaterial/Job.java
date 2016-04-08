package com.nova.hro.novamaterial;

/**
 * Created by shrikant on 6/22/2015.
 */
public class Job {
    private String ID;
    private String position;
    private String experience;
    private String description;
    private String remarks;
    private String location;
    private String type;
    private String domain;
    private String expDate;


    public Job(String ID, String domain, String position, String type, String experience, String description, String location, String remarks, String expDate) {
        this.description = description;
        this.experience = experience;
        this.ID = ID;
        this.type = type;
        this.location = location;
        this.position = position;
        this.remarks = remarks;
        this.domain = domain;
        this.expDate = expDate;
    }

    public Job(String ID, String domain, String position, String type, String experience, String description, String location, String remarks) {
        this.description = description;
        this.experience = experience;
        this.ID = ID;
        this.type = type;
        this.location = location;
        this.position = position;
        this.remarks = remarks;
        this.domain = domain;
    }

    public Job(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
