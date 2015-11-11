package com.lefrantguillaume.animelist.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.adapters.RecyclerAdapter;
import com.lefrantguillaume.animelist.models.ShowItem;

import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;

    protected LinkedHashMap<String, ShowItem> mDataset;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title
     * @return A new instance of fragment ShowFragment.
     */

    public static ShowFragment newInstance(String title) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        args.putString("com.lefrantguillaume.TITLE", title);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mDataset = new LinkedHashMap<>();
        mLayoutManager = new LinearLayoutManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shows, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new RecyclerAdapter(this.getContext(), this.mDataset);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addToDataset(String _id, ShowItem item) {
        this.mDataset.put(_id, item);
    }

    public void updateDataset(String _id) {

    }

    public void removeFromDataset(String _id) {
        if (this.mDataset.containsKey(_id))
            this.mDataset.remove(_id);
    }

    public void clear() {
        if (this.mDataset != null)
            this.mDataset.clear();
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

    }

}
