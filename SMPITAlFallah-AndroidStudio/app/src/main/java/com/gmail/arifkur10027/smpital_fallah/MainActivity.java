package com.gmail.arifkur10027.smpital_fallah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.arifkur10027.smpital_fallah.Adapter.PengumumanAdapter;
import com.gmail.arifkur10027.smpital_fallah.Model.Pengumuman;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PengumumanAdapter pengumumanAdapter;
    private List<Pengumuman> mPengumuman;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pengumuman");

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mPengumuman = new ArrayList<>();

        ambilPengumuman();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.keluar:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
                return true;
        }
        return false;
    }

    private void ambilPengumuman() {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("usergroup");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String usergroup = dataSnapshot.getValue(String.class);
                Query ambildata = FirebaseDatabase.getInstance().getReference("Pengumuman").orderByChild("waktuterbit");

                ambildata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mPengumuman.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Pengumuman pengumuman = snapshot.getValue(Pengumuman.class);

                            if (pengumuman != null) {
                                if (pengumuman.getStatus().equals("terbit") && pengumuman.getUserGroup().equals(usergroup) || pengumuman.getUserGroup().equals("siswa, guru")) {
                                    mPengumuman.add(pengumuman);
                                }
                            }
                        }
                        pengumumanAdapter = new PengumumanAdapter(MainActivity.this, mPengumuman);
                        recyclerView.setAdapter(pengumumanAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
