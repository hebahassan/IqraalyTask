package com.example.heba.iqraalytask.component;

import com.example.heba.iqraalytask.interfaces.ApiServiceApplicationScope;
import com.example.heba.iqraalytask.module.RetrofitModule;
import com.example.heba.iqraalytask.network.ApiService;

import dagger.Component;


@ApiServiceApplicationScope
@Component(modules = RetrofitModule.class)
public interface BookDataComponent {
    ApiService getApiService();
}
