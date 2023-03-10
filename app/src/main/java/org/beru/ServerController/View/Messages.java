/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.View;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.beru.ServerController.App;

/**
 *
 * @author brand
 */
public class Messages {

    public static String password(String title) {
        Dialog<String> pane = new Dialog<String>();
        pane.initOwner(App.stage);
        pane.setTitle(title);

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        pane.getDialogPane().getButtonTypes().add(cancel);
        pane.getDialogPane().getButtonTypes().add(type);

        PasswordField txt = new PasswordField();
        txt.setPromptText("Password:");

        pane.getDialogPane().setContent(txt);

        pane.getDialogPane().setPrefWidth(350);
        pane.getDialogPane().setPrefHeight(100);
        pane.showAndWait();
        
        return txt.getText();
    }

    public static String text(String title) {
        Dialog<String> pane = new Dialog<String>();
        pane.initOwner(App.stage);
        pane.setTitle(title);

        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        pane.getDialogPane().getButtonTypes().add(type);

        TextField txt = new TextField();
        txt.setPromptText("Text:");

        pane.getDialogPane().setContent(txt);

        pane.getDialogPane().setPrefWidth(350);
        pane.getDialogPane().setPrefHeight(100);
        pane.showAndWait();

        return txt.getText();
    }
    public static String info(String title, String info) {
        Dialog<String> pane = new Dialog<String>();
        pane.initOwner(App.stage);
        pane.setTitle(title);

        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        pane.getDialogPane().getButtonTypes().add(type);

        Text txt = new Text(info);

        pane.getDialogPane().setContent(txt);

        pane.getDialogPane().setPrefWidth(350);
        pane.getDialogPane().setPrefHeight(100);
        pane.showAndWait();

        return txt.getText();
    }

}
