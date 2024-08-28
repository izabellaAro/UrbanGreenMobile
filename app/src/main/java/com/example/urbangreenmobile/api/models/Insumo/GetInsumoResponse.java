package com.example.urbangreenmobile.api.models.Insumo;

public class GetInsumoResponse {
    private int id;
    private String nome;

    public GetInsumoResponse(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
