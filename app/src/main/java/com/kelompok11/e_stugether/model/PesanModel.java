package com.kelompok11.e_stugether.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PesanModel implements Serializable {
    private String idPesan;
    private String idPengirim;
    private String idReply;
    private String pesan;
    private long waktuKirim;
    public PesanModel() {
    }

    public String getIdPesan() {
        return idPesan;
    }

    public void setIdPesan(String idPesan) {
        this.idPesan = idPesan;
    }

    public long getWaktuKirim() {
        return waktuKirim;
    }

    public void setWaktuKirim(long waktuKirim) {
        this.waktuKirim = waktuKirim;
    }

    public String getIdPengirim() {
        return idPengirim;
    }

    public void setIdPengirim(String idPengirim) {
        this.idPengirim = idPengirim;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public PesanModel(String idPengirim, String idReply, String pesan, long waktuKirim) {
        this.idPengirim = idPengirim;
        this.idReply = idReply;
        this.pesan = pesan;
        this.waktuKirim = waktuKirim;
    }

    public String getIdReply() {
        return idReply;
    }

    public void setIdReply(String idReply) {
        this.idReply = idReply;
    }

    @Exclude
    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<>();
        result.put("idPengirim",idPengirim);
        result.put("idReply",idReply);
        result.put("pesan",pesan);
        result.put("waktuKirim",waktuKirim);
        return result;
    }
}
