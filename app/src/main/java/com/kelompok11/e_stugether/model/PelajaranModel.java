package com.kelompok11.e_stugether.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PelajaranModel implements Serializable {
    private String judulPelajaran,pembahasan;
    public PelajaranModel() {
    }

    public String getPembahasan() {
        return pembahasan;
    }

    public void setPembahasan(String pembahasan) {
        this.pembahasan = pembahasan;
    }

    public String getJudulPelajaran() {
        return judulPelajaran;
    }

    public void setJudulPelajaran(String judulPelajaran) {
        this.judulPelajaran = judulPelajaran;
    }

    public PelajaranModel(String judulPelajaran, String pembahasan) {
        this.judulPelajaran = judulPelajaran;
        this.pembahasan = pembahasan;
    }

    @Exclude
    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<>();
        result.put("judulPelajaran",judulPelajaran);
        result.put("pembahasan",pembahasan);
        return result;
    }
}
