package com.example.heba.iqraalytask.ui.book;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.example.heba.iqraalytask.component.BookDataComponent;
import com.example.heba.iqraalytask.component.DaggerBookDataComponent;
import com.example.heba.iqraalytask.network.model.Book;
import com.example.heba.iqraalytask.network.model.BookResponse;
import com.example.heba.iqraalytask.network.model.Data;
import com.example.heba.iqraalytask.network.ApiService;
import com.example.heba.iqraalytask.ui.audio.BookAudioActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookViewModel extends AndroidViewModel {
    private ApiService apiService;
    private MutableLiveData<Integer> busy; //0 -> visible, 8 -> gone
    private MutableLiveData<Integer> retry;
    private MutableLiveData<Data> bookResLiveData = new MutableLiveData<>();

    public BookViewModel(@NonNull Application application) {
        super(application);

        BookDataComponent bookDataComponent = DaggerBookDataComponent.create();
        apiService = bookDataComponent.getApiService();

        getRetry().setValue(8);
    }

    public MutableLiveData<Integer> getBusy() {
        if(busy == null)
            busy = new MutableLiveData<>();
        return busy;
    }

    public MutableLiveData<Integer> getRetry() {
        if(retry == null)
            retry = new MutableLiveData<>();

        return retry;
    }

    public MutableLiveData<Data> getBookData(){
        getBusy().setValue(0);

        apiService.getBookData().enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getCode().equals(0)){
                        bookResLiveData.setValue(response.body().getData());
                    }
                    getRetry().setValue(8);
                }
                else {
                    bookResLiveData.setValue(null);
                    getRetry().setValue(0);
                }

                getBusy().setValue(8);
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                bookResLiveData.setValue(null);
                getBusy().setValue(8);
                getRetry().setValue(0);
            }
        });

        return bookResLiveData;
    }

    public void onRetryClick(){
        getRetry().setValue(8);
        getBookData();
    }
}
