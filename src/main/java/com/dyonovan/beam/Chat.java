package com.dyonovan.beam;

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
class Chat {

    static BeamChatConnectable chatConnectible;
    static BeamAPI beam;
    static BeamUser user;
    static String username;
    static String password;
    static boolean isConnected = false;

    public void connect(String username, String password) throws ExecutionException, InterruptedException {
        Chat.username = username;
        Chat.password = password;
        beam = new BeamAPI();
        user = beam.use(UsersService.class).login(username, password).get();
        BeamChat chat = beam.use(ChatService.class).findOne(user.channel.id).get();
        chatConnectible = chat.connectable(beam);

        if (chatConnectible.connect()) {
            chatConnectible.send(AuthenticateMessage.from(user.channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
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
            switch (event.data.message.message.get(0).text) {
                case "!ping":
                    chatConnectible.send(ChatSendMethod.of(String.format("@%s PONG!",event.data.userName)));
                    break;
                case "!spend":

                    break;
            }
        });
    }
}