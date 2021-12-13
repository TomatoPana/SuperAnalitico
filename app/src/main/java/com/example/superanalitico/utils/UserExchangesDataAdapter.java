package com.example.superanalitico.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.example.superanalitico.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UserExchangesDataAdapter extends RecyclerView.Adapter<UserExchangesDataAdapter.ViewHolder> {

    Map<String,Map<String, Object>> allData;
    Context context;
    FragmentManager fragmentManager;

    public UserExchangesDataAdapter(Map<String,Map<String, Object>> allData, Context context, FragmentManager fragmentManager) {
        this.allData = allData;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public TextView textViewInfo;
        public Button editButton;
        public Button deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewInfo = itemView.findViewById(R.id.textViewInfo);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_data_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set<String> keys = allData.keySet();
        String key = (String) keys.toArray()[position];

        Long amount = (Long) allData.get(key).get("amount");

        Timestamp updated_at = (Timestamp) allData.get(key).get("updated_at");
        String category = (String) allData.get(key).get("category");
        String subcategory = (String) allData.get(key).get("subcategory");

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        assert updated_at != null;
        String date = simpleDateFormat.format(updated_at.toDate());

        holder.textViewDate.setText("Gasto de $" + amount/100 + " el " + date);
        holder.textViewInfo.setText("Categoria: " + category + ">" + subcategory);

        holder.editButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = this.fragmentManager;
            EditUserDataDialogFragment newFragment = new EditUserDataDialogFragment();

                // The device is smaller, so show the fragment fullscreen
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // For a little polish, specify a transition animation
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // To make it fullscreen, use the 'content' root view as the container
                // for the fragment, which is always the root view for the activity
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
        });

        holder.deleteButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this.context);
            builder.setTitle("¿Estas seguro?")
                    .setMessage("Esta accion es irreversible")
                    .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
                                                    if(innerDocument.getId().equals(key)) {
                                                        document.getReference().collection("data").document(key).delete();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

}
