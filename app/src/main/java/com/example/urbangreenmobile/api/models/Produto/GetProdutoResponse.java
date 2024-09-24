package com.example.urbangreenmobile.api.models.Produto;

public class GetProdutoResponse {
    private int id;
    private String nome;
    private String imagemUrl;
    private int quantidade;
    private double valor;

    public GetProdutoResponse(int id, String nome, String imagemUrl, int quantidade, double valor){
        this.id = id;
        this.nome = nome;
        this.imagemUrl = imagemUrl;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }

    public String getImagemUrl() {
        System.out.println(imagemUrl);
        return imagemUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
