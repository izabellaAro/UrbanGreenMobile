package com.example.urbangreenmobile.api;

import com.example.urbangreenmobile.api.models.Fornecedor;
import com.example.urbangreenmobile.api.models.LoginRequest;
import com.example.urbangreenmobile.api.models.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiInterface {
    @POST("Login/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("Fornecedor?skip=0&take=50")
    Call<List<Fornecedor>> getFornecedores();
}
