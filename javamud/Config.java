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
public class Config {
    public String mysql_host;
    public String mysql_user;
    public String mysql_pass;
    public String mysql_db;
    public boolean autoload_mysql;
    public String migration_dir;
    
    public Config(){
        mysql_host = "localhost";
        mysql_user = "javamud";
        mysql_db = "javamud";
        mysql_pass = "javamud1234";
        autoload_mysql = true;
        migration_dir = System.getProperty("user.dir") + "/migrations/";
    }
}
