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


/*    @Test
    public void shouldBeValidateGameCreation(){ //CONSULTAR
        User.insert("francomagnado@gmail.com","fRaNcO","","contraseña");
        UUser.insert("francomaimbil@gmail.com","Franco","Maimbil","password123");
        Game g= new Game(u1,u2);
        the(g).shouldNotBeNull();
        assertEquals(u1.get("id"),g.get("player1_id"));
        assertEquals(u2.get("id"),g.get("player2_id"));
        Board board=Board.findFirst("game_id=?",g.get("id"));
        the(board).shouldNotBeNull();
    }

    @Test
    public void shouldValidatethereIsAWinner(){
        try{
            User.insert("a@gmail.com","ariel","perez","12345678");
            User.insert("b@gmail.com","beatriz","gomez","87654321")
        }catch(UserException e){

        }
        User u1=User.findFirst("email=?","a@gmail.com");
        User u2=User.findFirst("email=?","b@gmail.com");
        Game game=new Game(u1,u2);
        Board board=Board.findFirst("game_id=?",game.get("id"));
        Cell cell;
       //User 1 must win. He'll put 4 chips in horizontal way;
       try{  

            cell=board.fillCell(u1,0);
            cell=board.fillCell(u1,1);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();        
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCell satisfactoriamente.
        
        }
        //The user 2 must win. He'll put 4 chips in vertical way.
        try{     
            board.emptyBoard();
            cell=board.fillCell(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeTrue();      
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //it return this, because we wish that the test fail.   
        }

        //The user 1 must win.He'll put 4 chìps diagonally to left and down.
        try{
            board.emptyBoard();
            cell=board.fillCell(u1,2);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u1,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,4);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,5);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,5);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,5);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,6);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,5);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCell satisfactoriamente.

        }

        //The user 1 must win. He'll put 4 chips diagonally to right and down. The last chip has been inserted in the middle.
        try{
            board.emptyBoard();
            cell=board.fillCell(u1,3);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,1);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,0);
            the(game.thereIsAWinner(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,2);
            the(game.thereIsAWinner(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.thereIsAWinner(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCell satisfactoriamente.
        }


    }
*/
}