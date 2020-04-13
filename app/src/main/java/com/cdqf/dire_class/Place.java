package com.cdqf.dire_class;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Place {

    private View view;

    private TextView tvMapTitle;

    private ImageView ivMapImage;

    public Place(View view, TextView tvMapTitle, ImageView ivMapImage) {
        this.view = view;
        this.tvMapTitle = tvMapTitle;
        this.ivMapImage = ivMapImage;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTvMapTitle() {
        return tvMapTitle;
    }

    public void setTvMapTitle(TextView tvMapTitle) {
        this.tvMapTitle = tvMapTitle;
    }

    public ImageView getIvMapImage() {
        return ivMapImage;
    }

    public void setIvMapImage(ImageView ivMapImage) {
        this.ivMapImage = ivMapImage;
    }
}
