package com.example.superanalitico.usersection;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superanalitico.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowUserDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowUserDataFragment extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";

    public ShowUserDataFragment() {
        // Required empty public constructor
    }

    public static ShowUserDataFragment newInstance(@DrawableRes int iconId) {
        ShowUserDataFragment frg = new ShowUserDataFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_user_data, container, false);
    }
}