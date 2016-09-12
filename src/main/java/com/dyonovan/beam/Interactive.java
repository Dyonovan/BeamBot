package com.dyonovan.beam;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

import java.io.IOException;
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
public class Interactive {

    static Rcon rcon;
    static boolean connected = false;

    public static void connect(String serverAddress, int rconPort, String rconPassword) throws ExecutionException, InterruptedException {
        try {
            rcon = new Rcon(serverAddress, rconPort, rconPassword.getBytes());
        } catch (IOException | AuthenticationException e) {
            e.printStackTrace();
        }
        connected = true;

        Robot robot = new RobotBuilder().username(Chat.username).password(Chat.password).channel(Chat.user.channel.id).build(Chat.beam).get();

        robot.on(Protocol.Report.class, report -> {
            if (report.getTactileCount() > 0) {
                report.getTactileList().stream().filter(tactileInfo -> tactileInfo.getReleaseFrequency() > 0).forEach(tactileInfo -> {
                    try {
                        doAction(tactileInfo.getId(), (int) tactileInfo.getReleaseFrequency(), "test"); //TODO check for response
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public static boolean doAction(int ID, int times, String user) throws IOException {
        for (int i = 0; i < times; i++) {
            switch (ID) {
                case 0: //Set to day
                    rcon.command("time set day");
                    break;
                case 1: //Set to Night
                    rcon.command("time set night");
                    break;
                case 2: //Heal 5 hearts
                    rcon.command("effect Dyonovan 6 1 1"); //TODO
                    break;
                case 3: //Enchant Item in Hand
                    rcon.command(""); //TODO
                    break;
                case 4: //Give Random Potion
                    rcon.command(""); //TODO
                    break;
                case 5: //Throw into Air
                    rcon.command("tp Dyonovan ~ ~20 ~"); //TODO
                    break;
                case 6: //Give XP
                    rcon.command(""); //TODO
                    break;
                case 7: //Strike with Lightning
                    rcon.command(""); //TODO
                    break;
                case 8: //Spawn Creeper
                    String response = rcon.command("cc_spawnentity Dyonovan creeper " + user); //TODO
                    if (response.equalsIgnoreCase("That player cannot be found")) return false;
                    break;
                case 9: //InstaKILL
                    rcon.command("effect Dyonovan 7 1 100"); //TODO
                    break;
                case 10: //Spawn Wither
                    rcon.command(""); //TODO
                    break;
                default:
            }
        }
        return true;
    }
}
