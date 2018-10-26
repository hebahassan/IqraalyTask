package com.example.heba.iqraalytask.network;

import com.example.heba.iqraalytask.network.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("show/1203")
    Call<BookResponse> getBookData();
}
