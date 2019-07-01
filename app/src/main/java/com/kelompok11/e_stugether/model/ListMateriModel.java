package com.kelompok11.e_stugether.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ListMateriModel {
    public String keyMateri;

    public ListMateriModel() {
    }

    public ListMateriModel(String keyMateri) {
        this.keyMateri = keyMateri;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("key",keyMateri);
        return result;
    }
}
