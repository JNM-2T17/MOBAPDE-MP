package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ALBUM = "param1";
    private static final String SONGS = "param2";

    // TODO: Rename and change types of parameters
    private String albumName;

    private RecyclerView songView;
    private Button backButton;

    private boolean hideBack;

    private List<Song> songs;

    private SongAdapter sa;

    private OnFragmentInteractionListener mListener;

    private boolean firstCreate;

    private ArrayList<Long> selectedSongs;

    public SongFragment() {
        // Required empty public constructor
        songs = new ArrayList<Song>();
        hideBack = false;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param album Parameter 1.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongFragment newInstance(String album,long[] songs) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString(ALBUM, album);
        args.putLongArray(SONGS,songs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long[] selected = null;
        if (getArguments() != null) {
            albumName = getArguments().getString(ALBUM);
            selected = getArguments().getLongArray(SONGS);
        }
        //make these attributes
        MusicProvider mp = new MusicProvider(getActivity().getContentResolver());

        if( albumName == null ) {
            songs = mp.getAllSongs();
        } else {
            //get songs in album
            songs = mp.getSongsIn(albumName);
        }

        sa = new SongAdapter(songs);
        sa.setListener(new SongAdapter.OnClickListener() {

            @Override
            public void onSongClick(long id, boolean checked) {
                onButtonPressed(id, checked);
            }
        });
        if( selectedSongs == null ) {
            ArrayList<Long> sel = new ArrayList<Long>();
            if (selected != null) {
                for (long l : selected) {
                    sel.add(l);
                }
            }
            setSongs(sel);
        } else {
            setSongs(selectedSongs);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_song, container, false);
        songView = (RecyclerView)v.findViewById(R.id.songView);
        songView.setAdapter(sa); //change this later or else
        songView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        backButton = (Button)v.findViewById(R.id.backButton);
        if( hideBack ) {
            backButton.setVisibility(View.GONE);
        } else {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.up();
                    }
                }
            });
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(long songId,boolean checked) {
        if (mListener != null) {
            mListener.onFragmentInteraction(songId, checked);
        }
    }

    public void hideBack() {
        hideBack = true;
    }

    public void setSongs(ArrayList<Long> songs) {
        sa.setSelected(songs);
        selectedSongs = songs;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("SongFragment","ATTACHING");
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
        void onFragmentInteraction(long songId, boolean checked);
        void up();
    }
}
