package com.devoxx.espresso.samples.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.devoxx.espresso.samples.model.Picture;
import com.devoxx.espresso.samples.R;

public class PicturesAdapter extends BaseAdapter {

    private Context context;
    private Picture[] pictures;

    public PicturesAdapter(Context context, Picture[] pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        return pictures.length;
    }

    @Override
    public Picture getItem(int position) {
        return pictures[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.picture_item, parent, false);
        }
        ((PictureItemView) convertView).bind(getItem(position));
        return convertView;
    }
}
