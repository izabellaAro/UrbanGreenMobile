package com.example.urbangreenmobile.api.models.Pedido;

public class ItemPedidoRequest {
    private int quantidade;
    private int produtoId;

    public ItemPedidoRequest(int quantidade, int produtoId) {
        this.quantidade = quantidade;
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }
}


