package com.example.urbangreenmobile.api.models.Producao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemInspecao {
    private LocalDateTime data;
    private String nome;  // Nome do tipo no GET
    private int tipoId;   // ID do tipo no POST/PUT
    private boolean realizado;

    public ItemInspecao(String nome, int tipoId, LocalDateTime data, boolean realizado){
        this.nome = nome;
        this.tipoId = tipoId;
        this.data = data;
        this.realizado = realizado;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ItemInspecao(String nome, int tipoId){
        this.nome = nome;
        this.tipoId = tipoId;
        this.data = LocalDateTime.now();
    }

    // Getters and Setters
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDataInText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.data = LocalDateTime.now();
        this.realizado = realizado;
    }
}

