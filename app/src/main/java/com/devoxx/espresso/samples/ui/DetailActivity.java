package com.devoxx.espresso.samples.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.devoxx.espresso.samples.R;
import com.devoxx.espresso.samples.model.Data;
import com.devoxx.espresso.samples.model.Picture;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DetailActivity extends ActionBarActivity {

    public static final String EXTRA_POSITION = "com.devoxx.espresso.samples.EXTRA_POSITION";
    public static final String EXTRA_PICTURES_TYPE = "com.devoxx.espresso.samples.EXTRA_PICTURES_TYPE";

    @InjectView(R.id.picture_detail_bg) ImageView pictureBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.inject(this);
        int picturePosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        int pitcturesType = getIntent().getIntExtra(EXTRA_PICTURES_TYPE, 0);
        Picture picture = Data.getPicturesForType(pitcturesType)[picturePosition];
        pictureBg.setImageResource(picture.drawableRes);
    }

    @OnClick(R.id.scroll_to_me)
    public void buttonClicked(){
        Toast.makeText(this, "Scroll to me button clicked !", Toast.LENGTH_LONG).show();
    }
}
