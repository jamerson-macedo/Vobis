package com.jmdevelopers.workproject.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmdevelopers.workproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MinhasDoacoesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MinhasDoacoesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MinhasDoacoesFragment extends Fragment {


    public MinhasDoacoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_minhas_doacoes, container, false);
    }
}
