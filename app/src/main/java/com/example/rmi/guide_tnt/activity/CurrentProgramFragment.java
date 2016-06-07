package com.example.rmi.guide_tnt.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;
import com.example.rmi.guide_tnt.service.Webservice;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentProgramFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentProgramFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentProgramFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CurrentProgramFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentProgramFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentProgramFragment newInstance(String param1, String param2) {
        CurrentProgramFragment fragment = new CurrentProgramFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RetrieveCurrentProgramsTask(getActivity()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_program, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class RetrieveCurrentProgramsTask extends AsyncTask<Void, Void, String> {

        List<Channel> channels = null;
        Map<Channel, List<Program>> programs = null;
        private Activity activity;
        private Date requestedDate;

        public RetrieveCurrentProgramsTask(Activity activity) {
            super();
            this.activity = activity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                channels = Webservice.getChannels();
                requestedDate = new Date();
                programs = Webservice.getProgramsAtTime(requestedDate); // TODO use correct WS
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
                // build view
                LinearLayout mainContainer = (LinearLayout) activity.findViewById(R.id.programContainer);
                mainContainer.removeAllViews();

                LinearLayout channelsLayout = (LinearLayout) activity.findViewById(R.id.channelLayout);
                channelsLayout.removeAllViews();

                ImageView timeIcon = new ImageView(activity);
                Picasso.with(activity)
                        .load("https://cdn0.iconfinder.com/data/icons/feather/96/clock-128.png")
                        .resize(55, 55)
                        .centerInside()
                        .into(timeIcon);

                timeIcon.setLayoutParams(new LinearLayout.LayoutParams(55, 55));
                channelsLayout.addView(timeIcon);

                LinearLayout layout = new LinearLayout(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(layoutParams);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(requestedDate);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                for (int i = 0; i < 8; i++) {
                    Date tmpStartDate = calendar.getTime();
                    calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);
                    Date tmpEndDate = calendar.getTime();
                    TextView programTextView = new TextView(activity);
                    int width = convertTimeToWidth(tmpStartDate, tmpEndDate);
                    programTextView.setLayoutParams(new LinearLayout.LayoutParams(width, 55));
                    if(width > (420/2)) {
                        programTextView.setText(dateFormat.format(tmpStartDate));
                        programTextView.setPadding(10,0,0,0);
                    }
                    programTextView.setLines(1);
                    programTextView.setTextSize(12);
                    programTextView.setBackgroundResource(R.drawable.background_border);


                    layout.addView(programTextView);
                }
                mainContainer.addView(layout);


                int i = 1;
                for (Channel channel : channels) {

                    ImageView channelIcon = new ImageView(activity);
                    Picasso.with(activity)
                            .load("http://www.guide-tnt.fr/images/channels/logo" + i++ + ".gif")
                            .resize(55, 55)
                            .centerInside()
                            .into(channelIcon);
                    channelIcon.setLayoutParams(new LinearLayout.LayoutParams(55, 55));
                    channelsLayout.addView(channelIcon);

                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout = new LinearLayout(activity);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    layout.setLayoutParams(layoutParams);

                    for (Program program : programs.get(channel)) {

                        if (program.getEndDate().getTime() < (requestedDate.getTime() - 60 * 60 * 1000))
                            continue;

                        TextView programTextView = new TextView(activity);
                        programTextView.setLayoutParams(new LinearLayout.LayoutParams(programWidthComputation(program), 55));
                        programTextView.setText(program.getTitle());
                        programTextView.setLines(1);
                        programTextView.setTextSize(12);
                        programTextView.setEllipsize(TextUtils.TruncateAt.END);
                        programTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        if (programInProgress(program)) {
                            programTextView.setBackgroundResource(R.drawable.background_border2);
                        } else {
                            programTextView.setBackgroundResource(R.drawable.background_border);
                        }
                        layout.addView(programTextView);
                    }
                    mainContainer.addView(layout);
                }

            }


        }


        private int programWidthComputation(Program program) {
            return convertTimeToWidth(program.getStartDate(), program.getEndDate());
        }

        private int convertTimeToWidth(Date startDate, Date endDate) {
            long currentTimeStamp = requestedDate.getTime();
            long minimalVisibleTimeStamp = currentTimeStamp - 60 * 60 * 1000; // current time minus 1h
            long programDuration = endDate.getTime() - Math.max(startDate.getTime(), minimalVisibleTimeStamp);
            return (int) (programDuration / (1000 * 60) * 7);
        }

        private boolean programInProgress(Program program) {
            return program.getStartDate().before(requestedDate) && program.getEndDate().after(requestedDate);
        }
    }
}
