/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author wmerfalen
 */
public class SocketServer {
    protected ArrayList<SocketChannel> m_sockets;
    protected ServerSocketChannel m_server;
    protected ArrayList<DataPair<SocketChannel,String>> m_read_buffers;
    protected ArrayList<SocketChannel> m_needs_auth;
    
    SocketServer() throws Exception{
        throw new Exception("Please call the Socket(Integer port) constructor");
    }
    
    SocketServer(Integer port){
        m_read_buffers = new ArrayList<DataPair<SocketChannel,String>>();
        m_needs_auth = new ArrayList<SocketChannel>();
        try{
            m_server = ServerSocketChannel.open();
            m_server.socket().bind(new InetSocketAddress(port));
            m_server.socket().setSoTimeout(100);
            m_server.configureBlocking(false);
        }catch(Exception e){
            System.out.println("javaapplication2.SocketServer.accept() Server socket error: " + e.getLocalizedMessage());
        }
        m_sockets = new ArrayList<SocketChannel>();
    }
    
    public Integer accept(){
        Integer acceptCount = 0;
        try{
            SocketChannel clientSocket = m_server.accept();
            if(clientSocket == null){
                return 0;
            }
            m_sockets.add(clientSocket);
            m_needs_auth.add(clientSocket);
            acceptCount++;
        }catch(Exception e){
            System.out.println("javaapplication2.JavaApplication2.main() " + e.getMessage());
        }
        return acceptCount;
    }
    
    public String uniqid(){
        return UUID.randomUUID().toString();
    }
    
    public boolean readBufferReady() throws IOException{
        ByteBuffer buf = ByteBuffer.allocate(1024);
        boolean read = false;
        for(SocketChannel s: m_sockets){
            if(s.read(buf) > 0){
                m_read_buffers.add(new DataPair(s,Util.bb2str(buf)));
                read = true;
            }
            buf.clear();
        }
        return read;
    }
    
    public ArrayList<DataPair<SocketChannel,String>> getReadBuffers(){
        return m_read_buffers;
    }
    
    public Integer authorizeSocket(SocketChannel s){
        if(m_needs_auth.contains(s)){
            m_needs_auth.remove(s);
            return 1;
        }
        return 0;
    }

}
