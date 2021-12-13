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

import com.example.superanalitico.R;
import com.example.superanalitico.utils.UserExchangesDataAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        View view = inflater.inflate(R.layout.fragment_show_user_data, container, false);

        Map<String,Map<String, Object>> allData = new HashMap<>();

        RecyclerView userExchangesData = view.findViewById(R.id.userExchangesData);
        userExchangesData.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        userExchangesData.setLayoutManager(layoutManager);



        UserExchangesDataAdapter adapter = new UserExchangesDataAdapter(allData, getContext(), requireActivity().getSupportFragmentManager());
        userExchangesData.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");

        data.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Log.d("TAG", document.getId() + " => " + document.getData());
                    document.getReference().collection("data").get().addOnCompleteListener(innerTask -> {
                        if(innerTask.isSuccessful()) {
                            for(QueryDocumentSnapshot innerDocument : Objects.requireNonNull(innerTask.getResult())) {
                                Log.d("TAG", innerDocument.getId() + " => " + innerDocument.getData());
                                allData.put(innerDocument.getId(), innerDocument.getData());
                                adapter.notifyItemChanged(allData.size());
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}