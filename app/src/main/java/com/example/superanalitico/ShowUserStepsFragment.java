package com.example.superanalitico;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowUserStepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowUserStepsFragment extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";

    public ShowUserStepsFragment() {
        // Required empty public constructor
    }

    public static ShowUserStepsFragment newInstance(@DrawableRes int iconId) {
        ShowUserStepsFragment frg = new ShowUserStepsFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_user_steps, container, false);
    }
}