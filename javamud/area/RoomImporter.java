/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud.area;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javamud.mysql.Mysql;

/**
 *
 * @author wmerfalen
 */
public class RoomImporter {
    private String m_file_name;
    
    public RoomImporter(String fName){
        m_file_name = fName;
    }
    
    public ArrayList<Room> importFromDB(Integer areaId) throws SQLException, Exception{
        Mysql db;
        db = new Mysql("localhost","javamud","javamud_user","sudorm-rf/");
        PreparedStatement stmt = db.prepare("SELECT * FROM rooms where area_id = ?");
        stmt.setInt(0, areaId.intValue());
        ResultSet res = stmt.executeQuery();
        ArrayList<Room> rooms = new ArrayList<Room>();
        
        ArrayList<Integer> ids = new ArrayList<Integer>();
        while(res.next()){
            int id = res.getInt("id");
            Room r = new Room(id,
                    res.getString("name"),
                    res.getString("description"),
                    res.getInt("x"),
                    res.getInt("y"),
                    res.getInt("z")
            );
            if(res.getInt("dir_north") != 0){
                r.attachRoomId(res.getInt("dir_north"),Room.NORTH);
            }
            if(res.getInt("dir_northeast") != 0){
                r.attachRoomId(res.getInt("dir_northeast"),Room.NORTHEAST);
            }
            if(res.getInt("dir_northwest") != 0){
                r.attachRoomId(res.getInt("dir_northwest"),Room.NORTHWEST);
            }
            if(res.getInt("dir_east") != 0){
                r.attachRoomId(res.getInt("dir_east"),Room.EAST);
            }
            if(res.getInt("dir_west") != 0){
                r.attachRoomId(res.getInt("dir_west"),Room.WEST);
            }
            if(res.getInt("dir_south") != 0){
                r.attachRoomId(res.getInt("dir_south"),Room.SOUTH);
            }
            if(res.getInt("dir_southeast") != 0){
                r.attachRoomId(res.getInt("dir_southeast"),Room.SOUTHEAST);
            }
            if(res.getInt("dir_southwest") != 0){
                r.attachRoomId(res.getInt("dir_south"),Room.SOUTHWEST);
            }
            rooms.add(r);
        }
        return rooms;
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
                if(roomName != null && roomDesc != null){
                    rooms.add(new Room(roomName,roomDesc,x,y,z));
                }
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
