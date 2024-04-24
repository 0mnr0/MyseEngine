package com.svl.myseengine;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

        // Установите прозрачный фон для диалогового окна
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Задайте размеры для фрагмента
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels / 6;
        getDialog().getWindow().setLayout(width, height);

        return view;
    }
}
