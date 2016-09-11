package com.dyonovan.beam;

import pro.beam.api.resource.chat.methods.ChatSendMethod;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This file was created for beam
 * <p>
 * beam is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 9/11/2016
 */
class Points {

    public static void addPoints() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            //Add points here //TODO
            Beam.chatConnectible.send(ChatSendMethod.of("Giving Points"));
        }, 5, 5, TimeUnit.MINUTES);
    }
}
