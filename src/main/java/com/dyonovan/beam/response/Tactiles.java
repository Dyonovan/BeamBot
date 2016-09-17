package com.dyonovan.beam.response;

import java.util.ArrayList;
import java.util.List;

/**
 * This file was created for beam
 * <p>
 * beam is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 9/15/2016
 */
public class Tactiles {
    public int reportInterval;
    public List<TactilesListing> tactiles;
    public List<JoystickListing> joysticks;
    public List<ScreenListing> screens;

    public Tactiles(int reportInterval, List<TactilesListing> tactiles) {
        this.reportInterval = reportInterval;
        this.tactiles = tactiles;
        this.joysticks = new ArrayList<>();
        this.screens = new ArrayList<>();
    }

    private class JoystickListing {
    }

    private class ScreenListing {
    }
}
