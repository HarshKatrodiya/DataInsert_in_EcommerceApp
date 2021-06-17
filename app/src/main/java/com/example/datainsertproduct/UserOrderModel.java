package com.example.datainsertproduct;

public class UserOrderModel {

    private String pushkey;
    private String imageuser;
    private String username;
    private String email;
    private String contact;
    private String address;
    private String password;


    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public UserOrderModel(String imageuser, String username, String email, String contact, String address, String password) {
        this.imageuser = imageuser;
        this.username = username;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.password = password;
    }

    public UserOrderModel() {
    }

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
