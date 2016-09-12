package com.dyonovan.beam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/maingui.fxml"));
        primaryStage.setTitle("Beam Bot");
        primaryStage.setScene(new Scene(root, 400, 325));
        primaryStage.setResizable(false);
        primaryStage.show();

        Points.loadPoints();
    }

    @Override
    public void stop() {
        Points.savePoints();
    }
}
