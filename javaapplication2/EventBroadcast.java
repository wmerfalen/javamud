/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import javaapplication2.interfaces.IEventHandler;
import java.util.ArrayList;

/**
 *
 * @author wmerfalen
 */
public class EventBroadcast {

    public enum EventID {
        /* ROOM CONSTANTS */
        ROOM_LEAVE,ROOM_ENTER
    };

    /**
     *
     */
    protected static ArrayList<DataPair<EventID,IEventHandler>> m_handler_list;
    
    public static void init(){
        m_handler_list = new ArrayList<DataPair<EventID,IEventHandler>>();
    }
    
    public static void event(EventID eventId, int dataId) {
        m_handler_list.stream().filter((a) -> (a.getKey().equals(eventId))).forEach((a) -> {
            a.getValue().dispatch(eventId, dataId);
        });
    }
    
    public static <TData> int registerHandler(EventID eventId,IEventHandler<TData> handler){
        try{
            DataPair<EventID,IEventHandler> m = new DataPair<EventID,IEventHandler>(eventId,handler);
            m_handler_list.add(m);
        }catch(Exception e){
            System.out.println("javaapplication2.EventBroadcast.registerHandler() EXCEPTION " + e.getLocalizedMessage());
        }
        return 0;
    }
}
