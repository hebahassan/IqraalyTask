package com.example.heba.iqraalytask.helper;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.heba.iqraalytask.app.GlideApp;

public class GlideHelper {
    @BindingAdapter("imageUrl")
    public static void setImage(ImageView imageView, String url){
        Context context = imageView.getContext();
        GlideApp.with(context)
                .load(url)
                .into(imageView);
    }
}
