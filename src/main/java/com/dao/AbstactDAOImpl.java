package com.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class AbstactDAOImpl {

    protected JdbcTemplate jdbcTemplateObject;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplateObject = jdbcTemplate;
    }
}
