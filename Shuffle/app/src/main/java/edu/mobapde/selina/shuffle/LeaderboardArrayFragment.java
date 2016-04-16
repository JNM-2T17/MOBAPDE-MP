package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by XPS 13 on 4/13/2016.
 */
public class LeaderboardArrayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GAME_MODE = "gameMode";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int gameMode;
    private LeaderboardArrayAdapter la;
    private RecyclerView leaderboardView;

    private OnFragmentInteractionListener mListener;

    public LeaderboardArrayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gameMode Parameter 1.
     * @return A new instance of fragment LeaderboardArrayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardArrayFragment newInstance(int gameMode) {
        LeaderboardArrayFragment fragment = new LeaderboardArrayFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_MODE, gameMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameMode = getArguments().getInt(GAME_MODE);
        }
        Resources res = getResources();
        la = new LeaderboardArrayAdapter(res,gameMode);
        (new ScoreRetriever()).execute(gameMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        leaderboardView = (RecyclerView) v.findViewById(R.id.leaderboardView);
        leaderboardView.setAdapter(la);
        leaderboardView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        return v;
    }

    class ScoreRetriever extends AsyncTask<Integer,Void,Score[]> {

        @Override
        protected Score[] doInBackground(Integer... params) {
            OkHttpClient ohc = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .build();
            RequestBody rb = new FormBody.Builder()
                                .add("gameMode", params[0].toString())
                                .build();

            Request request = new Request.Builder()
                                .url("http://" + MainActivity.ip + ":8080/ShuffleServer/Scores")
                                .post(rb)
                                .build();
            try {
                Response response = ohc.newCall(request).execute();
                String str = response.body().string();
                return (new Gson()).fromJson(str,Score[].class);
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            return new Score[0];
        }

        @Override
        protected void onPostExecute(Score[] scores) {
            super.onPostExecute(scores);
            la.setArray(scores);
        }
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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

}
