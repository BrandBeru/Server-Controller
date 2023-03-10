/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author brand
 */
public class Sql {

    private static final Logger log;

    public static Connection con;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");
        log = Logger.getLogger(Sql.class.getName());
    }

    public static boolean azure_connect() {
        try {
            log.info("Loading application properties");
            Properties properties = new Properties();
            properties.load(Sql.class.getClassLoader().getResourceAsStream("Properties/AzureConnection.properties"));

            log.info("Connecting to the database");
            con = DriverManager.getConnection(properties.getProperty("url"), properties);
            log.info("Database connection test: " + con.getCatalog());

            return !con.isClosed();

            /*
		Todo todo = new Todo(1L, "configuration", "congratulations, you have set up JDBC correctly!", true);
        insertData(todo, connection);
        todo = readData(connection);
        todo.setDetails("congratulations, you have updated data!");
        updateData(todo, connection);
        deleteData(todo, connection);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean mysql_connect(CacheDatas data, String pass) {
        try {
            log.info("Loading application properties");
            
            String url = "jdbc:mysql://"+data.getHost()+":"+data.getPorts().get(1)+"/"+data.getUser();

            log.info("Connecting to the database");
            con = DriverManager.getConnection(url, data.getUser(), pass);
            log.info("Database connection test: " + con.getCatalog());

            return !con.isClosed();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Object> getProject(String folder) {
        List<Object> values = new ArrayList<>();
        try {
            String query = "select * from files where name=" + folder;
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                values.add(rs.getString(2));
                values.add(rs.getInt(4));
                values.add(rs.getInt(5));
                values.add(rs.getInt(6));
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getProjectInfo(String name, String info){
        String value = "";
        try{
            String query = "SELECT "+info+" FROM info WHERE name=?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, name);
            
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                value = rs.getObject(1).toString();
            }
            return value;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ProjectInfo> getProjects() {
        List<ProjectInfo> values = new ArrayList<>();
        try {
            String query = "select * from files ";
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ProjectInfo info = new ProjectInfo(rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(6));
                values.add(info);
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ProjectInfo getProjectDetails(ProjectInfo info, String password) {
        ProjectInfo pInfo = info;
        try {
            String query = "select * from info where name='" + info.getName() + "' and password='" + password + "';";
            System.out.println("QUERY: " + query);
            Statement ps = con.createStatement();

            ResultSet rs = ps.executeQuery(query);

            while (rs.next()) {
                pInfo.setType(rs.getString(2));
                pInfo.setDate(rs.getDate(3));
                pInfo.setLast_modified(rs.getDate(4));
                pInfo.setVersion(rs.getString(5));
                pInfo.setRemotePath(rs.getString(6));
            }
            return pInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public static boolean createProject(String name, String password, String type, boolean remote, LocalDate currentDate, String path) {
        try {
            String query = "INSERT INTO files (name, password, remote) VALUES (?,?,?)";

            System.out.println("Files");

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setBoolean(3, remote);

            stmt.execute();
            createInfo(name, password, type, currentDate, path);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void createInfo(String name, String pass, String type, LocalDate currentDate, String path) {
        try {
            String query = "INSERT INTO info (type, created, name, password, path) VALUES (?,?,?,?,?)";

            System.out.println("Project Info");

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, type);
            stmt.setDate(2, java.sql.Date.valueOf(currentDate));
            stmt.setString(3, name);
            stmt.setString(4, pass);
            stmt.setString(5, path);
            
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean deleteProject(String name){
        try{
            String query = "DELETE FROM info WHERE name=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            
            stmt.executeUpdate();
            
            String query2 = "DELETE FROM files WHERE name=?";
            stmt = con.prepareStatement(query2);
            stmt.setString(1, name);
            
            stmt.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }public static boolean updateValue(String table, String column, Object value, String project){
        try{
            String query = "update "+table+" set " + column + "="+value+" where name=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, project);
            
            stmt.executeUpdate();
            
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
