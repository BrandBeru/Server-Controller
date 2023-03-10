/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Controller;

import java.net.URL;
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
import org.beru.ServerController.Model.Ftp;
import org.beru.ServerController.View.Messages;

/**
 *
 * @author brand
 */
public class FtpController implements Initializable {

    @FXML
    private VBox ftpPanel;
    @FXML
    private Circle ftp_connect;
    @FXML
    private Label hostTXT;
    @FXML
    private Label portTXT;
    @FXML
    private Label userTXT;
    @FXML
    private Label timeoutTXT;
    
    boolean con;
    @FXML
    private Button buttonCon;
    @FXML
    private Label localNameTXT;
    @FXML
    private Label localHostTXT;
    @FXML
    private Label localPortTXT;
    @FXML
    private Label ftp_name;
    @FXML
    private Label nameTXT;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new DraggableMaker().make(ftpPanel);
        con = Ftp.isConnected;
        getStatus();
        
    }

    @FXML
    private void dis_connect(ActionEvent event) {
        con = !con;
        try{
            if(!con){
                Ftp.ftp.disconnect();
                buttonCon.setText("Connect");
            }else{
                String pass = Messages.password("Authentification");
                Ftp.ftp.connect(ApplicationController.datas.getHost(), ApplicationController.datas.getPorts().get(0));
                Ftp.ftp.login(ApplicationController.datas.getUser(), pass);
                buttonCon.setText("Disconnect");
            }
            ApplicationController.instance.infoCon();
        }catch(Exception e){
            e.printStackTrace();
        }
        getStatus();
    }
    
    private void getStatus(){
        con = Ftp.ftp.isConnected();
        
        ftp_connect.setFill((con ? Color.BLUE : Color.RED));
        
        localHostTXT.setText("Local Host: "+ (con ? Ftp.ftp.getLocalAddress().getHostAddress() : ""));
        localNameTXT.setText("Local Machine Name: "+ ( con ? Ftp.ftp.getLocalAddress().getHostName() : ""));
        localPortTXT.setText("Local Port: "+ (con ? Ftp.ftp.getLocalPort() : ""));
        
        hostTXT.setText("Host: " + (con ? Ftp.ftp.getRemoteAddress().getHostAddress() : ""));
        nameTXT.setText("Mahcine Name: " + (con ? Ftp.ftp.getRemoteAddress().getHostName() : ""));
        portTXT.setText("Port: " + (con ? Ftp.ftp.getRemotePort() : ""));
        userTXT.setText("User: " + (con ? ApplicationController.datas.getUser() : ""));
        timeoutTXT.setText("Timeout: " + (con ? Ftp.ftp.getDataTimeout().toString() : "" ));
    }
}
