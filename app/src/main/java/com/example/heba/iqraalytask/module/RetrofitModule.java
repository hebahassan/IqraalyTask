package com.example.heba.iqraalytask.module;

import android.util.Log;

import com.example.heba.iqraalytask.interfaces.ApiServiceApplicationScope;
import com.example.heba.iqraalytask.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    @Provides
    public ApiService apiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    @ApiServiceApplicationScope
    @Provides
    public Retrofit retrofit(GsonConverterFactory gsonConverterFactory){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://staging.app.iqraaly.com/api/")
                .addConverterFactory(gsonConverterFactory)
                .build();

        Log.d("Retrofit", "" + retrofit.hashCode());

        return retrofit;
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}
