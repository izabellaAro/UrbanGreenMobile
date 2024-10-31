package com.example.urbangreenmobile.api.models.Pedido;

import java.util.List;

public class CreatePedidoRequest {
    private List<ItemPedidoRequest> itensPedido;
    private String nomeComprador;

    public List<ItemPedidoRequest> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedidoRequest> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }
}
