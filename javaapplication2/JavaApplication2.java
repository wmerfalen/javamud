/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;


import javaapplication2.area.Room;
import javaapplication2.area.RoomImporter;
import javaapplication2.interfaces.IPipeInterface;
import javaapplication2.interfaces.IRoomCallable;
import javaapplication2.interfaces.IEventHandler;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplication2.interfaces.IRoomCallable.IRCReturnStatus;
import static javaapplication2.interfaces.IRoomCallable.IRCReturnStatus.IRC_KEEP_ITERATING;
import static javaapplication2.interfaces.IRoomCallable.IRCReturnStatus.IRC_RETURN_CURRENT;

/**
 *
 * @author wmerfalen
 */
public class JavaApplication2 {
    protected static Room[][][] m_rooms;
    protected static ArrayList<DataPair<Socket,Person>> m_socket_person;
    protected static TextProcessor m_processor;
    
    public static final int EVENT_COMMAND_PROCESS = 0;
    public static final int EVENT_BROADCAST = 1;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        Person mentoc = new Person("mentoc",10);
        Person foobar = new Person("foobar",11);
        ArrayList<Person> clients = new ArrayList<Person>();
        RoomImporter importer = new RoomImporter("rooms.conf");
        m_socket_person = new ArrayList<DataPair<Socket,Person>>();
        m_processor = new TextProcessor();
        m_rooms = importer.generateFromFile();
        
        EventBroadcast.init();
        EventBroadcast.registerHandler(EventBroadcast.EventID.ROOM_ENTER, new EventRoomEnterHandler());
        EventBroadcast.registerHandler(EventBroadcast.EventID.ROOM_LEAVE, new EventRoomLeaveHandler());
        
        SocketServer serv = new SocketServer(4444);
        
        while(true){
            SocketChannel scTemp = null;
            if((scTemp = serv.accept()) != null){
                System.out.println("Client Connected ");
                Person p = new Person();
                PersonPipe pipe = new PersonPipe();
                System.err.println(scTemp.toString());
                pipe.setSocketChannel(scTemp);
                p.setPipeInterface(pipe);
                clients.add(p);
                m_socket_person.add(new DataPair(scTemp.socket(),p));
            }

            String inputLine = null;
            String commandRegex = "^/[a-zA-Z0-9]{1,}";
            DataPair<SocketChannel,String> readBuffer = null;
            if((readBuffer = serv.readBuffer()) != null){
                Person p = getPersonObjectBySocket(readBuffer.getKey());
                if(p != null){
                    DataPair<Integer,String> response = m_processor.parseCommand(p,readBuffer.getValue());
                    switch(response.getKey().intValue()){
                        case JavaApplication2.EVENT_BROADCAST:
                            System.out.println("javaapplication2.JavaApplication2.main() BROADCST: " + response.getValue());
                            break;
                        case JavaApplication2.EVENT_COMMAND_PROCESS:
                            System.out.println("CMD: " + readBuffer.getValue() + ": " + response.getValue());
                            break;
                        default:
                            System.out.println("javaapplication2.JavaApplication2.main() DEFAULT");
                    }
                }
            }
            
            Thread.sleep(100);
        }
        
    }
    
    public static Person getPersonObjectBySocket(SocketChannel s){
        Socket sock = s.socket();
        for(DataPair<Socket,Person> d : m_socket_person){
            if(d.getKey().equals(sock)){
                return d.getValue();
            }
        }
        return null;
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
                    p.pipeWrite(p.id() + " entered the room.");
                }
            }
            return 0;
        }
    }
    private static class EventRoomLeaveHandler implements IEventHandler<DataPair<Person,DataPair<Room,Integer>>> {
        @Override
        public int dispatch(EventBroadcast.EventID eventId, DataPair<Person,DataPair<Room,Integer>> dataId) {
            if(eventId.equals(EventBroadcast.EventID.ROOM_ENTER)){
                Integer[] xy = explodeRoomCoords(dataId.getValue().getKey().coords());
                for(Person p : m_rooms[xy[0]][xy[1]][0].scan()){
                    p.pipeWrite(p.id() + " left the room.");
                }
            }
            return 0;
        }
    }
    
    private static class PersonPipe implements IPipeInterface {
        private SocketChannel m_socket;
        public static Integer PIPE_SOCKET_CLOSED = -1;
        public static Integer PIPE_NO_ACTION = -2;
        
        @Override
        public Integer write(String msg, Integer len) {
            if(!m_socket.isOpen()){
                System.out.println("javaapplication2.JavaApplication2.PersonPipe.write() -- M SOCKET ISNT OPEN");
                return PersonPipe.PIPE_SOCKET_CLOSED;
            }
            try {
                ByteBuffer b = Util.str2bb(msg);
                b.rewind();
                return m_socket.write(b);
            } catch (IOException ex) {
                Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
            }
            return PersonPipe.PIPE_NO_ACTION;
        }

        @Override
        public String read(Integer len) {
            ByteBuffer b = ByteBuffer.allocate(1);
            try {
                int ret = m_socket.read(b);
                if(ret <= 0){
                    return null;
                }
                return Util.bb2str(b);
            } catch (IOException ex) {
                Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public void setSocketChannel(SocketChannel s) {
            m_socket = s;
        }
        
    }
}
