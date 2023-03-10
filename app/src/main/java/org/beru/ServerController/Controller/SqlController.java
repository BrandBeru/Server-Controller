/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Controller;

import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.beru.ServerController.Model.DraggableMaker;
import org.beru.ServerController.Model.Sql;
import org.beru.ServerController.Model.SystemInformation;
import org.beru.ServerController.View.Messages;

/**
 *
 * @author brand
 */
public class SqlController implements Initializable{

    @FXML
    private VBox sqlPane;
    @FXML
    private Circle sql_connect;
    @FXML
    private Label hostTXT;
    @FXML
    private Label portTXT;
    @FXML
    private Label serverVersionTXT;
    @FXML
    private Label serverNameTXT;
    @FXML
    private Label serverIPTXT;
    @FXML
    private Label clientVersionTXT;
    @FXML
    private Label clientNameTXT;
    @FXML
    private Label clientIPTXT;
    @FXML
    private Label operatingSystemTXT;
    @FXML
    private Label cpuTXT;
    
    private boolean con;
    @FXML
    private Button conBTN;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new DraggableMaker().make(sqlPane);
        
        getStatus();
        
    }
    
    
    public void getStatus(){
        try{
            con = !Sql.con.isClosed();
            
            sql_connect.setFill((con ? Color.BLUE : Color.RED));
                        
            String[] infoLocal = (con ? Sql.con.getMetaData().getUserName().split("@" ) : null);
            
            hostTXT.setText("Host: " + (con ? ApplicationController.datas.getHost() : ""));
            portTXT.setText("Port: " + (con ? ApplicationController.datas.getPorts().get(1) : ""));
            
            //Server
            serverNameTXT.setText("Name: " + (con ? infoLocal[0] : ""));
            serverIPTXT.setText("IP: " + (con ? Sql.con.getMetaData().getURL() : ""));
            serverVersionTXT.setText("Version: " + (con ? Sql.con.getMetaData().getDatabaseProductVersion() : ""));
            
            //CLient
            clientVersionTXT.setText("Version: "+ (con ? Sql.con.getMetaData().getDriverVersion() : ""));
            clientNameTXT.setText("Name: "+ (con ? infoLocal[0] : ""));
            clientIPTXT.setText("IP: "+ (con ? infoLocal[1] : ""));
            
            //Machine
            operatingSystemTXT.setText("Operating System: " + System.getProperty("os.name"));
            cpuTXT.setText("Hardware: " + SystemInformation.cpu());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void dis_connect(ActionEvent event) {
        con = !con;
        
        try{
            if(!con){
                Sql.con.close();
                getStatus();
                conBTN.setText("Connec");
            }else{
                String pass = Messages.password("Authentification");
                String url = "jdbc:mysql://"+ApplicationController.datas.getHost()+":"+ApplicationController.datas.getPorts().get(1);
                Sql.con = DriverManager.getConnection(url, ApplicationController.datas.getUser(), pass);
                conBTN.setText("Disconnec");
                
                getStatus();
            }
            ApplicationController.instance.infoCon();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
}
