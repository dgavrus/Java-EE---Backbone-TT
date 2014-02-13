package com.model;

import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class User implements GrantedAuthority {

    public User(int id, String login, String firstName,
                String lastName, String password, int accountId){
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.accountId = accountId;
    }

    public User() {

    }

    private int id;

    private String login;

    private String firstName;

    private String lastName;

    private String password;

    private int accountId;

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role == User.Role.Employee ? "Employee" : "Client";
    }

    public enum Role {Employee, Client}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public JSONObject getJSONObject(){
        JSONObject result = new JSONObject();
        result.put("FirstName", getFirstName());
        result.put("LastName", getLastName());
        result.put("AccountId", getAccountId());
        return result;
    }
}
