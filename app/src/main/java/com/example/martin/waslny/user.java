package com.example.martin.waslny;

public class user {
     private String fname;
     private String lname;
     private String address;
     private String phone;


    public user() {

    }


    public user(String Fname, String Lname, String Address, String Phone) {
       this. fname = Fname;
       this. lname = Lname;
       this. address=Address;
       this. phone = Phone;
    }


    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
