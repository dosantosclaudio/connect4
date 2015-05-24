package com.unrc.app;

import java.util.*;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unrc.app.*;

import static org.junit.Assert.assertEquals;
import static org.javalite.test.jspec.JSpec.the;


public class BoardTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4_test", "root", "root");
        System.out.println("BoardTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("BoardTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

/*

    @Test
    public void shouldValidateBoardCreation(){ //CONSULTAR TODOS LOS TEST NCONSTRUCTOR GAME NO ANDA bIEN
        User u1= User.createIt("email","direccion@gmail.com","password","password","first_name","nombre");
        User u2= User.createIt("email","otra_direccion@gmail.com","password","password","first_name","nombre2");
        Game g1= new Game(new Pair<User,User>(u1,u2));

        Board b1= Board.findFirst("game_id =?", g1.get("id"));
        b1.saveIt();
        b1.printBoard();
        the(b1).shouldNotBeNull();    
    }

    @Test
    public void shouldValidateBoardCreation2(){
        User u1= User.createIt("email","otradireccion@gmail.com","password","password","first_name","nombreSS");
        User u2= User.createIt("email","otra_email@gmail.com","password","password","first_name","nombre265d");

        Game g1= new Game(new Pair<User,User>(u1,u2));
        Board b1= Board.findFirst("game_id =?", g1.get("id"));
        for (int i=0; i<7; i++) {
            for (int j=0; j<6; j++) {
                List<Cell> aux= Cell.where("board_id = ?",b1.get("id"));
                org.junit.Assert.assertEquals(aux.size(),6*7);
            }
        }
    }

    @Test
    public void shouldValidateBoardCreation3(){
        User u1= User.createIt("email","californication@gmail.com","password","contraseña","first_name","AnOtHeR_nAmE");
        User u2= User.createIt("email","ya_no_se_que_poner@gmail.com","password","password123","first_name","otro_ nombre");

        Game g1= new Game(new Pair<User,User>(u1,u2));

        Board b1= Board.findFirst("game_id =?", g1.get("id"));
       for (int i=0; i<7; i++) {
            for (int j=0; j<6; j++) {
                Cell aux= Cell.findFirst("board_id = ? and col = ? and row = ?",b1.get("id"),i,j);
                the(aux).shouldNotBeNull();
            }
        }
    }


*/
}
