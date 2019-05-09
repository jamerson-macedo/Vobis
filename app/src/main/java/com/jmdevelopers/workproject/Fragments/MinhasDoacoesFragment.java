package com.jmdevelopers.workproject.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmdevelopers.workproject.R;


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
