package com.example.superanalitico.usersection;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superanalitico.R;
import com.example.superanalitico.orm.Exchange;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateUserDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserDataFragment extends Fragment {

    private static final String CHANNEL_ID = "1";
    ConstraintLayout mainConstraintLayout;
    ConstraintLayout secondConstraintLayout;
    Spinner spinnerCategory;
    EditText editTextAmount;
    Spinner spinnerSubcategory;
    EditText editTextDescription;
    Button saveButton;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    private static final String ARG_ICON = "ARG_ICON";

    public CreateUserDataFragment() {
        // Required empty public constructor
    }

    public static CreateUserDataFragment newInstance(@DrawableRes int iconId) {
        CreateUserDataFragment frg = new CreateUserDataFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    protected void verifyIfExistsBudget() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");
        data.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot allData = task.getResult();
                assert allData != null;

                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());
                boolean result = false;
                for(DocumentSnapshot document: allData.getDocuments()){
                    if(document.getId().equals(date)){
                        result = true;
                        break;
                    }
                }
                if(!result) {
                    spinnerCategory.setVisibility(View.GONE);
                    editTextAmount.setVisibility(View.GONE);
                    spinnerSubcategory.setVisibility(View.GONE);
                    editTextDescription.setVisibility(View.GONE);
                    saveButton.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    textView2.setVisibility(View.GONE);
                    textView3.setVisibility(View.GONE);
                    textView4.setVisibility(View.GONE);
                    secondConstraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_user_data, container, false);

        mainConstraintLayout = view.findViewById(R.id.mainContraintLayout);
        secondConstraintLayout = view.findViewById(R.id.secondConstaintLayout);

        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        spinnerSubcategory = view.findViewById(R.id.spinnerSubcategory);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        saveButton = view.findViewById(R.id.saveButton);
        textView1 = view.findViewById(R.id.textView);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView4);
        textView4 = view.findViewById(R.id.textView5);
        verifyIfExistsBudget();

        EditText editTextBudgetAmount = view.findViewById(R.id.editTextBudgetAmount);
        Button saveBudgetButton = view.findViewById(R.id.saveBudgetButton);

        saveBudgetButton.setOnClickListener(buttonView -> {
            if(editTextBudgetAmount.length() == 0) {
                editTextBudgetAmount.setError("Ingresa una cantidad");
            } else {
                try {
                    double amount = Double.parseDouble(editTextBudgetAmount.getText().toString());
                    amount = Math.round(amount * 100.0) / 100.0;
                    int exactAmount = (int) amount * 100;
                    HashMap<String, Integer> newData = new HashMap<>();
                    newData.put("budget", exactAmount);
                    newData.put("remaining", exactAmount);

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
                    DocumentReference database = mFirebase.collection("users").document(uid);

                    CollectionReference data = database.collection("exchanges");

                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String date = simpleDateFormat.format(new Date());

                    data.document(date).set(newData).addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           spinnerCategory.setVisibility(View.VISIBLE);
                           editTextAmount.setVisibility(View.VISIBLE);
                           spinnerSubcategory.setVisibility(View.VISIBLE);
                           editTextDescription.setVisibility(View.VISIBLE);
                           saveButton.setVisibility(View.VISIBLE);
                           textView1.setVisibility(View.VISIBLE);
                           textView2.setVisibility(View.VISIBLE);
                           textView3.setVisibility(View.VISIBLE);
                           textView4.setVisibility(View.VISIBLE);
                           secondConstraintLayout.setVisibility(View.GONE);
                       }
                    });

                } catch (Exception e) {
                    editTextBudgetAmount.setError("Ingresa una cantidad valida");
                }
            }

        });


        String[] categoryData = {"Necesario","Entretenimiento","Extras"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, categoryData);
        spinnerCategory.setAdapter(arrayAdapter);
        spinnerSubcategory.setEnabled(false);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        String[] necessarySubcategoryData = {
                                "Alimentos",
                                "Transporte",
                                "Pago de Luz",
                                "Pago de Agua",
                                "Pago de telefonia",
                                "Pago de internet",
                                "Mantenimiento",
                                "Salud",
                                "Higiene",
                                "Gasolina"
                        };
                        ArrayAdapter necessarySubcategoryAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, necessarySubcategoryData);
                        spinnerSubcategory.setAdapter(necessarySubcategoryAdapter);
                        spinnerSubcategory.setEnabled(true);
                        break;
                    case 1:
                        String[] entertainmentSubcategoryData = {
                                "Salida al cine",
                                "Salida a clubes",
                                "Peliculas en renta",
                                "Juegos",
                                "Diversion",
                                "Snacks",
                        };
                        ArrayAdapter entertainmentSubcategoryAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, entertainmentSubcategoryData);
                        spinnerSubcategory.setAdapter(entertainmentSubcategoryAdapter);
                        spinnerSubcategory.setEnabled(true);
                        break;
                    case 2:
                        String[] extraSubcategoryData = {
                                "Hobbies",
                                "Mangas",
                                "Ropa",
                                "Otros pagos"
                        };
                        ArrayAdapter extraSubcategoryAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, extraSubcategoryData);
                        spinnerSubcategory.setAdapter(extraSubcategoryAdapter);
                        spinnerSubcategory.setEnabled(true);
                        break;
                    default:
                        spinnerSubcategory.setAdapter(null);
                        spinnerSubcategory.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        saveButton.setOnClickListener(buttonView -> {
            String category = (String) spinnerCategory.getSelectedItem();
            String subcategory = (String) spinnerSubcategory.getSelectedItem();
            String description = editTextDescription.getText().toString();
            try {
                double amount = Double.parseDouble(editTextAmount.getText().toString());
                amount = Math.round(amount * 100.0) / 100.0;
                int exactAmount = (int) amount * 100;
                Exchange exchange = new Exchange();
                exchange.amount = exactAmount;
                exchange.subcategory = subcategory;
                exchange.category = category;
                exchange.created_at = new Date();
                exchange.updated_at = new Date();
                exchange.description = description;

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
                DocumentReference database = mFirebase.collection("users").document(uid);

                CollectionReference data = database.collection("exchanges");

                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());

                data.document(date).get().addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        try {
                            int budget = Objects.requireNonNull(Objects.requireNonNull(task1.getResult()).getLong("budget")).intValue();
                            int remaining = Objects.requireNonNull(Objects.requireNonNull(task1.getResult()).getLong("remaining")).intValue();


                            data.document(date).collection("data").add(exchange).addOnCompleteListener(task -> {
                                if(task.isSuccessful()) {
                                    editTextAmount.setText("");
                                    editTextDescription.setText("");
                                    HashMap<String, Integer> postData = new HashMap<>();
                                    int newRemaining = remaining - exactAmount;
                                    postData.put("remaining", newRemaining);
                                    postData.put("budget", budget);
                                    if(newRemaining < 0) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                .setContentTitle("Presupuesto excedido!")
                                                .setContentText("Se ha excedido el presupuesto por la cantidad de " + (newRemaining * -1)/100 + " pesos.")
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText("Se ha excedido el presupuesto por la cantidad de " + (newRemaining * -1)/100 + " pesos."))
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = "Info";
                                            String descriptionNotification = "Information of the app";
                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                                            channel.setDescription(descriptionNotification);
                                            // Register the channel with the system; you can't change the importance
                                            // or other notification behaviors after this
                                            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                        }

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
                                        notificationManager.notify(new Random().nextInt(1000000), builder.build());
                                    }
                                    data.document(date).set(postData).addOnCompleteListener(task2 -> {
                                        if(task2.isSuccessful()) {
                                            Snackbar.make(view, "Guardado exitoso!", Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            Snackbar.make(view, "Algo salio mal! " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });



            } catch (Exception e) {
                editTextAmount.setError("Ingresa una cantidad valida");
            }
        });

        return view;
    }
}