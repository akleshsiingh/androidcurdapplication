package com.androidfizz.model;

/**
 * Created by Aklesh on 1/2/2018.
 */

public class ModelPerson {
    private int personID;
    private String name, email, age;


    public ModelPerson(int personID, String name, String email, String age) {
        this.personID = personID;
        this.name = name;
        this.email = email;
        this.age = age;
    }
    public ModelPerson(String name, String email, String age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
