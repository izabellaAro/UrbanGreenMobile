package com.example.urbangreenmobile.api.models.Producao;

public class ItemInspecao {
    private String data;  // Formato de data ISO 8601
    private String nome;  // Nome do tipo no GET
    private int tipoId;   // ID do tipo no POST/PUT
    private boolean realizado;

    // Getters and Setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
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
        this.realizado = realizado;
    }
}

