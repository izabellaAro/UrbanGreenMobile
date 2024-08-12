package com.example.urbangreenmobile.api;

import com.example.urbangreenmobile.api.models.LoginRequest;
import com.example.urbangreenmobile.api.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("Login/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
