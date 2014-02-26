package com.dao;

import com.model.User;

import java.util.List;


public interface UserDAOdb {

    public User getUserById(Integer id);

    public User getUserByLogin(String login);

    public List<User> listUsers();

    public String getFirstNameById(int id);

    public String getLastNameById(int id);

}
