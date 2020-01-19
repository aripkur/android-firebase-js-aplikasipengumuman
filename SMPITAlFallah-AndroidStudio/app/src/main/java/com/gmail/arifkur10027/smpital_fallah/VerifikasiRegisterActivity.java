package com.gmail.arifkur10027.smpital_fallah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class VerifikasiRegisterActivity extends AppCompatActivity {

    ProgressBar pb;
    TextView verif;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_register);

        verif = findViewById(R.id.txtVerifikasi);
        pb = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("usergroup");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usergroup = dataSnapshot.getValue(String.class);
                if (!usergroup.equals("kosong")){
                    if (usergroup.equals("guru")){
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("siswa");
                        FirebaseMessaging.getInstance().subscribeToTopic("guru");

                        Toast.makeText(VerifikasiRegisterActivity.this, "verifikasi berhasil", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        verif.setText("Verifikasi Sukses");

                        Intent intent = new Intent(VerifikasiRegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else if (usergroup.equals("siswa")){
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("guru");
                        FirebaseMessaging.getInstance().subscribeToTopic("siswa");

                        Toast.makeText(VerifikasiRegisterActivity.this, "verifikasi berhasil", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        verif.setText("Verifikasi Sukses");

                        Intent intent = new Intent(VerifikasiRegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("siswa");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("guru");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
