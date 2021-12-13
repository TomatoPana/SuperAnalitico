package com.example.superanalitico.usersection;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superanalitico.R;
import com.example.superanalitico.utils.UserDashboardDataAdapter;
import com.example.superanalitico.utils.UserExchangesDataAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        TextView textView = view.findViewById(R.id.textView6);
        List<Map<String, Object>> allData = new ArrayList<>();

        RecyclerView dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView);
        dashboardRecyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(layoutManager);

        UserDashboardDataAdapter adapter = new UserDashboardDataAdapter(allData, getContext(), requireActivity().getSupportFragmentManager());
        dashboardRecyclerView.setAdapter(adapter);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        button.setText(date);
        button.setOnClickListener(buttonView -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker
                    .Builder
                    .datePicker()
                    .setTitleText("Selecciona una fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String selectedDate = simpleDateFormat.format(new Date(selection + 21600000));
                button.setText(selectedDate);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
                DocumentReference database = mFirebase.collection("users").document(uid);

                CollectionReference data = database.collection("exchanges");

                data.document(selectedDate).get().addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       Objects.requireNonNull(task.getResult()).getReference().collection("data").get().addOnCompleteListener(innerTask -> {
                          if(innerTask.isSuccessful()) {
                              if(Objects.requireNonNull(innerTask.getResult()).getDocuments().size() == 0) {
                                  textView.setVisibility(View.VISIBLE);
                                  allData.clear();
                                  adapter.notifyDataSetChanged();
                              } else {
                                  textView.setVisibility(View.GONE);
                                  allData.clear();
                                  adapter.notifyDataSetChanged();
                                  for (DocumentSnapshot document :
                                          Objects.requireNonNull(innerTask.getResult()).getDocuments()) {
                                      allData.add(document.getData());
                                      adapter.notifyDataSetChanged();
                                  }
                              }
                          }
                       });
                   }
                });

                Log.d("TAG", selection.toString());
            });

            datePicker.show(getParentFragmentManager(), "Date picker");
        });
        return view;
    }
}