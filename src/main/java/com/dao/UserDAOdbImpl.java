package com.dao;

import com.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.*;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public String getFirstNameById(int id) {
        String SQL = "select firstName from users where id = ?";
        String fn;
        try {
            fn = jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{id}, String.class);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return fn;
    }

    public String getLastNameById(int id) {
        String SQL = "select lastName from users where id = ?";
        String ln;
        try {
            ln = jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{id}, String.class);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return ln;
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

    public class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setLogin(rs.getString("login"));
            user.setFirstName(rs.getString("firstname"));
            user.setLastName(rs.getString("lastname"));
            user.setPassword(rs.getString("password"));
            user.setAccountId(rs.getInt("accountid"));
            user.setRole(roleConverter(rs.getString("role")));
            return user;
        }

        private User.Role roleConverter(String status){
            if(status.equals("Employee")){
                return User.Role.Employee;
            } else return User.Role.Client;
        }
    }
}
