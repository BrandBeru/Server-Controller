/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.View;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.beru.ServerController.App;

/**
 *
 * @author brand
 */
public class Loading {

    public static ProgressBar bar = new ProgressBar();
    public static Label text = new Label();

    public Loading(String message) {
        (new Thread(){
            public void run(){
                Dialog<String> pane = new Dialog<String>();
                pane.initOwner(App.stage);
                pane.setTitle(message);

                ButtonType type = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                pane.getDialogPane().getButtonTypes().add(type);

                VBox content = new VBox();
                content.setAlignment(Pos.TOP_LEFT);
                content.setFillWidth(true);
                content.resize(350, 100);

                bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                bar.setPrefWidth(320);
                bar.setPrefHeight(50);

                text.resize(320, 20);

                content.getChildren().add(bar);
                content.getChildren().add(text);

                pane.getDialogPane().setContent(content);

                pane.getDialogPane().setPrefWidth(350);
                pane.getDialogPane().setPrefHeight(100);
                
                pane.show();
            }
        }).start();
    }

    

}
