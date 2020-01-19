package com.gmail.arifkur10027.smpital_fallah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gmail.arifkur10027.smpital_fallah.Model.Pengumuman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPengumumanActivity extends AppCompatActivity {
    DatabaseReference reference;
    Intent intent;

    TextView txtJudul, txtIsi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumuman);
        txtJudul = findViewById(R.id.txtJudul);
        txtIsi = findViewById(R.id.txtIsi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Pengumuman");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        final String pengumumanid = intent.getStringExtra("pengumumanid");
        reference = FirebaseDatabase.getInstance().getReference("Pengumuman").child(pengumumanid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pengumuman pengumuman = dataSnapshot.getValue(Pengumuman.class);
                txtJudul.setText(pengumuman.getJudul());
                txtIsi.setText(pengumuman.getIsi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
