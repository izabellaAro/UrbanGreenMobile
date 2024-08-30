package com.example.urbangreenmobile.api.models.Fornecedor;

public class GetFornecedorResponse {
    private int fornecedorId;
    private String nome;
    private String nomePJ;
    private String insumo;
    public String telefone;
    public String email;
    public double valor;

    public GetFornecedorResponse(String nomePJ, String insumo, String telefone, String email, double valor, int id){
        this.fornecedorId = id;
        this.nome = nome;
        this.nomePJ = nomePJ;
        this.insumo = insumo;
        this.telefone = telefone;
        this.email = email;
        this.valor = valor;
    }

    public GetFornecedorResponse(){}

    public String getNome(){
        return nome;
    }

    public String getNomePJ(){
        return nomePJ;
    }

    public String getInsumo(){
        return insumo;
    }

    public String getTelefone(){
        return telefone;
    }

    public String getEmail(){
        return email;
    }

    public double getValor(){
        return valor;
    }

    public int getId(){
        return fornecedorId;
    }

}
