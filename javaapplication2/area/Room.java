/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2.area;


/* For the removal of array elements */
import java.util.ArrayList;
import javaapplication2.EventBroadcast;
import javaapplication2.Person;

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
    private String m_name;
    private String m_desc;
    private int m_x =0;
    private int m_y =0;
    private int m_z =0;

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
    
    Room(String rName,int _x,int _y,int _z){
        m_x = _x;
        m_y = _y;
        m_z = _z;
        m_name = rName;
        m_adjacent = null;
    }

    Room(String roomName, String roomDesc, int x, int y, int z) {
        m_name = roomName;
        m_desc = roomDesc;
        m_x = x;
        m_y = y;
        m_z = z;
        m_adjacent = null;
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
}
