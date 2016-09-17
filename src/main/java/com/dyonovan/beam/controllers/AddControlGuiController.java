package com.dyonovan.beam.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This file was created for beam
 * <p>
 * beam is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 9/17/2016
 */
public class AddControlGuiController implements Initializable  {

    @FXML
    ComboBox commandType;

    @FXML
    Pane paneConsoleCommand;


    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set options for Command Type
        ObservableList<String> commandOptions =
                FXCollections.observableArrayList(
                        "Run Console Command",
                        "Spawn Entity",
                        "Random Enchant",
                        "Give Item");
        commandType.setItems(commandOptions);
        commandType.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch ((String)newValue) {
                case "Run Console Command":
                    paneConsoleCommand.setVisible(true);
                    break;
            }
        });
    }

    public void Cancel_OnClick(ActionEvent event) {
        MainGuiController.addNew.close();
    }
}
