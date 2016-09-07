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

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException, AuthenticationException {
        String username = args[0];
        String password = args[1];
        BeamAPI beam = new BeamAPI();

        BeamUser user = beam.use(UsersService.class).login(username, password).get();
        BeamChat chat = beam.use(ChatService.class).findOne(user.channel.id).get();
        BeamChatConnectable chatConnectable = chat.connectable(beam);

        Rcon rcon = new Rcon("teambrmodding.com", 25589, "".getBytes()); //AddPassword

        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(user.channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    chatConnectable.send(ChatSendMethod.of("Hello World!"));
                }
                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }

        Robot robot = new RobotBuilder().username(username).password(password).channel(user.channel.id).build(beam).get();

        robot.on(Protocol.Report.class, report -> {
            for (Protocol.Report.TactileInfo tInfo : report.getTactileList()) {
                boolean test = true;
            }
        });

        chatConnectable.on(IncomingMessageEvent.class, event -> {
            if (event.data.message.message.get(0).text.startsWith("!ping")) {
                chatConnectable.send(ChatSendMethod.of(String.format("@%s PONG!",event.data.userName)));
                try {
                    rcon.command("say testing123");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
