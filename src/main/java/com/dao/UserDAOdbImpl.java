package com.dao;

import com.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOdbImpl implements UserDAOdb {

    private JdbcTemplate jdbcTemplateObject;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplateObject = jdbcTemplate;
    }

    public void create(String login, String firstName, String lastName,
                       String password, Integer accountId) {
        String query = "insert into users (login, firstname, lastname, " +
                "password, accountId) values (?, ?, ?, ?, ?)";
        jdbcTemplateObject.update(query, firstName, lastName, password, accountId);

    }

    public User getUserById(Integer id) {
        String SQL = "select * from users where id = ?";
        User user;
        try {
            user = jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{id}, new UserMapper());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    public User getUserByLogin(String login) {
        String SQL = "select * from users where login = ?";
        User user;
        try {
            user = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{login}, new UserMapper());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    public List<User> listUsers() {
        String query = "select * from users";
        return jdbcTemplateObject.query(query, new UserMapper());
    }

    public void delete(Integer id) {

    }

    public JSONArray getUsersJSONArray(){
        JSONArray result = new JSONArray();
        for(User user : listUsers()){
            JSONObject userJSON = user.getJSONObject();
            result.put(userJSON);
        }
        return result;
    }
}
