package com.ziggyqubert.android.baking_app.model;

import android.net.Uri;

import java.io.Serializable;

public class Step implements Serializable {
    public Integer id;
    public String shortDescription;
    public String description;
    public String thumbnailURL;
    public String videoURL;

    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public String toString() {
        return getShortDescription();
    }

    public String getDescription() {
        return description;
    }

    public Boolean hasVideo() {
        return !videoURL.isEmpty();
    }

    public String getVideoURL() {
        return videoURL;
    }

    public Uri getVideoUri() {
        return Uri.parse(getVideoURL());
    }
}
