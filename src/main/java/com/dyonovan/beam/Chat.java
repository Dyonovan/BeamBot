package com.dyonovan.beam;

import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.resource.chat.ws.BeamChatConnectable;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;

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
public class Chat {

    static BeamChatConnectable chatConnectible;
    public static BeamAPI beam;
    static BeamUser user;
    static BeamChat chat;
    static String username;
    static String password;
    static boolean isConnected = false;

    public static boolean connect(String username, String password, BeamChannel channel) throws InterruptedException {
        Chat.username = username;
        Chat.password = password;
        try {
            user = beam.use(UsersService.class).login(username, password).get();
        } catch (ExecutionException e) {
            return false;
        }
        try {
            chat = beam.use(ChatService.class).findOne(channel.id).get();
        } catch (ExecutionException e) {
            return false;
        }
        chatConnectible = chat.connectable(beam);

        if (chatConnectible.connect()) {
            chatConnectible.send(AuthenticateMessage.from(channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    Chat.isConnected = true;
                    chatConnectible.send(ChatSendMethod.of("I am ALIVE!!"));
                    Points.addPoints();
                }

                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }

        chatConnectible.on(IncomingMessageEvent.class, event -> {
            for (int i = 0; i < event.data.message.message.size(); i++) {
                if (event.data.message.message.get(i).text.startsWith("!ping")) {
                    chatConnectible.send(ChatSendMethod.of(String.format("@%s PONG!", event.data.userName)));
                } else if (event.data.message.message.get(i).text.startsWith("#points") ||
                        event.data.message.message.get(i).text.startsWith("#points") ||
                        event.data.message.message.get(i).text.startsWith("#points")) {
                    int points = Points.getPoints(event.data.userName);
                    chatConnectible.send(ChatSendMethod.of(String.format("@%s You have " + points + " points!", event.data.userName)));
                } else if (event.data.message.message.get(i).text.startsWith("!spend")) {
                    String[] msg = event.data.message.message.get(i).text.split(" ");
                    if (msg.length == 1 || msg.length >= 4 || Integer.parseInt(msg[1]) > 10) { //TODO Map id to command
                        chatConnectible.send(ChatSendMethod.of(String.format("@%s Spawn List: <TODO ENTER URL>", event.data.userName)));
                        return;
                    }
                    try {
                        Interactive.doAction(Integer.parseInt(msg[1]), msg[2] != null ? Integer.parseInt(msg[2]) : 1, event.data.userName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.data.message.message.get(i).text.startsWith("#giveall")) { //TODO MAKE SURE USER IS A MOD
                    String[] msg = event.data.message.message.get(i).text.split(" ");
                    if (msg.length != 2) {
                        chatConnectible.send(ChatSendMethod.of(String.format("@%s Usage: #giveall <amount>", event.data.userName)));
                        return;
                    }
                    Points.givePointsToAll(Integer.parseInt(msg[1]));
                }
            }
        });
        return true;
    }
}
