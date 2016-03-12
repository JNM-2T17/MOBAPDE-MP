package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button backButton;
    private ArtistAdapter aa;
    private RecyclerView artistView;

    public ArtistFragment() {
        // Required empty public constructor
    }

    public static ArtistFragment newInstance() {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicProvider mp = new MusicProvider(getActivity().getContentResolver());

        List<String> artistNames = mp.getAllArtists();
        ArrayList<Artist> artists = new ArrayList<Artist>();

        for(String s: artistNames){
            Artist a = new Artist(s);
            artists.add(a);
        }

        aa = new ArtistAdapter(artists);
        aa.setListener(new ArtistAdapter.OnClickListener() {
            @Override
            public void onArtistClick(String name) {
                onButtonPressed(name);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_artist, container, false);
        /*backButton = (Button) v.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.back();
            }
        });*/

        artistView = (RecyclerView) v.findViewById(R.id.artistView);

        artistView.setAdapter(aa);
        artistView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String artistName) {
        if (mListener != null) {
            mListener.onFragmentInteraction(BuildPlaylistActivity.ARTIST, artistName);
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
        void onFragmentInteraction(int source, String value);
        boolean back();
    }
}
