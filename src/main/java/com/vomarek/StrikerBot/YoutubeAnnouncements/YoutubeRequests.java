package com.vomarek.StrikerBot.YoutubeAnnouncements;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YoutubeRequests {
    protected String apiKey;
    protected String channelId;

    public YoutubeRequests (String apiKey, String channel) {
        this.apiKey = apiKey;
        this.channelId = channel;
    }

    public JSONObject getVideos() throws IOException{
        return new GetVideos().getVideos();
    };


    protected class GetVideos {

        public JSONObject getVideos() throws IOException {
            final URL url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&channelId="+channelId+"&maxResults=1&order=date&key="+apiKey);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }
                in.close();

                return new JSONObject(response.toString());
            }
            return null;
        }
    }
}
