/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud;

/**
 *
 * @author wmerfalen
 */
class TextProcessor {
    
    public DataPair<Integer,String> parseCommand(Person p,String cmd){
        //TODO: parse potential commands
        return new DataPair(0,"LOL");
    }
    
    public boolean authenticate(Person p,String readBuffer){
        //TODO: authenticate the user here
        //TODO: set person as being authenticated int he Person class
        return true;
    }
}
