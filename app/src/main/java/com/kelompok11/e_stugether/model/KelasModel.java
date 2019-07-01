package com.kelompok11.e_stugether.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class KelasModel implements Serializable {
    private String keyKelas,namaKelas,idPengajar,passwordKelas;

    public KelasModel() {
    }

    public String getKeyKelas() {
        return keyKelas;
    }

    public void setKeyKelas(String keyKelas) {
        this.keyKelas = keyKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getIdPengajar() {
        return idPengajar;
    }

    public void setIdPengajar(String idPengajar) {
        this.idPengajar = idPengajar;
    }

    public String getPasswordKelas() {
        return passwordKelas;
    }

    public void setPasswordKelas(String passwordKelas) {
        this.passwordKelas = passwordKelas;
    }

    public KelasModel(String namaKelas, String idPengajar, String passwordKelas) {
        this.namaKelas = namaKelas;
        this.passwordKelas = passwordKelas;
        this.idPengajar = idPengajar;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("namaKelas",namaKelas);
        result.put("passwordKelas",passwordKelas);
        result.put("idPengajar",idPengajar);
        return result;
    }
}
