package com.devoxx.espresso.samples.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.devoxx.espresso.samples.model.Picture;
import com.devoxx.espresso.samples.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PictureItemView extends FrameLayout {

    @InjectView(R.id.picture_content) ImageView pictureContent;
    @InjectView(R.id.picture_title) TextView pictureTitle;

    public PictureItemView(Context context) {
        super(context);
    }

    public PictureItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void bind(Picture picture) {
        Picasso.with(getContext()).load(picture.drawableRes).fit().centerCrop().into(pictureContent);
        pictureTitle.setText(picture.title);
    }
}
