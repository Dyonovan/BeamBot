package com.dyonovan.beam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pro.beam.api.resource.chat.OnlineChatUser;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.response.chat.OnlineUsersResponse;
import pro.beam.api.services.impl.ChatService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

    private static Map<String, Integer> points;

    static void loadPoints() throws FileNotFoundException {
        File file = new File("points.json");
        if (file.exists() && !file.isDirectory()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            TypeToken type = new TypeToken<HashMap<String, Integer>>(){};
            points = gson.fromJson(reader, type.getType());
        } else points = new HashMap<>();
    }

    static void savePoints() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(points);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("points.json"));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void addPoints() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            //Add points here //TODO
            Beam.chatConnectible.send(ChatSendMethod.of("Giving Points"));

            try {
                OnlineUsersResponse onlineList = Beam.beam.use(ChatService.class).users(Beam.user.channel, 0, 50).get();
                for (OnlineChatUser user : onlineList) {

                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }, 0, 5, TimeUnit.MINUTES); //Change 0 to 5
    }
}
