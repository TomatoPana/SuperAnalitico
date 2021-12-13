package com.example.superanalitico.usersection;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.superanalitico.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowUserDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowUserDashboardFragment extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";

    public ShowUserDashboardFragment() {
        // Required empty public constructor
    }

    public static ShowUserDashboardFragment newInstance(@DrawableRes int iconId) {
        ShowUserDashboardFragment frg = new ShowUserDashboardFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_user_dashboard, container, false);
        Button button = view.findViewById(R.id.button4);
        button.setOnClickListener(buttonView -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker
                    .Builder
                    .datePicker()
                    .setTitleText("Selecciona una fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> Toast.makeText(getContext(), selection.toString(), Toast.LENGTH_LONG).show());

            datePicker.show(getParentFragmentManager(), "Date picker");
        });
        return view;
    }
}