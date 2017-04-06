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
 */
public class EventBroadcast {

    public enum EventID {
        /* ROOM CONSTANTS */
        ROOM_LEAVE,ROOM_ENTER
    };
    protected static ArrayList<DataPair<EventID,IEventHandler>> m_handler_list;
    
    public static void event(EventID eventId, int dataId) {
        m_handler_list.stream().filter((a) -> (a.getKey().equals(eventId))).forEach((a) -> {
            a.getValue().dispatch(eventId, dataId);
        });
    }
    
    public static <TData> int registerHandler(EventID eventId,IEventHandler<TData> handler){
        m_handler_list.add(new DataPair(eventId,handler));
        return 0;
    }
}
