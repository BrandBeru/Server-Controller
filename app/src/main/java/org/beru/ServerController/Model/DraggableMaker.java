/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import javafx.scene.Node;

/**
 *
 * @author brand
 */
public class DraggableMaker {
    
    private double mouseAnchorX;
    private double mouseAnchorY;
        
    
    public void make(Node object){
        object.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
        object.setOnMouseDragged(mouseEvent -> {
            object.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            object.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
        });
    }
    
    
    
}
