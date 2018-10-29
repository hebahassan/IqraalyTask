package com.example.heba.iqraalytask.controller;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.interfaces.ConnectivityInterface;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    ConnectivityInterface listener;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityHelper.isConnectedToNetwork(context)){
            if(listener != null)
                listener.onNetworkConnection();
        }
        else {
            Toast.makeText(context, context.getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void setNetworkListener(ConnectivityInterface listener){
        ConnectivityChangeReceiver.this.listener = listener;
    }
}
