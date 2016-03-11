package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectPlaylistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SelectPlaylistFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private RecyclerView playlistView;
    private PlaylistAdapter pa;

    private OnFragmentInteractionListener mListener;

    public SelectPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectPlaylistFragment newInstance() {
        SelectPlaylistFragment fragment = new SelectPlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBManager dbm = new DBManager(getActivity().getBaseContext());
        Cursor c = dbm.getPlaylists();
        pa = new PlaylistAdapter(getActivity().getBaseContext(),c);
        pa.setListener(new CreatePlaylistActivity.OnClickListener() {
            @Override
            public void onClick(Playlist p) {
                onButtonPressed(p);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_album, container, false);
        playlistView = (RecyclerView) v.findViewById(R.id.albumView);
        playlistView.setAdapter(pa);
        playlistView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Playlist p) {
        if (mListener != null) {
            mListener.onFragmentInteraction(p);
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
        void onFragmentInteraction(Playlist p);
    }

}
