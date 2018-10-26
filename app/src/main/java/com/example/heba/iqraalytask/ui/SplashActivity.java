package com.example.heba.iqraalytask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.helper.Const;
import com.example.heba.iqraalytask.helper.LocalHelper;
import com.example.heba.iqraalytask.ui.book.BookActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LocalHelper.setLocale(this, Const.AR);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, BookActivity.class));
                finish();
            }
        }, 1000);
    }
}
