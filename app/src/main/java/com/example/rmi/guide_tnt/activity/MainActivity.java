package com.example.rmi.guide_tnt.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;
import com.example.rmi.guide_tnt.service.Webservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new RetrieveFeedTask().execute();

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        List<Channel> channels = null;
        Map<Channel, List<Program>> programs = null;


        @Override
        protected String doInBackground(Void... params) {
            try {
                channels = Webservice.getChannels();
                programs = Webservice.getProgramsTonight();
                return "OK";
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "ERROR";
            } else {
                StringBuilder responseBuilder = new StringBuilder();
                DateFormat dateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE);
                dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

//                for (Channel channel : channels) {
//                    responseBuilder.append(channel.getName());
//                    responseBuilder.append("\n");
//                }


                for (Channel channel : programs.keySet()) {
                    responseBuilder.append(channel.getName() + "\n");
                    for (Program program : programs.get(channel)) {
                        responseBuilder.append(dateFormat.format(program.getStartDate()) + " " + program.getTitle());
                        responseBuilder.append("\n");
                    }
                    responseBuilder.append("\n");
                }


                ((TextView) findViewById(R.id.content)).setText(responseBuilder.toString());
            }

        }
    }
}
