package com.gmail.arifkur10027.smpital_fallah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText email, password, nama;
    Button btnDaftar;
    LinearLayout progres;

    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.txtNama);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        btnDaftar = findViewById(R.id.btnDaftar);
        progres = findViewById(R.id.progress);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pendaftaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();
                String strNama = nama.getText().toString();

                if (TextUtils.isEmpty(strNama) || TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)){
                    Toast.makeText(RegisterActivity.this, "Lengkapi Data Pendaftaran Diatas", Toast.LENGTH_SHORT).show();
                }else if (strPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password harus lebih dari 6 karakter", Toast.LENGTH_SHORT).show();
                }else {
                    progres.setVisibility(View.VISIBLE);
                    daftar(strEmail, strPassword, strNama);
                }
            }
        });
    }

    private void daftar(String email, String password,final String nama){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();
                            String email = firebaseUser.getEmail();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("email", email);
                            hashMap.put("nama", nama);
                            hashMap.put("usergroup", "kosong");
                            hashMap.put("tanggalDaftar", ServerValue.TIMESTAMP);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(RegisterActivity.this, VerifikasiRegisterActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else {
                            progres.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Pendaftaran Gagal, pastikan data pendaftaran sudah benar... ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
