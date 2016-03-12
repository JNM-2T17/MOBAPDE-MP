package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectAlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SelectAlbumFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private RecyclerView albumView;
    private AlbumAdapter aa;
    private OnFragmentInteractionListener mListener;

    public SelectAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectAlbumFragment newInstance() {
        SelectAlbumFragment fragment = new SelectAlbumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicProvider mp = new MusicProvider(getActivity().getContentResolver());
        ArrayList<Album> albums = mp.getAllAlbums();
        Resources res = getResources();
        aa = new AlbumAdapter(res,albums);
        aa.setListener(new AlbumAdapter.OnClickListener() {
            @Override
            public void onAlbumClick(String album) {
                onButtonPressed(album);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_album, container, false);
        albumView = (RecyclerView) v.findViewById(R.id.albumView);
        albumView.setAdapter(aa);
        albumView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String albumName) {
        if (mListener != null) {
            mListener.onFragmentInteraction(BuildPlaylistActivity.ALBUM, albumName);
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
        void onFragmentInteraction(int fragmentType, String value);
    }

}
