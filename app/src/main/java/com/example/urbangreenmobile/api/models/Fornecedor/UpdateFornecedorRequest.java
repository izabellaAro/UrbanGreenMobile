package com.example.urbangreenmobile.api.models.Fornecedor;

public class UpdateFornecedorRequest {
    private int id;
    private String nome;
    private String email;
    private String telefone;;

    public UpdateFornecedorRequest(int id, String nome, String email, String telefone){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public UpdateFornecedorRequest(){}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
}
