package com.dyonovan.beam.response;

import java.util.List;
import java.util.Map;

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
public class TactilesListing {
    public int id;
    public String type;
    public List<TactileBlueprint> blueprint;
    public Map<String, Boolean> analysis;
    public String text;
    public String help;
    public Map<String, Map<String, Integer>> cost;
    public Map<String, Integer> cooldown;

    public TactilesListing(int id, String type, List<TactileBlueprint> blueprint, Map<String, Boolean> analysis, String text, String help, Map<String, Map<String, Integer>> cost, Map<String, Integer> cooldown) {
        this.id = id;
        this.type = type;
        this.blueprint = blueprint;
        this.analysis = analysis;
        this.text = text;
        this.help = help;
        this.cost = cost;
        this.cooldown = cooldown;
    }
}
