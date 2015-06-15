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


    @Test
    public void shouldBeValidateGameCreation(){ //CONSULTAR
        try{
            User.insert("francomagnado@gmail.com","fRaNcO","","contraseña");
            User.insert("francomaimbil@gmail.com","Franco","Maimbil","password123");
        }catch(UserException e){
            System.out.println(e.getCode());
        }
        User u1=User.findFirst("email=?","francomagnado@gmail.com");
        User u2= User.findFirst("email=?","francomaimbil@gmail.com");
        Pair<User,User> players = new Pair<User,User>(u1,u2);
        Game g= new Game(players);
        the(g).shouldNotBeNull();
        assertEquals(u1.get("id"),g.get("player1_id"));
        assertEquals(u2.get("id"),g.get("player2_id"));
        Board board=Board.findFirst("game_id=?",g.get("id"));
        the(board).shouldNotBeNull();
        System.out.println("TEST 1");
    }

    @Test
    public void shouldValidatethereIsAWinner(){
        try{
            User.insert("a@gmail.com","ariel","perez","12345678");
            User.insert("b@gmail.com","beatriz","gomez","87654321");
        }catch(UserException e){
            System.out.println(e.getCode());
   
        }

        User u1=User.findFirst("email=?","a@gmail.com");
        User u2=User.findFirst("email=?","b@gmail.com");
        Pair<User,User> players = new Pair<User,User>(u1,u2);
        Game game=new Game(players);
        Board board=game.getBoard();
        Cell cell=null;
       //User 1 must win. He'll put 4 chips in horizontal way;
       try{ 

            cell=board.fillCellMemory(u1,0);
            cell=board.fillCellMemory(u1,1);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u1,2);
            cell=board.fillCellMemory(u1,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();        
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true);                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCellMemory satisfactoriamente.
        
        }

        try{ 

            cell=board.fillCellMemory(u1,1);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u1,2);
            cell=board.fillCellMemory(u1,3);
            cell=board.fillCellMemory(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();        
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true);                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCellMemory satisfactoriamente.
        
        }
         //User 1 must win. He'll put 4 chips in vertical way;
        try{
            game= new Game(players);
            board=game.getBoard();
            cell=board.fillCellMemory(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u1,0);
            cell=board.fillCellMemory(u1,0);
            cell=board.fillCellMemory(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();        
        
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true);
        }

       
        try{
            game= new Game(players);
            board=game.getBoard();
            cell=board.fillCellMemory(u1,0);
            cell=board.fillCellMemory(u2,1);
            cell=board.fillCellMemory(u1,1);
            cell=board.fillCellMemory(u2,2);     
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u2,2);
            cell=board.fillCellMemory(u1,2);
            cell=board.fillCellMemory(u2,3);     
            cell=board.fillCellMemory(u2,3);
            cell=board.fillCellMemory(u2,3);
            cell=board.fillCellMemory(u1,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true);                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCellMemory satisfactoriamente.

        }

        try{
            game= new Game(players);
            board=game.getBoard();
            cell=board.fillCellMemory(u2,1);
            cell=board.fillCellMemory(u1,1);
            cell=board.fillCellMemory(u2,2);     
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u2,2);
            cell=board.fillCellMemory(u1,2);
            cell=board.fillCellMemory(u2,3);     
            cell=board.fillCellMemory(u2,3);
            cell=board.fillCellMemory(u2,3);
            cell=board.fillCellMemory(u1,3);
            cell=board.fillCellMemory(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true) ;               //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCellMemory satisfactoriamente.
        }
        
        try{
            game= new Game(players);
            board=game.getBoard();
            cell=board.fillCellMemory(u1,3);
            cell=board.fillCellMemory(u2,2);
            cell=board.fillCellMemory(u1,2);
            cell=board.fillCellMemory(u2,1);     
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u2,1);
            cell=board.fillCellMemory(u1,1);
            cell=board.fillCellMemory(u2,0);     
            cell=board.fillCellMemory(u2,0);
            cell=board.fillCellMemory(u2,0);
            cell=board.fillCellMemory(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true) ;               //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCellMemory satisfactoriamente.
        }
        

        try{
            game= new Game(players);
            board=game.getBoard();
            cell=board.fillCellMemory(u2,2);
            cell=board.fillCellMemory(u1,2);
            cell=board.fillCellMemory(u2,1);     
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCellMemory(u2,1);
            cell=board.fillCellMemory(u1,1);
            cell=board.fillCellMemory(u2,0);     
            cell=board.fillCellMemory(u2,0);
            cell=board.fillCellMemory(u2,0);
            cell=board.fillCellMemory(u1,0);
            cell=board.fillCellMemory(u1,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true) ;               //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCellMemory satisfactoriamente.
        }
    }
}




