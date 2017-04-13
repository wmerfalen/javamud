/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud.mysql;

/**
 *
 * @author wmerfalen
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javamud.Config;
import javamud.area.Room;
import javax.sql.DataSource;

public class Mysql {
    private Connection m_connect = null;
    private String m_db_host = "localhost";
    private String m_db_db = null;
    private String m_db_user = null;
    private String m_db_pass = null;
    private boolean m_loaded = false;
    private String m_data_source = null;
    public Mysql(){
        m_connect = null;
        m_db_user = null;
        m_db_pass = null;
        m_db_db = null;
        m_db_host = "localhost";
        m_loaded = false;
    }
    
    public Mysql(Config conf) throws Exception{
        m_db_user = conf.mysql_user;
        m_db_pass = conf.mysql_pass;
        m_db_db = conf.mysql_db;
        m_db_host = conf.mysql_host;
        m_data_source = "jdbc:mysql://" + m_db_host + "/" + m_db_db + "?" + "user=" + m_db_user + "&password=" + m_db_pass;
        if(conf.autoload_mysql){
            try{
                load();
            }catch(Exception e){
                throw e;
            }
        }
    }
    
    /**
     *
     * @param host
     * @param db
     * @param user
     * @param pass
     */
    public Mysql(String host,String db,String user,String pass){
        m_db_host = host;
        m_db_db = db;
        m_db_user = user;
        m_db_pass = pass;
    }
    
    public String getDataSourceBaseUrl(){
        return "jdbc:mysql://" + m_db_host + "/" + m_db_db;
    }
    
    public String getUser(){
        return m_db_user;
    }

    public String getPass(){
        return m_db_pass;
    }
    
    public void load() throws Exception {
        if(m_data_source == null){
            m_data_source = "jdbc:mysql://" + m_db_host + "/" + m_db_db + "?" + "user=" + m_db_user + "&password=" + m_db_pass;
        }
        m_connect = DriverManager.getConnection(m_data_source);
    }

    public PreparedStatement prepare(String query) throws SQLException, Exception{
        if(!m_loaded){
            this.load();
        }
        return m_connect.prepareStatement(query);
    }

    public void close() throws Exception {
        try {
            if (m_connect != null) {
                m_connect.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDataSource() {
        return m_data_source;
    }
    
}