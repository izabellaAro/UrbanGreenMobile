package com.example.urbangreenmobile.api.Integrations;

import com.example.urbangreenmobile.api.Interceptors.AuthInterceptor;
import com.example.urbangreenmobile.utils.TokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:5173/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(TokenManager tokenManager) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
