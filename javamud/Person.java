/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud;

import javamud.area.Room;
import javamud.interfaces.IPipeInterface;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 *
 * @author wmerfalen
 * @param <TPipeInterface>
 */
public class Person {
    private String m_name = "";
    private int m_identifier =0;
    private int m_x;
    private int m_y;
    private int m_z;
    private IPipeInterface m_pipefd;
    private SocketChannel m_socket;
    
    Person(){
        m_x = 0;
        m_y = 0;
        m_z = 0;
        m_pipefd = null;
        m_socket = null;
    }
    
    Person(String n,int id){
        m_identifier = id;
        m_name = n;
        m_x = 0;
        m_y = 0;
        m_z = 0;
        m_pipefd = null;
        m_socket = null;
    }
    
    public void setName(String n){
        m_name = n;
    }
    
    public String getName(){
        return m_name;
    }
    
    public void setSocketChannel(SocketChannel s){
        m_socket = s;
    }
    
    public SocketChannel getSocketChannel(){
        return m_socket;
    }
    
    public void setPipeInterface(IPipeInterface t){
        m_pipefd = t;
    }
    
    public IPipeInterface getPipeInterface(){
        return m_pipefd;
    }
    
    public int id(){
        return m_identifier;
    }
    
    public ArrayList<Room> scan(){
        ArrayList<Room> rooms = new ArrayList<Room>();
        
        return rooms;
    }
    
    public Integer pipeWrite(String msg){
        return m_pipefd.write(msg, msg.length());
    }
    
    public String pipeRead(Integer len){
        return m_pipefd.read(len);
    }

    boolean isAuthenticated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
