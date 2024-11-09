package com.example.urbangreenmobile.api.Integrations;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.urbangreenmobile.api.Interceptors.AuthInterceptor;
import com.example.urbangreenmobile.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:5173/api/";
    private static Retrofit retrofit = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Retrofit getClient(TokenManager tokenManager) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .build();

            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .optionalStart()
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .optionalEnd()
                    .toFormatter();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (dateTime, type, jsonSerializationContext) ->
                            jsonSerializationContext.serialize(dateTime.format(formatter)))
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                            LocalDateTime.parse(json.getAsString(), formatter))
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
