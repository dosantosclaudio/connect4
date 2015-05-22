package com.unrc.app;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unrc.app.*;

import static org.junit.Assert.assertEquals;
import static org.javalite.test.jspec.JSpec.the;


public class UserTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4_test", "root", "root");
        System.out.println("UserTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("UserTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
      User user = new User();
      user.set("last_name","Gerrard");
      the(user).shouldNotBe("valid");
      user.set("email", "juan@hotmail.com");
      the(user).shouldNotBe("valid");
      user.set("password","123456789");
      the(user).shouldBe("valid");
    }


    @Test
    public void shouldValidateSignUp(){
        try{
            User.insert("juan@hotmail.es","Juan","Perez","12345");      //The password is invalid, it is too shortest, so it mustn't sign up to new account.
        }catch(UserException e){
                User u=User.findFirst("email = ?", "juan@hotmail.es" );
                the(u).shouldBeNull(); 
        }
        try{
            User.insert("juan@hotmail.es","Juan","Perez","12345678");
            User.insert("juan@hotmail.es","Juan","Perez","123456789");
        }catch(UserException e){
            User u=User.findFirst("email = ?", "juan@hotmail.es" );     //The second insert mustn't work because "juan@hotmail.es" allready registered.
            assertEquals(u.get("password"),"12345678");
        }
    }

    @Test
    public void shouldValidateSignIn(){
        try{
        User.insert("jg@gmail.com","Juan","Giroud","arsenalfc");
        }catch(UserException e){

        }
        User u=null;
        try{
            u=User.signIn("jg2@gmail.com","dddddddddddd");
            the(u).shouldBeNull();
        }catch(UserException e){
            the(u).shouldBeNull();          //The account doesn't exist
        }
        try{
            u=User.signIn("jg@gmail.com","arsenalf");
            the(u).shouldBeNull();
        }catch(UserException e){
            the(u).shouldBeNull();          //Invalid password
        }
        try{
            u=User.signIn("jg2@gmail.com","arsenalfc");
            the(u).shouldBeNull();
        }catch(UserException e){
            the(u).shouldBeNull();          //Invalid email adress
        }
        try{
            u=User.signIn("jg@gmail.com","arsenalfc");
            the(u).shouldNotBeNull();       //The email and password are correct.
        }catch(UserException e){
            the(u).shouldNotBeNull();       
        }
       
    }

    @Test
    public void shouldValidateisWin(){
        try{
            User.insert("a@gmail.com","ariel","perez","12345678");
            User.insert("b@gmail.com","beatriz","gomez","87654321")
        }catch(UserException e){

        }
        User u1=findFirst("email=?","a@gmail.com");
        User u2=findFirst("email=?","b@gmail.com");
        Game game=new Game(u1,u2);
        Board board=findFirst("game_id=?",game.get("id"));
        Cell cell;
       //User 1 must win. He'll put 4 chip in horizontal way;
       try{  

            cell=board.fillCell(u1,0);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,3);
            the(game.isWin(u1,cell)).shouldBeTrue();        
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCell satisfactoriamente.
        
        }
        //The user 2 must win. He'll put 4 chip in vertical way.
        try{     
            board.emptyBoard();
            cell=board.fillCell(u1,0);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeTrue();      
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCell satisfactoriamente.
        
        }

        //The user 1 must win.He'll put 4 chìp diagonally to left and down.
        try{
            board.emptyBoard();
            cell=board.fillCell(u1,2);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,3);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u1,3);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,4);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,5);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,4);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,5);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,5);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,6);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,5);
            the(game.isWin(u1,cell)).shouldBeTrue();
        }catch(BoardException e){
            assertEquals(e.getMessage(),false,true)                //Se desea retornar que el test de error, ya que por x causa no se ha podido realizar fillCell satisfactoriamente.

        }

        try{
            board.emptyBoard();
            cell=board.fillCell(u1,3);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,1);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,2);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,0);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,0);
            the(game.isWin(u1,cell)).shouldBeFalse();
            cell=board.fillCell(u2,2);
            the(game.isWin(u2,cell)).shouldBeFalse();
            cell=board.fillCell(u1,1);
            the(game.isWin(u1,cell)).shouldBeFalse();
        }catch(BoardException e){

        }


    }


}