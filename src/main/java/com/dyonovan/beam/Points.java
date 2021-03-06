package com.dyonovan.beam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pro.beam.api.resource.chat.OnlineChatUser;
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

    private final static int POINTS_LOOP = 25;

    private static Map<String, Integer> points;
    static ScheduledExecutorService scheduler;

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
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> givePointsToAll(POINTS_LOOP), 0, 5, TimeUnit.MINUTES);
    }

    static int getPoints(String username) {
        if (points.containsKey(username)) return points.get(username);
        return 0;
    }

    static void givePointsToAll(int amount) {
        try {
            OnlineUsersResponse onlineList = Chat.beam.use(ChatService.class).users(Beam.mainGuiController.beamChannel, 0, 50).get();
            int newPoints = 0;
            for (OnlineChatUser user : onlineList) {
                newPoints = amount;
                if (points.containsKey(user.userName)) {
                    newPoints += points.get(user.userName);
                }
                points.put(user.userName, newPoints);
            }
            savePoints();
            Beam.mainGuiController.updateLog(amount + " points added to everyone in chat...");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
