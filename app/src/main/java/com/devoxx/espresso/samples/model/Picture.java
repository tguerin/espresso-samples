package com.devoxx.espresso.samples.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Picture {
    public final int drawableRes;
    public final String title;

    @JsonCreator
    public Picture(@JsonProperty("drawableRes") int drawableRes, @JsonProperty("title") String title) {
        this.drawableRes = drawableRes;
        this.title = title;
    }
}
