package com.dyonovan.beam;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.dyonovan.beam.Chat.beam;

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
            Beam.controller.updateLog("Could not connect to Minecraft RCon Post...");
            return;
        }
        connected = true;

        Robot robot = new RobotBuilder().username(Beam.controller.txtBeamName.getText()).password(Beam.controller.txtBeamPassword.getText()).channel(Beam.controller.beamChannel.id).build(beam).get();

        if (!robot.isOpen()) {
            Beam.controller.updateLog("Could not create interactive...");
            return;
        }
        else Beam.controller.updateLog("Interactive has Connected..."); //TODO check if interactive is up on stream

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
        String response;
        for (int i = 0; i < times; i++) {
            switch (ID) {
                case 0: //Set to day
                    rcon.command("time set day");
                    break;
                case 1: //Set to Night
                    rcon.command("time set night");
                    break;
                case 2: //Heal 5 hearts
                    response = rcon.command("cc_changehealth Dyonovan 10 " + user);
                    if (response.equalsIgnoreCase("That player cannot be found") || response.equalsIgnoreCase("player is dead")) return false;
                    break;
                case 3: //Enchant Item in Hand
                    rcon.command(""); //TODO
                    break;
                case 4: //Give Random Potion
                    rcon.command(""); //TODO
                    break;
                case 5: //Throw into Air
                    rcon.command("tp Dyonovan ~ ~20 ~"); //todo check if player is dead
                    break;
                case 6: //Give XP
                    rcon.command(""); //TODO
                    break;
                case 7: //Strike with Lightning
                    rcon.command(""); //TODO
                    break;
                case 8: //Spawn Creeper
                    response = rcon.command("cc_spawnentity Dyonovan creeper " + user);
                    if (response.equalsIgnoreCase("That player cannot be found")) return false;
                    break;
                case 9: //InstaKILL
                    response = rcon.command("cc_changehealth Dyonovan -1000 " + user); //TODO
                    if (response.equalsIgnoreCase("That player cannot be found") || response.equalsIgnoreCase("player is dead")) return false;
                    break;
                case 10: //Spawn Wither
                    response = rcon.command("cc_spawnentity Dyonovan witherboss " + user);
                    if (response.equalsIgnoreCase("That player cannot be found")) return false;
                    break;
                default:
            }
        }
        return true;
    }
}
