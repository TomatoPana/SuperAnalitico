package com.example.superanalitico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class UserAnalyticActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    FirebaseAuth mAuth;
    SharedPreferences savedUserData;
    PieChart pieChart;

    private void setData() {
        pieChart.addPieSlice(
                new PieModel(
                        "Del 90% al 100%",
                        Integer.parseInt("5"),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Del 70% al 89.9%",
                        Integer.parseInt("10"),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Del 50% al 69.9%",
                        Integer.parseInt("8"),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Menos del 50%",
                        Integer.parseInt("2"),
                        Color.parseColor("#29B6F6")));

        // To animate the pie chart
        pieChart.startAnimation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_analitic);

        mAuth = FirebaseAuth.getInstance();
        savedUserData = getSharedPreferences("savedUserData", MODE_PRIVATE);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(item -> {
            if ("Cerrar Sesion".equals(item.getTitle().toString())) {
                mAuth.signOut();
                SharedPreferences.Editor writer = savedUserData.edit();
                writer.clear();
                writer.apply();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                startActivity(logoutIntent);
                return true;
            }
            return false;
        });

        pieChart = findViewById(R.id.piechart);

        setData();

    }
}