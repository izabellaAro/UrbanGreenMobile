package com.example.urbangreenmobile.api.models.Pedido;

import java.util.List;

public class CreatePedidoRequest {
    private List<Integer> produtos;
    private String nomeComprador;

    public CreatePedidoRequest(List<Integer> produtos, String nomeComprador) {
        this.produtos = produtos;
        this.nomeComprador = nomeComprador;
    }

    public List<Integer> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Integer> produtos) {
        this.produtos = produtos;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }
}
