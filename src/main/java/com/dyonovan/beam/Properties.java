package com.dyonovan.beam;

import java.io.*;

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
public class Properties {

    public static void saveProperties() {
        java.util.Properties prop = new java.util.Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("config.properties");

            prop.setProperty("Username", Beam.mainGuiController.txtUserName.getText());
            prop.setProperty("Password", Beam.mainGuiController.txtPassword.getText()); //TODO maybe encrypt
            prop.setProperty("Channel", Beam.mainGuiController.txtChannelName.getText());
            prop.setProperty("Save", Beam.mainGuiController.chkSave.isSelected() ? "Yes" : "No");
            prop.setProperty("BeamOauth", Beam.mainGuiController.interactiveOauth.getText());

            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }

    static void loadProperties() {
        java.util.Properties prop = new java.util.Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");

            prop.load(input);

            Beam.mainGuiController.txtUserName.setText(prop.getProperty("Username"));
            Beam.mainGuiController.txtPassword.setText(prop.getProperty("Password"));
            Beam.mainGuiController.txtChannelName.setText(prop.getProperty("Channel"));
            Beam.mainGuiController.chkSave.setSelected(prop.getProperty("Save").equals("Yes"));
            Beam.mainGuiController.interactiveOauth.setText(prop.getProperty("BeamOauth"));

        } catch (IOException ignored) {

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {

                }
            }
        }
    }
}
