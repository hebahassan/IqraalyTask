package com.example.heba.iqraalytask.ui.book;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.controller.ConnectivityChangeReceiver;
import com.example.heba.iqraalytask.controller.ConnectivityHelper;
import com.example.heba.iqraalytask.databinding.ActivityBookBinding;
import com.example.heba.iqraalytask.interfaces.ConnectivityInterface;
import com.example.heba.iqraalytask.network.model.Data;
import com.example.heba.iqraalytask.ui.audio.BookAudioActivity;

public class BookActivity extends AppCompatActivity implements ConnectivityInterface {
    ActivityBookBinding binding;
    BookViewModel viewModel;

    ConnectivityChangeReceiver connectivityChangeReceiver;
    ConnectivityInterface connectivityInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book);
        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        setReceiver();
        onStartActivity();
    }

    private void setReceiver(){
        connectivityChangeReceiver = new ConnectivityChangeReceiver();
        connectivityInterface = this;
        connectivityChangeReceiver.setNetworkListener(connectivityInterface);
        registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void onStartActivity(){
        viewModel.getBookData().observe(this, new Observer<Data>() {
            @Override
            public void onChanged(@Nullable Data data) {
                if(data != null){
                    binding.setData(data);
                    binding.toolbarLayout.title.setText(data.getBook().getName());

                    openAudioActivity();
                }
            }
        });
    }

    private void openAudioActivity(){
        binding.BTPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectivityHelper.isConnectedToNetwork(BookActivity.this)){
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookAudioActivity.class);
                    intent.putExtra("Book", binding.getData().getBook());
                    context.startActivity(intent);
                }else {
                    Snackbar.make(binding.containerLayout, getString(R.string.check_connection),
                            Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(connectivityChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onNetworkConnection() {
        if(binding.getData() == null)
            onStartActivity();
    }
}
