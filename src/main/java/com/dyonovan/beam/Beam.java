package com.dyonovan.beam;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.resource.chat.ws.BeamChatConnectable;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;
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
 * @since 9/7/2016
 */
public class Beam {

    private static Rcon rcon;
    static BeamChatConnectable chatConnectible;

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException, AuthenticationException {
        String username = args[0];
        String password = args[1];
        BeamAPI beam = new BeamAPI();

        BeamUser user = beam.use(UsersService.class).login(username, password).get();
        BeamChat chat = beam.use(ChatService.class).findOne(user.channel.id).get();
        chatConnectible = chat.connectable(beam);

        rcon = new Rcon("teambrmodding.com", 25589, "test123".getBytes()); //AddPassword

        if (chatConnectible.connect()) {
            chatConnectible.send(AuthenticateMessage.from(user.channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    chatConnectible.send(ChatSendMethod.of("Hello World!"));
                    Points.addPoints();
                }
                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }

        Robot robot = new RobotBuilder().username(username).password(password).channel(user.channel.id).build(beam).get();


        robot.on(Protocol.Report.class, report -> {
            if (report.getTactileCount() > 0) {
                for (Protocol.Report.TactileInfo tactileInfo : report.getTactileList()) {
                    if (tactileInfo.getReleaseFrequency() > 0) {
                        //System.out.println("Button pressed was " + tactileInfo.getId());
                        try {
                            doAction(tactileInfo.getId(), (int)tactileInfo.getReleaseFrequency());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        chatConnectible.on(IncomingMessageEvent.class, event -> {
            switch (event.data.message.message.get(0).text) {
                case "!ping":
                    chatConnectible.send(ChatSendMethod.of(String.format("@%s PONG!",event.data.userName)));
                    break;
                case "!spend":

                    break;
            }
        });
    }

    private static void doAction(int ID, int times) throws IOException {
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
                    rcon.command(""); //TODO
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
    }
}
