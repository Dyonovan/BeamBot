package com.dyonovan.beam;

import com.dyonovan.beam.controllers.AddControlGuiController;
import com.dyonovan.beam.controllers.MainGuiController;
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

    static MainGuiController mainGuiController;
    public static AddControlGuiController addControlGuiController;
    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Beam.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
        Parent root = loader.load();
        Beam.mainGuiController = loader.getController();
        primaryStage.setTitle("Beam Bot");
        primaryStage.setScene(new Scene(root, 600, 600));
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
        if (Interactive.robot != null)
            Interactive.stopInteractive();
        Points.savePoints();
        Properties.saveProperties();
        Platform.exit();
        System.exit(0);
    }
}
