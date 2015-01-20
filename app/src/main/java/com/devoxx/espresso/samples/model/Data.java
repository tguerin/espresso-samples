package com.devoxx.espresso.samples.model;


import android.util.SparseArray;

import com.devoxx.espresso.samples.R;

public class Data {
    private static final Picture[] LANDSCAPES = {
            new Picture(R.drawable.landscape_1, "Landscape 1"),
            new Picture(R.drawable.landscape_2, "Landscape 2"),
            new Picture(R.drawable.landscape_3, "Landscape 3"),
            new Picture(R.drawable.landscape_4, "Landscape 4"),
            new Picture(R.drawable.landscape_5, "Landscape 5"),
            new Picture(R.drawable.landscape_6, "Landscape 6"),
            new Picture(R.drawable.landscape_7, "Landscape 7"),
            new Picture(R.drawable.landscape_8, "Landscape 8")
    };

    private static final Picture[] KITTENS = {
            new Picture(R.drawable.kittens_1, "Kittens 1"),
            new Picture(R.drawable.kittens_2, "Kittens 2"),
            new Picture(R.drawable.kittens_3, "Kittens 3"),
            new Picture(R.drawable.kittens_4, "Kittens 4"),
            new Picture(R.drawable.kittens_5, "Kittens 5"),
            new Picture(R.drawable.kittens_6, "Kittens 6"),
            new Picture(R.drawable.kittens_7, "Kittens 7"),
            new Picture(R.drawable.kittens_8, "Kittens 8")
    };

    private static final SparseArray<Picture[]> PICTURES_BY_TYPE = new SparseArray<Picture[]>() {{
        put(TYPE_LANDSCAPES, LANDSCAPES);
        put(TYPE_KITTENS, KITTENS);
    }};

    public static Picture[] getPicturesForType(int picturesType){
        return PICTURES_BY_TYPE.get(picturesType, new Picture[0]);
    }

    public static final int TYPE_LANDSCAPES = 0;
    public static final int TYPE_KITTENS = 1;

    private Data() {
        // No instances.
    }
}
