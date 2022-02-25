package com.example.smartchat;

public class Users {

    String personName, phoneNumber;
    String personDOB, password;

    public Users(){

    }
    public Users(String personName, String phoneNumber, String personDOB, String password) {
        this.personName = personName;
        this.phoneNumber = phoneNumber;
        this.personDOB = personDOB;
        this.password = password;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonDOB() {
        return personDOB;
    }

    public void setPersonDOB(String personDOB) {
        this.personDOB = personDOB;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
