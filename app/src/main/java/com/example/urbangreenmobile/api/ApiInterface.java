package com.example.urbangreenmobile.api;

import com.example.urbangreenmobile.api.models.Fornecedor.CreateFornecedorRequest;
import com.example.urbangreenmobile.api.models.Fornecedor.GetFornecedorResponse;
import com.example.urbangreenmobile.api.models.Insumo.GetInsumoResponse;
import com.example.urbangreenmobile.api.models.Login.LoginRequest;
import com.example.urbangreenmobile.api.models.Login.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiInterface {
    @POST("Login/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("Fornecedor?skip=0&take=50")
    Call<List<GetFornecedorResponse>> getFornecedores();

    @POST("Fornecedor")
    Call<Void> criarFornecedor(@Body CreateFornecedorRequest fornecedor);

    @GET("Insumo?skip=0&take=50")
    Call<List<GetInsumoResponse>> getInsumos();

}
