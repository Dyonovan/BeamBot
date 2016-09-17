package com.dyonovan.beam.controllers;

import com.dyonovan.beam.Beam;
import com.dyonovan.beam.Chat;
import com.dyonovan.beam.Interactive;
import com.dyonovan.beam.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.response.users.UserSearchResponse;
import pro.beam.api.services.impl.UsersService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * This file was created for beam
 * <p>
 * beam is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 9/12/2016
 */
public class MainGuiController implements Initializable {

    @FXML
    public
    TextField txtUserName;
    @FXML
    public
    TextField txtPassword;
    @FXML
    TextArea txtLog;
    @FXML
    public
    TextField txtChannelName;
    @FXML
    public
    CheckBox chkSave;
    @FXML
    public TextField interactiveOauth;


    public BeamChannel beamChannel;
    public static Stage addNew;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void AddNew_onClick(ActionEvent actionEvent) throws Exception{
        addNew = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewControlGui.fxml"));
        Beam.addControlGuiController = loader.getController();
        Parent parent = loader.load();
        addNew.setScene(new Scene(parent, 400, 450));
        addNew.setTitle("Add New Control");
        addNew.initModality(Modality.WINDOW_MODAL);
        addNew.initOwner(Beam.primaryStage);
        addNew.setResizable(false);
        //addNew.setAlwaysOnTop(true);
        addNew.setHeight(400.0);
        addNew.setWidth(400.0);
        addNew.show();
    }

    public void StartInteractive_onClick(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        Interactive.connect("127.0.0.1", 25589, "test123");
    }

    public void ConnectChat_onClick(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        if (txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Username or Password Missing");
            alert.setHeaderText(null);
            alert.setContentText("Please fill out the Beam Username and Password field on the Settings Tab");
            alert.show();

            return;
        }

        UserSearchResponse response =  Chat.beam.use(UsersService.class).search(txtChannelName.getText()).get();
        if (response.size() != 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't Find Users Channel");
            alert.setHeaderText(null);
            alert.setContentText("Please check the Beam Username who's channel you are trying to connect to in the Setting Tab");
            alert.show();

            return;
        }
        beamChannel = response.get(0).channel;

        boolean didConnect =  Chat.connect(txtUserName.getText(), txtPassword.getText(), beamChannel);
        if (!didConnect) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Username or Password");
            alert.setHeaderText(null);
            alert.setContentText("Wrong Beam Username or Password. Please check and correct on the Settings Tab");
            alert.show();
            return;
        }
        updateLog("Connect to Beam Chat...");

        if (chkSave.isSelected())
            Properties.saveProperties();
    }

    public void updateLog(String text) {
        this.txtLog.appendText(text + "\n");
    }

}
