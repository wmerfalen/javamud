/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud.area;

import java.util.ArrayList;

/**
 *
 * @author wmerfalen
 */
public class Area {
    private final ArrayList<Room> m_rooms;

    Area() {
        m_rooms = null;
    }
    
    Area(ArrayList<Room> roomList){
        m_rooms = roomList;
    }
    
    
    
    public void debug_dumpAreaVisual(){
        
    }
    
}
