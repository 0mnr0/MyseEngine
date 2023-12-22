package com.svl.myseengine;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

public class AchivmentFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Задайте макет для фрагмента

        View view = inflater.inflate(R.layout.achivment_unlocked, container, false);
        // Задайте размеры для фрагмента

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels / 6;
        getDialog().getWindow().setLayout(width, height);
        return view;
    }
}
