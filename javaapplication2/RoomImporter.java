/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wmerfalen
 */
public class RoomImporter {
    private String m_file_name;
    
    RoomImporter(String fName){
        m_file_name = fName;
    }
    
    public ArrayList<Room> importFromFile() throws IOException {
        //Open the file
        ArrayList<Room> rooms = null;
        try {
            FileReader fReader = new FileReader(m_file_name);
            BufferedReader bReader = new BufferedReader(fReader);
            String line;
            /*
             * Read the header. The header should look like this:
             * NxM
             * where N is the number of rows, and M is the number of columns
             * 
             * this file will generate an NxM grid of rooms
             */
            line = bReader.readLine();
            String[] parts = line.split("x");
            int rows = Integer.decode(parts[0]);
            int cols = Integer.decode(parts[1]);
            
            while((line = bReader.readLine()) != null){
                Integer iField = 0;
                String roomName = null,roomDesc = null;
                int x = 0,y = 0,z = 0;
                
                for(String part : line.split(",")){
                    switch(iField){
                        case 0:
                            roomName = part;
                            break;
                        case 1:
                            roomDesc = part;
                            break;
                        case 2:
                            x = Integer.decode(part);
                            break;
                        case 3:
                            y = Integer.decode(part);
                            break;
                        case 4:
                            z = Integer.decode(part);
                            break;
                        default:
                            System.out.println("Unknown column in csv file");
                    }
                    iField++;
                }
                rooms.add(new Room(roomName,roomDesc,x,y,z));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot open file!" + m_file_name);
            Logger.getLogger(RoomImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rooms;
    }
    
    public Room[][][] generateFromFile() throws IOException {
        try {
            FileReader fReader = new FileReader(m_file_name);
            BufferedReader bReader = new BufferedReader(fReader);
            String line;
            /*
             * Read the header. The header should look like this:
             * NxMxZ
             * where N is the number of rows, and M is the number of columns
             * and Z is the up and down
             * 
             * This file will generate an NxMxZ grid of rooms
             *
             */
            line = bReader.readLine();
            String[] parts = line.split("x");
            int rows = Integer.decode(parts[0]);
            int cols = Integer.decode(parts[1]);
            int zzz = Integer.decode(parts[2]);
            Room[][][] roomList = new Room[rows][cols][zzz];
            for(int x=0; x < rows;x++){
                for(int y=0;y < cols;y++){
                    for(int z=0;z < zzz;z++){
                        String r = "Room:[";
                        r += "["+x+"]";
                        r += "["+y+"]";
                        r += "["+z+"]";
                        String d = "An ordinary room";
                        roomList[x][y][z] = new Room(r,d,x,y,z); 
                    }
                }
            }
            for(int x=0; x < rows;x++){
                for(int y=0;y < cols;y++){
                    for(int z=0;z < zzz;z++){
                        if((x-1) > -1){
                            roomList[x][y][z].attachRoom(roomList[x-1][y][z],Room.WEST);
                        }else{
                            roomList[x][y][z].clearDirection(Room.WEST);
                        }
                        if((y-1) > -1){
                            roomList[x][y][z].attachRoom(roomList[x][y-1][z],Room.SOUTH);
                        }else{
                            roomList[x][y][z].clearDirection(Room.SOUTH);
                        }
                        if((z-1) > -1){
                            roomList[x][y][z].attachRoom(roomList[x][y][z-1],Room.DOWN);
                        }else{
                            roomList[x][y][z].clearDirection(Room.DOWN);
                        }
                        
                        if((x+1) < rows){
                            roomList[x][y][z].attachRoom(roomList[x+1][y][z],Room.EAST);
                        }else{
                            roomList[x][y][z].clearDirection(Room.EAST);
                        }
                        if((y+1) < cols){
                            roomList[x][y][z].attachRoom(roomList[x][y+1][z],Room.NORTH);
                        }else{
                            roomList[x][y][z].clearDirection(Room.NORTH);
                        }
                        if((z+1) < zzz){
                            roomList[x][y][z].attachRoom(roomList[x][y][z+1],Room.UP);
                        }else{
                            roomList[x][y][z].clearDirection(Room.UP);
                        }
                        
                        /* North west */
                        if((x-1) > -1 && (y+1) < cols){
                            roomList[x][y][z].attachRoom(roomList[x-1][y+1][z],Room.NORTHWEST);
                        }else{
                            roomList[x][y][z].clearDirection(Room.NORTHWEST);
                        }
                        /* North east */
                        if((x+1) < rows && (y+1) < cols){
                            roomList[x][y][z].attachRoom(roomList[x+1][y+1][z],Room.NORTHEAST);
                        }else{
                            roomList[x][y][z].clearDirection(Room.NORTHEAST);
                        }
                        /* South west */
                        if((x-1) > -1 && (y-1) > -1){
                            roomList[x][y][z].attachRoom(roomList[x-1][y-1][z],Room.SOUTHWEST);
                        }else{
                            roomList[x][y][z].clearDirection(Room.SOUTHWEST);
                        }
                        /* South east */
                        if((x+1) < rows && (y-1) > -1){
                            roomList[x][y][z].attachRoom(roomList[x+1][y-1][z],Room.SOUTHEAST);
                        }else{
                            roomList[x][y][z].clearDirection(Room.SOUTHEAST);
                        }
                    }
                }
            }
            return roomList;
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot open file!" + m_file_name);
            Logger.getLogger(RoomImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Room[0][0][0];
    }
}
