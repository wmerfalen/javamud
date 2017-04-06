/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.IOException;
import java.util.ArrayList;
import javaapplication2.IRoomCallable.IRCReturnStatus;
import static javaapplication2.IRoomCallable.IRCReturnStatus.*;
import static javaapplication2.Room.*;

/**
 *
 * @author wmerfalen
 */
public class JavaApplication2 {
    protected static Room[][][] m_rooms;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Person mentoc = new Person("mentoc",10);
        Person foobar = new Person("foobar",11);
        RoomImporter importer = new RoomImporter("rooms.conf");
        
        m_rooms = importer.generateFromFile();

        EventBroadcast.registerHandler(EventBroadcast.EventID.ROOM_ENTER, new EventRoomEnterHandler());
    }
    
    public static Integer[] explodeRoomCoords(String coords){
        String[] s = coords.split(":");
        Integer[] foo = new Integer[2];
        foo[0] = Integer.decode(s[0]);
        foo[1] = Integer.decode(s[1]);
        return foo;
    }
    
    /**
     *
     * @param p
     * @return
     * @throws Exception
     */
    public static Room findPerson(Person p) throws Exception{
        return runAgainstRooms((Room r) -> {
            if(r.scan().contains(p)) {
                System.out.println(".call() Person: " + p.id() + " found in Room: " + r.coords());
                return IRC_RETURN_CURRENT;
            }else{
                return IRC_KEEP_ITERATING;
            }
        });
    }
    
    /**
     *
     * @param cb
     * @return
     * @throws java.lang.Exception
     */
    public static Room runAgainstRooms(IRoomCallable cb) throws Exception{
        for (Room[][] m_room : m_rooms) {
            for (Room[] m_room1 : m_room) {
                IRCReturnStatus returnValue = cb.call(m_room1[0]);
                switch (returnValue) {
                    case IRC_RETURN_CURRENT:
                        return m_room1[0];
                    case IRC_KEEP_ITERATING:
                        continue;
                    case IRC_RETURN_NOTHING_AND_STOP:
                        return null;
                    default:
                        throw new Exception("Unhandled return value from IRoomCallable: " + returnValue);
                }
            }
        }
        return null;
    }

    private static class EventRoomEnterHandler implements IEventHandler<DataPair<Person,DataPair<Room,Integer>>> {
        @Override
        public int dispatch(EventBroadcast.EventID eventId, DataPair<Person,DataPair<Room,Integer>> dataId) {
            if(eventId.equals(EventBroadcast.EventID.ROOM_ENTER)){
                Integer[] xy = explodeRoomCoords(dataId.getValue().getKey().coords());
                for(Person p : m_rooms[xy[0]][xy[1]][0].scan()){
                    //p.print(p.id() + " entered the room.");
                }
            }
            return 0;
        }
    }
}
