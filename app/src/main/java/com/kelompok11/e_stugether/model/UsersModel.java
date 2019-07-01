package com.kelompok11.e_stugether.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UsersModel implements Serializable {
    private String id;
    private String username;
    private String role;
    private String imageUrl;
    private String email;


    public UsersModel() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsersModel(String username, String role, String imageUrl, String email) {
        this.username = username;
        this.role = role;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    @Exclude
    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<>();
        return result;
    }
}
