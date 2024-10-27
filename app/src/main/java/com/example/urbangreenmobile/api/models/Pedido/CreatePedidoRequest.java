package com.example.urbangreenmobile.api.models.Pedido;

import java.util.List;

public class CreatePedidoRequest {
    private List<ItemPedidoRequest> itens;
    private String nomeComprador;

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }
}
