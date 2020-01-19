package com.gmail.arifkur10027.smpital_fallah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class AuthActivity extends AppCompatActivity {

    Button daftar, masuk;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Boolean ada = false;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(firebaseUser.getUid())) {
                            ada = true;
                            break;
                        }
                    }
                    if (ada) {
                        final String usergroup = dataSnapshot.child(firebaseUser.getUid()).child("usergroup").getValue(String.class);
                        if (usergroup.equals("kosong")) {
                            Intent intent = new Intent(AuthActivity.this, VerifikasiRegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else if(usergroup.equals("siswa") || usergroup.equals("guru")) {
                            FirebaseMessaging.getInstance().subscribeToTopic(usergroup);
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else if (ada == false){
//                        FirebaseAuth.getInstance().signOut();

                        firebaseUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AuthActivity.this, "Akun sudah dihapus oleh Admin", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else if(firebaseUser == null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("siswa");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("guru");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        masuk = findViewById(R.id.btnMasuk);
        daftar = findViewById(R.id.btnDaftar);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthActivity.this, LoginActivity.class));
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
            }
        });
    }
}
