/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import java.util.Date;
import javafx.scene.Node;

/**
 *
 * @author brand
 */
public class ProjectInfo {
    
    private Node icon;
    private String name;
    private int upddated;
    private int progress;
    private int remote;
    
    private String type;
    private Date date;
    private Date last_modified;
    private String version;
    private String remotePath;

    public ProjectInfo(String name, int upddated, int progress, int remote) {
        this.name = name;
        this.upddated = upddated;
        this.progress = progress;
        this.remote = remote;
    }
        
    public ProjectInfo(){
        
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public Node getIcon() {
        return icon;
    }

    public void setIcon(Node icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUpddated() {
        return upddated;
    }

    public void setUpddated(int upddated) {
        this.upddated = upddated;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRemote() {
        return remote;
    }

    public void setRemote(int remote) {
        this.remote = remote;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(Date last_modified) {
        this.last_modified = last_modified;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public String toString(){
        return "name: " + name + "\n"
                + "version: " + version;
    }
    
    
}
