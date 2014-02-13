package com.service;

import com.dao.UserDAOdb;
import com.dao.UserDAOdbImpl;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDAOdb userDAOdb;

    public User getAuthenticatedUser(){
        String login;
        try {
            login = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (NullPointerException e){
            login = null;
        }
        return userDAOdb.getUserByLogin(login);
    }

}
