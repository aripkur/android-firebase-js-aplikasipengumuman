package com.gmail.arifkur10027.smpital_fallah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {
    DatabaseReference reference;
    EditText txt_passwordApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        txt_passwordApp = findViewById(R.id.txtPasswordApp);
        reference = FirebaseDatabase.getInstance().getReference("SettingApp").child("passwordAplikasi");

        findViewById(R.id.btnMasukApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartActivity.this, "Tunggu ...", Toast.LENGTH_SHORT).show();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pass = dataSnapshot.getValue(String.class);
                        if (txt_passwordApp.getText().toString().equals(pass)){
                            Toast.makeText(StartActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StartActivity.this, AuthActivity.class));
                        }else {
                            Toast.makeText(StartActivity.this,"Password salah, silahkan hubungi Admin", Toast.LENGTH_SHORT).show();
                            txt_passwordApp.setText("");
                            txt_passwordApp.setFocusable(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(StartActivity.this, "Firebase error", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
