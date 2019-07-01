package com.kelompok11.e_stugether.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MateriModel implements Serializable {
    private String judul;
    private String pengupload;
    private String link;

    public MateriModel() {
    }

    public MateriModel(String judul, String pengupload, String link) {
        this.judul = judul;
        this.pengupload = pengupload;
        this.link = link;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("judul",judul);
        result.put("pengupload",pengupload);
        result.put("link",link);
        return result;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPengupload() {
        return pengupload;
    }

    public void setPengupload(String pengupload) {
        this.pengupload = pengupload;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
