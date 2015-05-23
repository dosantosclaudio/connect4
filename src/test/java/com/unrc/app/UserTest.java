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

}