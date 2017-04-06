/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author wmerfalen
 */
public class DataPair<TKeyType,TValueType> {
    private TKeyType m_key;
    private TValueType m_value;
    
    DataPair(){
        m_key = null;
        m_value = null;
    }
    
    DataPair(TKeyType key,TValueType value){
        m_key = key;
        m_value = value;
    }
    
    public TKeyType getKey(){
        return m_key;
    }
    
    public TValueType getValue(){
        return m_value;
    }
    
}