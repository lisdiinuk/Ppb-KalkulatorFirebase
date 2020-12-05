package com.example.kalkulatorfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String TAG = "MainActivity";
    private List<Hitung> hitungList = new ArrayList<>();
    private Recyclerview_Adapter mAdapter;


    EditText Input1, Input2;
    Button BtnHitung;
    RadioGroup Group;
    android.widget.RadioButton RadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String Psnn;
        Psnn = intent.getStringExtra("Psnn");
        if(Psnn != null){
            if(Psnn == "0") {
                Toast.makeText(MainActivity.this, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                Psnn = null;
            }else{
                Toast.makeText(MainActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                Psnn = null;
            }
        }

        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.MyRecyclerview);
        mAdapter = new Recyclerview_Adapter(this,hitungList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DataHitung();

        Input1 = findViewById(R.id.Input1);
        Input2 = findViewById(R.id.Input2);
        BtnHitung = findViewById(R.id.btn_hitung);
        Group = findViewById(R.id.radioGroup);

        BtnHitung.setOnClickListener(v -> {
            Double Result;
            String operasi;
            int RadioId = Group.getCheckedRadioButtonId();
            RadioButton = findViewById(RadioId);

            Toast.makeText(MainActivity.this, "Memproses Data", Toast.LENGTH_SHORT).show();

            if (RadioButton.getText().equals("Bagi")) {
                Result = Double.parseDouble(Input1.getText().toString()) / Double.parseDouble(Input2.getText().toString());
                operasi = "/";
            } else if (RadioButton.getText().equals("Kurang")) {
                Result = Double.parseDouble(Input1.getText().toString()) - Double.parseDouble(Input2.getText().toString());
                operasi = "-";
            } else if (RadioButton.getText().equals("Kali")) {
                Result = Double.parseDouble(Input1.getText().toString()) * Double.parseDouble(Input2.getText().toString());
                operasi = "*";
            } else {
                Result = Double.parseDouble(Input1.getText().toString()) + Double.parseDouble(Input2.getText().toString());
                operasi = "+";
            }


            TextView hasilField = findViewById(R.id.ResultField);
            hasilField.setText(" " + Result);

            Map<String, Object> hitung = new HashMap<>();
            hitung.put("Var1", Input1.getText().toString());
            hitung.put("Var2", Input2.getText().toString());
            hitung.put("Operator", operasi);
            hitung.put("Result", Result.toString());

            db.collection("data_hitung")
                    .add(hitung)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getBaseContext(),
                                "Input Data Berhasil", //documentReference.getId()
                                Toast.LENGTH_SHORT).show();
                        // Lihat Data (setelah sukses push data)
                        Input1.setText("0");
                        Input2.setText("0");
                        //Group.clearCheck();
                        DataHitung();



                    })
                    .addOnFailureListener(e -> Log.e("Error", e.getMessage()));

            DataHitung();

        });
    }

    private void DataHitung() {
        db.collection("data_hitung")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hitungList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Hitung hitung = new Hitung(document.getId(), document.getData().get("Var1").toString(), document.getData().get("Var2").toString(), document.getData().get("Operator").toString(), document.getData().get("Result").toString());
                            hitungList.add(hitung);
                            mAdapter.notifyDataSetChanged();
                        }


                    } else {
                        Log.w(TAG, "Gagal Mengambil Data", task.getException());
                    }
                });
    }

}