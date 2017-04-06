/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.ArrayList;

/**
 *
 * @author wmerfalen
 */
public class Person {
    private String name = "";
    private int identifier =0;
    private int m_x;
    private int m_y;
    private int m_z;
    
    Person(){
        m_x = 0;
        m_y = 0;
        m_z = 0;
    }
    
    Person(String n,int id){
        identifier = id;
        name = n;
        m_x = 0;
        m_y = 0;
        m_z = 0;
    }
    
    public void setName(String n){
        name = n;
    }
    
    public String getName(){
        return name;
    }
    
    public int id(){
        return identifier;
    }
    
    public ArrayList<Room> scan(){
        ArrayList<Room> rooms;
        rooms = new ArrayList<>();
        
        return rooms;
    }
    
}
