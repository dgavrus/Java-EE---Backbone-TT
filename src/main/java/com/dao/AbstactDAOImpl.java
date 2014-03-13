package com.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstactDAOImpl {

    protected JdbcTemplate jdbcTemplateObject;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplateObject = jdbcTemplate;
    }
}
