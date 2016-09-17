package com.dyonovan.beam;

import com.dyonovan.beam.response.*;
import net.kronos.rkon.core.Rcon;
import pro.beam.api.BeamAPI;
import pro.beam.api.http.BeamHttpClient;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

import java.io.IOException;
import java.util.*;
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

    private static final String GAME_NAME = "Minecraft_Interactive";
    private static final String VERSION = "0.1.0";

    private static Rcon rcon;
    private static BeamAPI interactiveBeam;
    private static BeamUser interactiveUser;
    static Robot robot;

    public static void connect(String serverAddress, int rconPort, String rconPassword) throws ExecutionException, InterruptedException {
        /*try {
            rcon = new Rcon(serverAddress, rconPort, rconPassword.getBytes());
        } catch (IOException | AuthenticationException e) {
            Beam.mainGuiController.updateLog("Could not connect to Minecraft RCon...");
            return;
        }*/

        //Connect to Beam and get BeamUser
        interactiveBeam = new BeamAPI(Beam.mainGuiController.interactiveOauth.getText());
        interactiveUser = interactiveBeam.use(UsersService.class).getCurrent().get();
        int gameID = 0;
        int gameVersionID = 0;

        //Get List of Current Games connected to channel
        InteractiveGameListingResponse response = interactiveBeam.http.get("interactive/games/owned", InteractiveGameListingResponse.class, BeamHttpClient.getArgumentsBuilder().put("user", interactiveUser.id).build()).get();

        for (InteractiveGameListing listing : response) {
            if (listing.name.equalsIgnoreCase(GAME_NAME)) {
                gameID = listing.id;
                //Check to see if version already exists in Game
                for (InteractiveGameVersions version : listing.versions) {
                    if (version.version.equalsIgnoreCase(VERSION)) {
                        gameVersionID = version.id;
                        break;
                    }
                }
                break;
            }
        }
        //If game doesn't exist, create new game
        if (gameID == 0) {
            InteractiveGameListing newGame = interactiveBeam.http.post("interactive/games", InteractiveGameListing.class,
                    BeamHttpClient.getArgumentsBuilder().put("ownerId", interactiveUser.id).put("name", GAME_NAME).build()).get();
            gameID = newGame.id;
        }

        TactileBlueprint blueprint = new TactileBlueprint();
        blueprint.width = 3;
        blueprint.height = 1;
        blueprint.grid = "large";
        blueprint.state = "default";
        blueprint.x = 0;
        blueprint.y = 0;
        List<TactileBlueprint> bp = Arrays.asList(blueprint);
        Map<String, Boolean> analysis = new HashMap<>();
        analysis.put("holding", false);
        analysis.put("frequency", true);
        Map<String, Integer> actCost = new HashMap<>();
        actCost.put("cost", 50);
        Map<String, Map<String, Integer>> cost = new HashMap<>();
        cost.put("press", actCost);
        Map<String, Integer> cooldown = new HashMap<>();
        cooldown.put("press", 0);
        TactilesListing tactiles = new TactilesListing(0, "tactiles", bp, analysis, "Yes it Changed.", "", cost, cooldown);

        Tactiles tactile = new Tactiles(50, Arrays.asList(tactiles));

        NewVersionResponse version;
        if (gameVersionID == 0) {
            //Create new version of game
            version = interactiveBeam.http.post("interactive/versions", NewVersionResponse.class, BeamHttpClient.getArgumentsBuilder().put("ownerId", interactiveUser.id)
                    .put("gameId", gameID).put("version", "0.1.0").build()).get();
            gameVersionID = version.id;
        }

        //Update version that already exists.
        version = interactiveBeam.http.put("interactive/versions/" + gameVersionID, NewVersionResponse.class, BeamHttpClient.getArgumentsBuilder().put("ownerId", interactiveUser.id)
                .put("gameId", gameID).put("controls", tactile).build()).get();

        //Set channel to interactive
        interactiveBeam.http.put("channels/" + interactiveUser.channel.id, null, BeamHttpClient.getArgumentsBuilder()
                .put("interactive", true).put("interactiveGameId", version.id).build()).get();


        robot = new RobotBuilder().channel(interactiveUser.channel).build(interactiveBeam, false).get();

        if (!robot.isOpen()) {
            Beam.mainGuiController.updateLog("Could not create interactive...");
            return;
        } else Beam.mainGuiController.updateLog("Interactive has Connected..."); //TODO check if interactive is up on stream

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

    static boolean doAction(int ID, int times, String user) throws IOException {
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
                    if (response.equalsIgnoreCase("That player cannot be found") || response.equalsIgnoreCase("player is dead"))
                        return false;
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
                    if (response.equalsIgnoreCase("That player cannot be found") || response.equalsIgnoreCase("player is dead"))
                        return false;
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

    static void stopInteractive() {

        //Set channel interactive to false and disable robot
        try {
            robot.disconnect();
            interactiveBeam.http.put("channels/" + interactiveUser.channel.id, null, BeamHttpClient.getArgumentsBuilder()
                    .put("interactive", false).build()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
