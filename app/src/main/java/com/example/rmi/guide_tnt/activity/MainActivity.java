package com.example.rmi.guide_tnt.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;
import com.example.rmi.guide_tnt.service.Webservice;

import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RetrieveFeedTask(this).execute();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        List<Channel> channels = null;
        Map<Channel, List<Program>> programs = null;
        private Activity activity;

        public RetrieveFeedTask(Activity activity) {
            super();
            this.activity = activity;
        }

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
                RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                rv.setLayoutManager(linearLayoutManager);
                rv.setHasFixedSize(true);

                if (programs != null) {
                    TonightAdapter tonightAdapter = new TonightAdapter(programs.get(new Channel(1, "toto", 1)));
                    rv.setAdapter(tonightAdapter);
                }
            }
        }


    }
}
