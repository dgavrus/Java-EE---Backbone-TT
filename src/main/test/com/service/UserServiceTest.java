package com.service;

import com.dao.UserDAOdb;
import com.model.User;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;

public class UserServiceTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    UserDAOdb userDAOdb;
    UserService userService = new UserService();

    @Before
    public void before() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("111","444"));
        userDAOdb = context.mock(UserDAOdb.class);
        Field f1 = userService.getClass().getDeclaredField("userDAOdb");
        f1.setAccessible(true);
        f1.set(userService, userDAOdb);
    }

    @Test
    public void getAuthenticatedUser(){
        final User user = new User(1,"111","222","333","444",2);
        context.checking(new Expectations(){{

            oneOf(userDAOdb).getUserByLogin("111");
            will(returnValue(user));
        }});
        Assert.assertEquals(userService.getAuthenticatedUser(), user);
    }

}
