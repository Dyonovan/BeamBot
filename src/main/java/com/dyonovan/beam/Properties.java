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
class Properties {

    static void saveProperties() {
        java.util.Properties prop = new java.util.Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("config.properties");

            prop.setProperty("Username", Beam.controller.txtUserName.getText());
            prop.setProperty("Password", Beam.controller.txtPassword.getText()); //TODO maybe encrypt
            prop.setProperty("Channel", Beam.controller.txtChannelName.getText());
            prop.setProperty("Save", Beam.controller.chkSave.isSelected() ? "Yes" : "No");
            prop.setProperty("BeamUsername", Beam.controller.txtBeamName.getText());
            prop.setProperty("BeamPassword", Beam.controller.txtBeamPassword.getText());

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

            Beam.controller.txtUserName.setText(prop.getProperty("Username"));
            Beam.controller.txtPassword.setText(prop.getProperty("Password"));
            Beam.controller.txtChannelName.setText(prop.getProperty("Channel"));
            Beam.controller.chkSave.setSelected(prop.getProperty("Save").equals("Yes"));
            Beam.controller.txtBeamName.setText(prop.getProperty("BeamUsername"));
            Beam.controller.txtBeamPassword.setText(prop.getProperty("BeamPassword"));

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
