package com.example.heba.iqraalytask.controller;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

public class BindingHelper {
    @BindingAdapter("imageUrl")
    public static void setImage(ImageView imageView, String url){
        Context context = imageView.getContext();
        GlideApp.with(context)
                .load(url)
                .into(imageView);
    }
}
