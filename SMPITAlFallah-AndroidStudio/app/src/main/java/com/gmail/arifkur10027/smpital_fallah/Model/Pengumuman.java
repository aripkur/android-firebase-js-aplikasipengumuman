package com.gmail.arifkur10027.smpital_fallah.Model;

/**
 * Created by Entong on 7/2/2019.
 */

public class Pengumuman {
    private String pengumumanid;
    private String judul;
    private String isi;
    private Long waktuterbit;
    private String usergroup;
    private String status;

    public Pengumuman(String pengumumanid, String judul, String isi, Long waktuterbit, String usergroup, String status) {
        this.pengumumanid = pengumumanid;
        this.judul = judul;
        this.isi = isi;
        this.waktuterbit = waktuterbit;
        this.usergroup = usergroup;
        this.status = status;
    }

    public String getUserGroup() {
        return usergroup;
    }

    public void setUserGroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public Pengumuman() {
    }

    public String getPengumumanid() {
        return pengumumanid;
    }

    public void setPengumumanid(String pengumumanid) {
        this.pengumumanid = pengumumanid;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public Long getwaktuTerbit() {
        return waktuterbit;
    }

    public void setwaktuTerbit(Long waktuterbit) {
        this.waktuterbit = waktuterbit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
