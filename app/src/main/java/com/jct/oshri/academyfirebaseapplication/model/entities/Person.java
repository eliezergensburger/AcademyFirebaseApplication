package com.jct.oshri.academyfirebaseapplication.model.entities;

import android.net.Uri;

import com.google.firebase.database.Exclude;

/**
 * Created by mailo on 14/12/2016.
 */

public class Person {
    private long id;
    private String name;
    private String phone;
    private String imageFirebaseUrl;
    private Uri imageLocalUri;


    @Exclude
    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageFirebaseUrl() {
        return imageFirebaseUrl;
    }
    public void setImageFirebaseUrl(String imageFirebaseUrl) {
        this.imageFirebaseUrl = imageFirebaseUrl;
    }

    @Exclude
    public Uri getImageLocalUri() {
        return imageLocalUri;
    }
    public void setImageLocalUri(Uri imageLocalUri) {
        this.imageLocalUri = imageLocalUri;
    }

    public String getFireBaseLocalImageUri() {

        return imageLocalUri.toString();
    }
    public void setFireBaseLocalImageUri(String value) {
        this.imageLocalUri = Uri.parse(value);
    }

}
