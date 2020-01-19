package com.gmail.arifkur10027.smpital_fallah.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.arifkur10027.smpital_fallah.DetailPengumumanActivity;
import com.gmail.arifkur10027.smpital_fallah.Model.Pengumuman;
import com.gmail.arifkur10027.smpital_fallah.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Entong on 7/2/2019.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.ViewHolder> {

    private Context mContext;
    private List<Pengumuman> mPengumuman;

    public PengumumanAdapter ( Context mContext, List<Pengumuman> mPengumuman){
        this.mPengumuman = mPengumuman;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pengumuman, parent, false);
        return new PengumumanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Pengumuman pengumuman = mPengumuman.get(position);
        holder.txtJudul.setText(pengumuman.getJudul());
        holder.txtTanggalTerbit.setText(formatWaktu(pengumuman.getwaktuTerbit()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailPengumumanActivity.class);
                intent.putExtra("pengumumanid", pengumuman.getPengumumanid());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPengumuman.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtJudul;
        public TextView txtTanggalTerbit;

        public ViewHolder(View itemView) {
            super(itemView);

            txtJudul = itemView.findViewById(R.id.txtJudul);
            txtTanggalTerbit = itemView.findViewById(R.id.tglTerbit);
        }
    }

    public String formatWaktu(long time) {
        Long ubahpositif = Math.abs(time);
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
        return sfd.format(new Date(ubahpositif));
    }
}
