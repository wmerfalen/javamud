/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2.interfaces;

import javaapplication2.EventBroadcast.EventID;

/**
 *
 * @author wmerfalen
 */
public interface IEventHandler<TDataType> {
    public int dispatch(EventID eventId,TDataType dataId);
}
