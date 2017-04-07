/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.nio.ByteBuffer;

/**
 *
 * @author wmerfalen
 */
public class Util {
    public static String bb2str(ByteBuffer buf){
        StringBuilder sb = new StringBuilder(buf.limit());
        buf.rewind();
        while (buf.remaining() > 0){
            char c = (char)buf.get();
            if (c == '\0') break;
            sb.append(c);
        }
        return sb.toString();
    }
    
    public static ByteBuffer str2bb(String s){
        ByteBuffer b = ByteBuffer.allocate(s.length());
        b.put(s.getBytes(), 0,s.length());
        return b;
    }
    
}
