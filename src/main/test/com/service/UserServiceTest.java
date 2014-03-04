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
        userService.setUserDAOdb(userDAOdb);

    }

    @Test
    public void getAuthenticatedUser(){
        final User user = new User(1,"111","222","333","444",2, User.Role.Employee);
        context.checking(new Expectations(){{

            oneOf(userDAOdb).getUserByLogin("111");
            will(returnValue(user));
        }});
        Assert.assertEquals(userService.getAuthenticatedUser(), user);
    }

    @Test
    public void authenticatedUserRoleTest(){
        final User user = new User(1,"111","222","333","444",2, User.Role.Employee);
        context.checking(new Expectations(){{
            oneOf(userDAOdb).getUserByLogin("111");
            will(returnValue(user));
        }});
        Assert.assertFalse("This user has other role", userService.getAuthenticatedUser().getAuthority().equals(User.Role.Client.toString()));
    }

}
