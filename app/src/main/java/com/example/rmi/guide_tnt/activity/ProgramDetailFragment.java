package com.example.rmi.guide_tnt.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Program;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgramDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgramDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROGRAM = "program";

    // TODO: Rename and change types of parameters
    private Program program;

    private OnFragmentInteractionListener mListener;

    public ProgramDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param program Parameter 1.
     * @return A new instance of fragment ProgramDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramDetailFragment newInstance(Program program) {
        ProgramDetailFragment fragment = new ProgramDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROGRAM, program);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            program = (Program) getArguments().get(ARG_PROGRAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_detail, container, false);

        TextView title = (TextView) view.findViewById(R.id.programTitle);
        TextView startTime = (TextView) view.findViewById(R.id.programHour);
        TextView descriptionFull = (TextView) view.findViewById(R.id.programDescriptionFull);
        ImageView programImage = (ImageView) view.findViewById(R.id.programImage);
        RelativeLayout programHeaderLayout = (RelativeLayout) view.findViewById(R.id.programHeaderLayout);
        TextView season = (TextView) view.findViewById(R.id.season);
        TextView category = (TextView) view.findViewById(R.id.category);
        RelativeLayout reviewLayout = (RelativeLayout) view.findViewById(R.id.telerama_layout);
        TextView review = (TextView) view.findViewById(R.id.telerama_text);

        // Start date
        java.text.DateFormat timeInstance = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        timeInstance.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        startTime.setText(timeInstance.format(program.getStartDate()));

        // Title
        title.setText(program.getTitle());

        // Season (and episode info) if available
        StringBuffer seasonData = new StringBuffer();
        if (program.getSeason() != null && program.getSeason().length() > 0)
            seasonData.append("S" + program.getSeason());
        if (program.getEpisode() != null && program.getEpisode().length() > 0)
            seasonData.append("E" + program.getEpisode());

        if (seasonData.length() > 0)
            season.setText(seasonData.toString());
        else
            season.setVisibility(View.GONE);

        // display the program's category if available
        if (program.getCategory() != null && program.getCategory().length() > 0)
            category.setText(program.getCategory());

        // Description
        descriptionFull.setText(program.getDescription());

        // Review
        if (program.getReview() != null && program.getReview().length() > 0)
            review.setText(program.getReview());
        else {
            review.setText(null);
            reviewLayout.setVisibility(View.GONE);
        }


        // Image
        if (program.getImageURL() != null)
            Picasso.with(getActivity())
                    .load(program.getImageURL())
                    .resize(500, 500)
                    .centerCrop()
                    .into(programImage);

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
}
