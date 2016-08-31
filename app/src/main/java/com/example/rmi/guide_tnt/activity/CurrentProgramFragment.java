package com.example.rmi.guide_tnt.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;
import com.example.rmi.guide_tnt.service.Webservice;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class CurrentProgramFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final static int ICON_SIZE = 55;
    private BottomSheetBehavior<NestedScrollView> mBottomSheetBehavior;
    private NestedScrollView bottomSheet;

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
        View view = inflater.inflate(R.layout.fragment_current_program, container, false);

        bottomSheet = (NestedScrollView) view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        return view;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class OnClickListener implements View.OnClickListener {
        Program program;

        public OnClickListener(Program program) {
            this.program = program;
        }

        @Override
        public void onClick(View view) {

            ProgramDetailFragment programDetailFragment = ProgramDetailFragment.newInstance(program);

//            bottomSheet.removeAllViews();
//            bottomSheet.addView(programDetailFragment);

//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
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
                programs = Webservice.getProgramsAtTime(requestedDate);
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

                // Retrieve the container for programs, and also for channels
                LinearLayout programsContainer = (LinearLayout) activity.findViewById(R.id.programsContainer);
                programsContainer.removeAllViews();

                LinearLayout channelsContainer = (LinearLayout) activity.findViewById(R.id.channelsContainer);
                channelsContainer.removeAllViews();

                // create first line with times
                // icon = clock, then 1 cell per hour
                createTimesHeader(programsContainer, channelsContainer);
                LinearLayout.LayoutParams layoutParams;
                LinearLayout channelProgramsLayout;

                int i = 1;
                for (Channel channel : channels) {


                    ImageView channelIcon = new ImageView(activity);
                    Picasso.with(activity)
                            .load("http://www.guide-tnt.fr/images/channels/logo" + i++ + ".gif")
                            .resize(ICON_SIZE, ICON_SIZE)
                            .centerInside()
                            .into(channelIcon);
                    channelIcon.setLayoutParams(new LinearLayout.LayoutParams(ICON_SIZE, ICON_SIZE));
                    channelsContainer.addView(channelIcon);

                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    channelProgramsLayout = new LinearLayout(activity);
                    channelProgramsLayout.setOrientation(LinearLayout.HORIZONTAL);
                    channelProgramsLayout.setLayoutParams(layoutParams);

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
                            programTextView.setBackgroundResource(R.drawable.background_program_in_progress);
                        } else {
                            programTextView.setBackgroundResource(R.drawable.background_program_default);
                        }

                        programTextView.setOnClickListener(new OnClickListener(program));
                        channelProgramsLayout.addView(programTextView);
                    }
                    programsContainer.addView(channelProgramsLayout);
                }

            }


        }

        private void createTimesHeader(LinearLayout programsContainer, LinearLayout channelsContainer) {
            ImageView clockIcon = new ImageView(activity);
            Picasso.with(activity)
                    .load("https://cdn0.iconfinder.com/data/icons/feather/96/clock-128.png") // TODO use a local resource?
                    .resize(ICON_SIZE, ICON_SIZE)
                    .centerInside()
                    .into(clockIcon);

            clockIcon.setLayoutParams(new LinearLayout.LayoutParams(ICON_SIZE, ICON_SIZE));
            channelsContainer.addView(clockIcon);

            LinearLayout layout = new LinearLayout(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(layoutParams);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            // get the requested date with hour precision, then get the hour before (as we display programs only 1hour before requested date)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(requestedDate);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 1);

            // display layout of 1hour length (except for the first one)
            for (int i = 0; i < 8; i++) {
                Date tmpStartDate = calendar.getTime();
                calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);
                Date tmpEndDate = calendar.getTime();
                TextView timeTextView = new TextView(activity);
                int width = convertDurationToWidth(tmpStartDate, tmpEndDate);
                timeTextView.setLayoutParams(new LinearLayout.LayoutParams(width, 55));

                // only display the time if there is enough place
                if (width > HOUR_WIDTH / 2) {
                    timeTextView.setText(dateFormat.format(tmpStartDate));
                    timeTextView.setPadding(10, 0, 0, 0);
                }
                timeTextView.setLines(1);
                timeTextView.setTextSize(12);
                timeTextView.setBackgroundResource(R.drawable.background_program_header);


                layout.addView(timeTextView);
            }
            programsContainer.addView(layout);
        }


        private int programWidthComputation(Program program) {
            return convertDurationToWidth(program.getStartDate(), program.getEndDate());
        }

        private int convertDurationToWidth(Date startDate, Date endDate) {
            long currentTimeStamp = requestedDate.getTime();
            long minimalVisibleTimeStamp = currentTimeStamp - 60 * 60 * 1000; // current time minus 1h
            long programDuration = endDate.getTime() - Math.max(startDate.getTime(), minimalVisibleTimeStamp); // duration in ms
            return (int) Math.round(programDuration * HOUR_WIDTH / (60.0 * 60.0 * 1000.0));
        }

        private boolean programInProgress(Program program) {
            return program.getStartDate().before(requestedDate) && program.getEndDate().after(requestedDate);
        }

        // size of a 1h program
        private static final int HOUR_WIDTH = 420; // /!\  per hour in hour, not per hour in ms /!\

    }
}
