package com.unrc.app;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unrc.app.*;

import static org.junit.Assert.assertEquals;
import static org.javalite.test.jspec.JSpec.the;


public class CellTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4_test", "root", "root");
        System.out.println("CellTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("CellTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateCellCreation(){
        Cell c=Cell.createIt("board_id",1,"col",0,"row",0);
        the(c).shouldNotBeNull(); 
    }
}