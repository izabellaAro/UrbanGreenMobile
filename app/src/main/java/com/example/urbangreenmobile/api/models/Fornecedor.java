package com.example.urbangreenmobile.api.models;

public class Fornecedor {
    private int fornecedorId;
    private String nome;
    private int pessoaJuridicaId;
    private int insumoId;
    private String nomePJ;
    private String insumo;
    public String telefone;
    public String email;
    public double valor;

    public Fornecedor(int fornecedorId, String nome, int pessoaJuridicaId, int insumoId, String nomePJ, String insumo, String telefone, String email, double valor){
        this.fornecedorId = fornecedorId;
        this.nome = nome;
        this.pessoaJuridicaId = pessoaJuridicaId;
        this.insumoId = insumoId;
        this.nomePJ = nomePJ;
        this.insumo = insumo;
        this.telefone = telefone;
        this.email = email;
        this.valor = valor;
    }

    public int getFornecedorId(){
        return fornecedorId;
    }

    public void setFornecedorId(int fornecedorId){
        this.fornecedorId = fornecedorId;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public int getPessoaJuridicaId(){
        return pessoaJuridicaId;
    }

    public void setPessoaJuridicaId(int pessoaJuridicaId){
        this.pessoaJuridicaId = pessoaJuridicaId;
    }

    public int getInsumoId(){
        return insumoId;
    }

    public void setInsumoId(int insumoId){
        this.insumoId = insumoId;
    }

    public String getNomePJ(){
        return nomePJ;
    }

    public void setNomePJ(String nomePJ){
        this.nomePJ = nomePJ;
    }

    public String getInsumo(){
        return insumo;
    }

    public void setInsumo(String insumo){
        this.insumo = insumo;
    }

    public String getTelefone(){
        return telefone;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public double getValor(){
        return valor;
    }

    public void setValor(double valor){
        this.valor = valor;
    }
}
