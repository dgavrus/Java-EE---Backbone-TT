package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.User;
import org.springframework.jdbc.core.RowMapper;

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