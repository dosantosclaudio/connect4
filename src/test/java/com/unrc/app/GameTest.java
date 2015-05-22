package com.unrc.app;

import java.util.*;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unrc.app.*;

import static org.junit.Assert.assertEquals;
import static org.javalite.test.jspec.JSpec.the;


public class GameTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4_test", "root", "root");
        System.out.println("GameTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("GameTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }
/*
    @Test
    public void shouldBeValidateGameCreation(){ //CONSULTAR
        User u1= User.createIt("email","francomagnado@gmail.com","password","contraseña","first_name","fRaNcO");
        User u2= User.createIt("email","francomainbil@gmail.com","password","password123","first_name","FRAN");
        Game g= new Game(new Pair<User,User>(u1,u2));
        the(g).shouldNotBeNull();
    }*/
}