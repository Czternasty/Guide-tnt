package com.example.rmi.guide_tnt.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rmi.guide_tnt.activity.R;
import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;
import com.example.rmi.guide_tnt.service.Webservice;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TonightFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TonightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TonightFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public TonightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TonightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TonightFragment newInstance(String param1, String param2) {
        TonightFragment fragment = new TonightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RetrieveTonightProgramsTask(getActivity()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tonight, container, false);
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

    class RetrieveTonightProgramsTask extends AsyncTask<Void, Void, String> {

        List<Channel> channels = null;
        Map<Channel, List<Program>> programs = null;
        private Activity activity;

        public RetrieveTonightProgramsTask(Activity activity) {
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
                RecyclerView rv = (RecyclerView) this.activity.findViewById(R.id.rv);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                rv.setLayoutManager(linearLayoutManager);
                rv.setHasFixedSize(true);

                if (programs != null) {
                    TonightAdapter tonightAdapter = new TonightAdapter(programs);
                    rv.setAdapter(tonightAdapter);
                }
            }
        }


    }
}
