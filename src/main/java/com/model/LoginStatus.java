package com.model;

import org.json.JSONObject;

public class LoginStatus {

    private Boolean loggedIn;
    private String username;
    private String password;
    private User.Role role;

    public LoginStatus(String username, String password, Boolean loggedIn, User.Role role) {
        this.loggedIn = loggedIn;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public LoginStatus(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String toString(){
        JSONObject json = new JSONObject();
        json.put("username", getUsername());
        json.put("password", getPassword());
        json.put("loggedIn", isLoggedIn());
        return json.toString();
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}