package com.example.rmi.guide_tnt.activity;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass..
 * Use the {@link TonightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TonightFragment extends Fragment {

    public TonightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TonightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TonightFragment newInstance() {
        TonightFragment fragment = new TonightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new RetrieveFeedTask().execute();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tonight, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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


            }

        }
    }
}
