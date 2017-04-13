/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud.area;


/* For the removal of array elements */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javamud.EventBroadcast;
import javamud.Person;
import javamud.mysql.Static;

/**
 *
 * @author wmerfalen
 */
public class Room {
    
    public static final int NORTH = 0;
    public static final int NORTHEAST = 1;
    public static final int NORTHWEST = 2;
    public static final int EAST = 3;
    public static final int WEST = 4;
    public static final int SOUTH = 5;
    public static final int SOUTHEAST = 6;
    public static final int SOUTHWEST = 7;
    public static final int UP = 8;
    public static final int DOWN = 9;
    
    private ArrayList<Person> m_inhabitants;
    private int m_id;
    private String m_name;
    private String m_desc;
    private int m_x =0;
    private int m_y =0;
    private int m_z =0;
    private boolean m_loaded = false;

    
    private Room m_north = null;
    private Room m_northwest = null;
    private Room m_northeast = null;
    private Room m_east = null;
    private Room m_west = null;
    private Room m_south = null;
    private Room m_southeast = null;
    private Room m_southwest = null;
    private Room m_down = null;
    private Room m_up = null;
    
    private ArrayList<Room> m_adjacent;
    
    public Room(int id) throws Exception{
        m_id = id;
        m_loaded = loadById(m_id);
    }
    
    public Room(String rName,int _x,int _y,int _z){
        m_id = 0;
        m_x = _x;
        m_y = _y;
        m_z = _z;
        m_name = rName;
        m_adjacent = null;
        m_loaded = false;
    }

    public Room(String roomName, String roomDesc, int x, int y, int z) {
        m_id = 0;
        m_name = roomName;
        m_desc = roomDesc;
        m_x = x;
        m_y = y;
        m_z = z;
        m_adjacent = null;
        m_loaded = false;
    }
    
    public Room(int id,String roomName, String roomDesc, int x, int y, int z) throws Exception {
        m_id = id;
        m_name = roomName;
        m_desc = roomDesc;
        m_x = x;
        m_y = y;
        m_z = z;
        m_adjacent = null;
        m_loaded = loadById(id);
    }

    public Room() {
        m_id = 0;
        m_x = 0;
        m_y = 0;
        m_z = 0;
        m_name = null;
        m_adjacent = null;
        m_loaded = false;
    }


    public void attachRoom(Room r,int dir){
        switch(dir){
            case Room.NORTH:
                m_north = r;
                break;
            case Room.NORTHEAST:
                m_northeast = r;
                break;
            case Room.NORTHWEST:
                m_northwest = r;
                break;
            case Room.EAST:
                m_east = r;
                break;
            case Room.WEST:
                m_west = r;
                break;
            case Room.SOUTH:
                m_south = r;
                break;
            case Room.SOUTHEAST:
                m_southeast = r;
                break;
            case Room.SOUTHWEST:
                m_southwest = r;
                break;
            case Room.UP:
                m_up = r;
                break;
            case Room.DOWN:
                m_down = r;
                break;
            default:
                System.out.println("javaapplication2.Room.attachRoom() UNKNOWN DIRECTION: " + dir);
        }
    }
    
    public ArrayList<Person> scan(){
        return m_inhabitants;
    }
    
    public void leave(Person p,int direction){
        for(Person in : m_inhabitants){
            if(in.id() == p.id()){
                m_inhabitants.remove(in);
                EventBroadcast.event(EventBroadcast.EventID.ROOM_LEAVE,p.id());
                return;
            }
        }
    }
    
    public boolean enter(Person p){
        for(Person in: m_inhabitants){
            if(in.id() == p.id()){
                return false;
            }
        }
        m_inhabitants.add(p);
        return true;
    }
    
    public void debug_dumpInhabitants(){
        for(Person i: m_inhabitants){
            System.out.println("INHABITANT ID: " + i.id());
        }
    }

    public void clearDirection(int dir) {
        switch(dir){
            case Room.DOWN:
                m_down = null;
                break;
            case Room.UP:
                m_up = null;
                break;
            case Room.NORTH:
                m_north = null;
                break;
            case Room.NORTHEAST:
                m_northeast = null;
                break;
            case Room.NORTHWEST:
                m_northwest = null;
                break;
            case Room.EAST:
                m_east = null;
                break;
            case Room.WEST:
                m_west = null;
                break;
            case Room.SOUTH:
                m_south = null;
                break;
            case Room.SOUTHEAST:
                m_southeast = null;
                break;
            case Room.SOUTHWEST:
                m_southwest = null;
                break;
            default:
                System.out.println("javaapplication2.Room.clearDirection() UNKNOWN DIRECTION: " + dir);
        }
    }

    public String coords() {
        return Integer.toString(m_x) + ":" + 
                Integer.toString(m_y) + ":" + 
                Integer.toString(m_z);
    }

    public void attachRoomId(int roomId, int direction) throws Exception {
        if(direction == Room.NORTH){
            m_north = new Room(roomId);
        }
    }

    public boolean loadById(int id) throws SQLException, Exception {
        try{
            PreparedStatement prep = Static.mysql.prepare("SELECT * FROM rooms where id = ?");
            prep.setInt(0, id);
            ResultSet r = prep.executeQuery();
            m_id = r.getInt("id");
            m_x = r.getInt("x");
            m_y = r.getInt("y");
            m_z = r.getInt("z");
            int temp = r.getInt("dir_up");
            if(temp != 0){
                m_up = new Room(temp);
            }else{
                m_up = null;
            }
            temp = r.getInt("dir_down");
            if(temp != 0){
                m_down = new Room(temp);
            }else{
                m_down = null;
            }
            temp = r.getInt("dir_north");
            if(temp != 0){
                m_north = new Room(temp);
            }else{
                m_north = null;
            }
            temp = r.getInt("dir_east");
            if(temp != 0){
                m_east = new Room(temp);
            }else{
                m_east = null;
            }
            temp = r.getInt("dir_west");
            if(temp != 0){
                m_west = new Room(temp);
            }else{
                m_west = null;
            }
            temp = r.getInt("dir_south");
            if(temp != 0){
                m_south = new Room(temp);
            }else{
                m_south = null;
            }
            temp = r.getInt("dir_northeast");
            if(temp != 0){
                m_northeast = new Room(temp);
            }else{
                m_northeast = null;
            }
            temp = r.getInt("dir_northwest");
            if(temp != 0){
                m_northwest = new Room(temp);
            }else{
                m_northwest = null;
            }
            temp = r.getInt("dir_southeast");
            if(temp != 0){
                m_southeast = new Room(temp);
            }else{
                m_southeast = null;
            }
            temp = r.getInt("dir_southwest");
            if(temp != 0){
                m_southwest = new Room(temp);
            }else{
                m_southwest = null;
            }
            m_loaded = true;
        }catch(Exception e){
            m_loaded = false;
            return false;
        }
        return true;
    }

    public boolean loaded() {
        return m_loaded;
    }
}
