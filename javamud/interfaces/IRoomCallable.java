/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud.interfaces;

import javamud.area.Room;

/**
 *
 * @author wmerfalen
 */
public interface IRoomCallable {
    public enum IRCReturnStatus {
        IRC_RETURN_CURRENT,IRC_KEEP_ITERATING,IRC_RETURN_NOTHING_AND_STOP
    };
    public IRCReturnStatus call(Room r);
}
