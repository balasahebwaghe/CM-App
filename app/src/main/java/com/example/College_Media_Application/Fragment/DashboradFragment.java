package com.example.College_Media_Application.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.College_Media_Application.R;


public class DashboradFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Home ");
        return inflater.inflate(R.layout.fragment_dashborad, container, false);
    }
}