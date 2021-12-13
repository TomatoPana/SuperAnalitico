package com.example.superanalitico.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superanalitico.R;
import com.example.superanalitico.orm.Users;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private final List<Users> dataSet;
    private final Context baseContext;

    public DataAdapter(List<Users> dataSet, Context baseContext) {
        this.dataSet = dataSet;
        this.baseContext = baseContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUserName;
        public TextView textViewUserEmail;
        public Spinner spinnerUserType;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
            spinnerUserType = itemView.findViewById(R.id.spinnerUserType);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_admin_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewUserEmail.setText(dataSet.get(position).getEmail());
        holder.textViewUserName.setText(dataSet.get(position).getLastNames() + " " + dataSet.get(position).getFirstNames());
        String[] userTypes = {"User", "Analytic", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(baseContext, R.layout.support_simple_spinner_dropdown_item, userTypes);
        holder.spinnerUserType.setAdapter(adapter);
        holder.spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(baseContext, userTypes[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
