/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.beru.ServerController.Model.CacheProjectSelected;
import org.beru.ServerController.Model.Ftp;
import org.beru.ServerController.Model.Encypter;
import org.beru.ServerController.Model.ProjectInfo;
import org.beru.ServerController.Model.Sql;

/**
 *
 * @author brand
 */
public class ProjectInfoController implements Initializable {

    @FXML
    private Label dateLBL;
    @FXML
    private Label lastLBL;
    @FXML
    private Label pathLBL;
    @FXML
    private Label versionLBL;
    @FXML
    private Label remoteLBL;
    @FXML
    private PasswordField passwordTXT;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Circle updated;
    @FXML
    private TitledPane parent;

    ProjectInfo p;

    public ProjectInfoController(ProjectInfo pInfo) {
        p = pInfo;
    }

    private boolean auth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDatas();
    }

    private void loadDatas() {
        double pro = (p.getProgress());
        double div = pro / 100;

        String remote = (p.getRemote() == 1) ? "Remote" : "Local";
        remoteLBL.setText(remote);
        progress.setProgress(div);
        Color u = (p.getUpddated() == 1) ? Color.BLUE : Color.RED;
        updated.setFill(u);
        parent.setTextFill(u);

        versionLBL.setText("Version: " + (auth ? p.getVersion() : "PROTECTED"));
        pathLBL.setText("Path: " + (auth ? p.getRemotePath() : "PROTECTED"));
        lastLBL.setText("Last modified: " + (auth ? p.getLast_modified().toString() : "PROTECTED"));
        dateLBL.setText("Creation Date: " + (auth ? p.getDate().toString() : "PROTECTED"));
        parent.setText(p.getName() + " -> " + (auth ? p.getType() : "PROTECTED"));
    }

    @FXML
    private void updateCompiler(ActionEvent event) {

        new Thread() {
            public void run() {
                Ftp.downloadProject(p.getRemotePath());
                Sql.updateValue("files", "updated", 1, p.getName());
            }
        }.start();
        
        loadDatas();
    }

    @FXML
    private void setPasswordConnection(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            p = Sql.getProjectDetails(p, Encypter.encrypt(passwordTXT.getText()));

            auth = (p.getVersion() != null);
            loadDatas();

        }
    }

    @FXML
    private void changeSelected(MouseEvent event) {
        CacheProjectSelected.projectName = p.getName();
        CacheProjectSelected.updated = p.getUpddated();
        CacheProjectSelected.progress = p.getProgress();

        System.out.println(CacheProjectSelected.projectName);

        ApplicationController.pSelected = p.getName();
    }

}
