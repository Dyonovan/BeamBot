package com.dyonovan.beam;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pro.beam.api.BeamAPI;

/**
 * This file was created for beam
 * <p>
 * beam is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 9/7/2016
 */
public class Beam extends Application {

    static Controller controller;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/maingui.fxml"));
        Parent root = loader.load();
        Beam.controller = loader.getController();
        primaryStage.setTitle("Beam Bot");
        primaryStage.setScene(new Scene(root, 400, 325));
        primaryStage.setResizable(false);
        primaryStage.show();
        Properties.loadProperties();
        Points.loadPoints();
        Chat.beam = new BeamAPI();
    }

    @Override
    public void stop() {
        if (Chat.chatConnectible != null)
            Chat.chatConnectible.disconnect();
        if (Points.scheduler != null)
            Points.scheduler.shutdownNow();
        Points.savePoints();
        Platform.exit();
        System.exit(0);
    }
}
