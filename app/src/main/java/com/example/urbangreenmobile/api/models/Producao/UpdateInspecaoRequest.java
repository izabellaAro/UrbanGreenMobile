package com.example.urbangreenmobile.api.models.Producao;

import java.util.List;

public class UpdateInspecaoRequest {
    private int produtoId;
    private String registro;
    private List<ItemInspecao> itens;

    // Getters and Setters
    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public List<ItemInspecao> getItens() {
        return itens;
    }

    public void setItens(List<ItemInspecao> itens) {
        this.itens = itens;
    }
}

