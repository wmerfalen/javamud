/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 *
 * @author wmerfalen
 */
public class SocketServer {
    protected ArrayList<SocketChannel> m_sockets;
    protected ServerSocketChannel m_server;
    protected ArrayList<DataPair<SocketChannel,String>> m_read_buffers;
    
    SocketServer() throws Exception{
        throw new Exception("Please call the Socket(Integer port) constructor");
    }
    
    SocketServer(Integer port){
        m_read_buffers = new ArrayList<DataPair<SocketChannel,String>>();
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
    
    public SocketChannel accept(){
        SocketChannel clientSocket = null;
        try{
            clientSocket = m_server.accept();
            if(clientSocket == null){
                return null;
            }
            m_sockets.add(clientSocket);
        }catch(Exception e){
            System.out.println("javaapplication2.JavaApplication2.main() " + e.getMessage());
        }
        return clientSocket;
    }
    
    public String uniqid(){
        return UUID.randomUUID().toString();
    }
    
    public DataPair<SocketChannel,String> readBuffer() throws IOException{
        ByteBuffer buf = ByteBuffer.allocate(1024);
        for (SocketChannel s : m_sockets) {
            if(s.read(buf) > 0){
                return new DataPair(s,Util.bb2str(buf));
            }
        }
        return null;
    }
    
    public ArrayList<DataPair<SocketChannel,String>> getReadBuffers(){
        return m_read_buffers;
    }
}
