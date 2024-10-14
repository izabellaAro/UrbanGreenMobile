package com.example.urbangreenmobile.api.models.Producao;

public class ItemInspecao {
    private String data;
    private String nome;  // Nome do tipo no GET
    private int tipoId;   // ID do tipo no POST/PUT
    private boolean realizado;

    public ItemInspecao(String nome, int tipoId, String data, boolean realizado){
        this.nome = nome;
        this.tipoId = tipoId;
        this.data = data;
        this.realizado = realizado;
    }

    public ItemInspecao(String nome, int tipoId){
        this.nome = nome;
        this.tipoId = tipoId;
        this.data = "";
    }

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

