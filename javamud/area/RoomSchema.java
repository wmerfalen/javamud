/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamud.area;

import javamud.mysql.Mysql;
import javamud.mysql.Static;
import org.flywaydb.core.Flyway;

/**
 *
 * @author wmerfalen
 */
public class RoomSchema {
    private Mysql m_db = null;
    private Flyway m_fly = null;
    public RoomSchema(){
        m_db = Static.mysql;
        m_fly = new Flyway();
    }
    
    public void migrate(String migrationDir){
        m_fly.setDataSource(m_db.getDataSourceBaseUrl(),m_db.getUser(),m_db.getPass());
    }
}
