package com.example.urbangreenmobile.api.models.Fornecedor;

public class CreateFornecedorRequest {
    private String nome;
    private int insumoId;
    private PessoaJuridica pessoaJuridica;

    public CreateFornecedorRequest(String nome, int insumoId, PessoaJuridica pessoaJuridica) {
        this.nome = nome;
        this.insumoId = insumoId;
        this.pessoaJuridica = pessoaJuridica;
    }

    public CreateFornecedorRequest() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(int insumoId) {
        this.insumoId = insumoId;
    }

    public PessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }
}
