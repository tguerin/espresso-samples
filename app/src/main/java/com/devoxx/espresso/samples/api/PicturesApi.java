package com.devoxx.espresso.samples.api;

import com.devoxx.espresso.samples.model.Picture;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface PicturesApi {

    @GET("/pictures/{type}")
    void getPicturesForType(@Path("type") int type, Callback<Picture[]> picturesCallback);
}
