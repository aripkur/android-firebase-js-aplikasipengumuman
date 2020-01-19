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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    Button masuk;
    LinearLayout progress;
    DatabaseReference reference;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Masuk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = findViewById(R.id.progress);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        masuk = findViewById(R.id.btnMasuk);

        auth = FirebaseAuth.getInstance();

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();

                if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)){
                    Toast.makeText(LoginActivity.this, "Lengkapi Data Diatas", Toast.LENGTH_SHORT).show();
                }else {
                    progress.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(strEmail, strPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        firebaseUser = auth.getCurrentUser();
                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("usergroup");
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String usergroup = dataSnapshot.getValue(String.class);
                                                if (usergroup.equals("kosong")) {
                                                    Intent intent = new Intent(LoginActivity.this, VerifikasiRegisterActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }else if(usergroup.equals("siswa") || usergroup.equals("guru")){
                                                    FirebaseMessaging.getInstance().subscribeToTopic(usergroup);
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }else {
                                        Toast.makeText(LoginActivity.this, "Gagal masuk, isi data dengan benar...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
