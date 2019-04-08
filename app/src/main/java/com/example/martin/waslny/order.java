package com.example.martin.waslny;

import android.provider.ContactsContract;

import java.io.Serializable;

public class order implements Serializable {
   private String uid;   // user id
   private String s_phone;  // sender phone number
   private String s_address;  // sender address
   private Double s_lat;  // start latitude (sender)
   private Double s_long; // start longitude (sender)
   private String r_first_name;  // receiver first name
   private String r_last_name;  // receiver last name
   private String r_phone;   // receiver phone
   private String r_address;  //receiver address
   private Double e_lat;  // end latitude (receiver)
   private Double e_long;   // end longitude (receiver)
   private String start_time; // time that delivery take order
   private String end_time;  // time that delivery deliver order
   private Double price;  // price for this service
   private String description; // describe send details




    public order() {
    }

    public order(String uid, String s_phone, String s_address, Double s_lat, Double s_long, String r_first_name, String r_last_name, String r_phone, String r_address, Double e_lat, Double e_long, String start_time, String end_time, Double price) {
        this.uid = uid;
        this.s_phone = s_phone;
        this.s_address = s_address;
        this.s_lat = s_lat;
        this.s_long = s_long;
        this.r_first_name = r_first_name;
        this.r_last_name = r_last_name;
        this.r_phone = r_phone;
        this.r_address = r_address;
        this.e_lat = e_lat;
        this.e_long = e_long;
        this.start_time = start_time;
        this.end_time = end_time;
        this.price = price;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setS_phone(String s_phone) {
        this.s_phone = s_phone;
    }

    public void setS_address(String s_address) {
        this.s_address = s_address;
    }

    public void setS_lat(Double s_lat) {
        this.s_lat = s_lat;
    }

    public void setS_long(Double s_long) {
        this.s_long = s_long;
    }

    public void setR_first_name(String r_first_name) {
        this.r_first_name = r_first_name;
    }

    public void setR_last_name(String r_last_name) {
        this.r_last_name = r_last_name;
    }

    public void setR_phone(String r_phone) {
        this.r_phone = r_phone;
    }

    public void setR_address(String r_address) {
        this.r_address = r_address;
    }

    public void setE_lat(Double e_lat) {
        this.e_lat = e_lat;
    }

    public void setE_long(Double e_long) {
        this.e_long = e_long;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public String getS_phone() {
        return s_phone;
    }

    public String getS_address() {
        return s_address;
    }

    public Double getS_lat() {
        return s_lat;
    }

    public Double getS_long() {
        return s_long;
    }

    public String getR_first_name() {
        return r_first_name;
    }

    public String getR_last_name() {
        return r_last_name;
    }

    public String getR_phone() {
        return r_phone;
    }

    public String getR_address() {
        return r_address;
    }

    public Double getE_lat() {
        return e_lat;
    }

    public Double getE_long() {
        return e_long;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {  return description; }

    public void setDescription(String description) {this.description = description; }
}
