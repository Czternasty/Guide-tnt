package com.example.rmi.guide_tnt.service;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Rémi on 13/04/2016.
 */
public class Webservice {

    protected static final String API_URL = "http://www.guide-tnt.fr/api/";


    /**
     * Provide the list of all available channles
     *
     * @return
     */
    public static List<Channel> getChannels() {

        // retrieve data from webservice calls
        String requestResult = executeRequest("channels");

        if (requestResult == null) {
            return null;
        }

        ArrayList<Channel> channels = new ArrayList<>();

        try {
            JSONArray channelsArray = (JSONArray) new JSONTokener(requestResult).nextValue();
            for (int i = 0; i < channelsArray.length(); i++) {
                JSONObject channelJSON = (JSONObject) channelsArray.get(i);

                // Convert the JSON into a Channel object
                Channel channel = new Channel(
                        channelJSON.getInt("id"),
                        channelJSON.getString("name"),
                        channelJSON.getInt("channel_key"));
                channels.add(channel);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return channels;
    }

    /**
     * Provide a list of <b>tonight</b> programs for each channel .
     *
     * @return A map where keys are channel's ids and values are programs
     */
    public static Map<Channel, List<Program>> getProgramsTonight() {
        String requestResult = executeRequest("tonight");

        if (requestResult == null) {
            return null;
        }

        SortedMap<Channel, List<Program>> programsByChannel = new TreeMap<>();

        List<Channel> channels = getChannels();

        try {
            JSONObject JSONObject = (JSONObject) new JSONTokener(requestResult).nextValue();
            for (Channel channel : channels) {

                ArrayList<Program> programs = new ArrayList<>();
                programsByChannel.put(channel, programs);

                JSONObject channelProgramsJSON = JSONObject.getJSONObject(Integer.toString(channel.getId()));
                JSONArray programsJSON = channelProgramsJSON.getJSONArray("programs");
                for (int i = 0; i < programsJSON.length(); i++) {
                    JSONObject programJSON = (org.json.JSONObject) programsJSON.get(i);

                    Drawable thumb = null;
                    String programImageUrl = programJSON.getString("icon");
//                    try {
//                        if(programImageUrl != null && programImageUrl.length() > 0) {
//                            URL thumbUrl = new URL(programImageUrl);
//                            thumb = Drawable.createFromStream(thumbUrl.openStream(), "src");
//                        }
//                    }
//                    catch (Exception e) {
//                        // handle it
//                    }

                    programs.add(new Program(
                            programJSON.getInt("id"),
                            programJSON.getString("title"),
                            programJSON.getString("description"),
                            new Date(programJSON.getLong("start") * 1000), // start and endate are in seconds, the Date constructor need ms.
                            new Date(programJSON.getLong("stop") * 1000),
                            programImageUrl,
                            thumb,
                            programJSON.isNull("review") ? null : programJSON.getString("review"),
                            programJSON.isNull("season") ? null : programJSON.getString("season"),
                            programJSON.isNull("episode") ? null :programJSON.getString("episode"),
                            programJSON.isNull("rating") ? null : programJSON.getInt("rating"),
                            programJSON.isNull("category") ? null : programJSON.getString("category")
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return programsByChannel;
    }

    private static String executeRequest(String resource) {
        try {
            URL url = new URL(API_URL + resource);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        return null;
    }

}
