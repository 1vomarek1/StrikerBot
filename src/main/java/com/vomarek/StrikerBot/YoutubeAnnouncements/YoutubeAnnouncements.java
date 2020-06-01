package com.vomarek.StrikerBot.YoutubeAnnouncements;

import com.vomarek.StrikerBot.StrikerBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class YoutubeAnnouncements {
    private YoutubeRequests requests;
    private Long latestVideo;
    private JDA jda;
    private Timer timer;

    public YoutubeAnnouncements (JDA jda, Map<String, Object> configValues) {
        requests = new YoutubeRequests((String) configValues.get("YoutubeAPIKey"), (String) configValues.get("YoutubeChannelID"));
        this.jda = jda;
        latestVideo = 0L;

        loadLatestVideo();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runRequests();
            }
        };

        timer = new Timer();
        timer.schedule(task, 5000, 600000);
    }

    private void runRequests() {
        JSONObject obj = null;

        try {
            obj = requests.getVideos();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        try {
            JSONArray arr = obj.getJSONArray("items");

            for (int i = 0; i < arr.length(); i++) {


                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Long videoUploadedAt = null;
                try {
                    videoUploadedAt = format.parse(arr.getJSONObject(i).getJSONObject("snippet").getString("publishedAt")).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert videoUploadedAt != null;
                if (latestVideo < videoUploadedAt) {
                    setLatestVideo(videoUploadedAt);

                    String id = arr.getJSONObject(i).getJSONObject("id").getString("videoId");

                    System.out.print("New video has come out! ("+arr.getJSONObject(i).getJSONObject("snippet").getString("title")+")\n");

                    TextChannel channel = jda.getTextChannelById("589985771793023002");

                    channel.sendMessage("https://youtu.be/"+id).queue();
                    channel.sendMessage("[||<@&615199809883865101>||]").queue();
                }
            }
        } catch (NullPointerException ignored) {
        }

    }

    private void loadLatestVideo() {
        final File file = new File("lastVideo.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
                latestVideo = 0L;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final String latestVideoString = new String(Files.readAllBytes(Paths.get(file.getPath())));

            latestVideo = Long.valueOf(latestVideoString);

        } catch (NumberFormatException e) {
            latestVideo = 0L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLatestVideo(Long latestVideo) {
        this.latestVideo = latestVideo;
        try {
            final File file = new File("lastVideo.txt");

            if (file.exists()) {
                file.delete();
            }

            FileWriter writer = new FileWriter("lastVideo.txt", true);
            writer.write(latestVideo+"");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
