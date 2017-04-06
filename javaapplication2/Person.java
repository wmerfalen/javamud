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
 * @param <TPipeInterface>
 */
public class Person {
    private String name = "";
    private int identifier =0;
    private int m_x;
    private int m_y;
    private int m_z;
    private IPipeInterface m_pipefd;
    
    Person(){
        m_x = 0;
        m_y = 0;
        m_z = 0;
        m_pipefd = null;
    }
    
    Person(String n,int id){
        identifier = id;
        name = n;
        m_x = 0;
        m_y = 0;
        m_z = 0;
        m_pipefd = null;
    }
    
    public void setName(String n){
        name = n;
    }
    
    public String getName(){
        return name;
    }
    
    public void setPipeInterface(IPipeInterface t){
        m_pipefd = t;
    }
    
    public int id(){
        return identifier;
    }
    
    public ArrayList<Room> scan(){
        ArrayList<Room> rooms;
        rooms = new ArrayList<>();
        
        return rooms;
    }
    
    public Integer pipeWrite(String msg){
        return m_pipefd.write(msg, msg.length());
    }
    
    public String pipeRead(Integer len){
        return m_pipefd.read(len);
    }
}
