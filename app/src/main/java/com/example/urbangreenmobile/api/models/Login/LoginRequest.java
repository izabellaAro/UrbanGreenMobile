package com.example.urbangreenmobile.api.models.Login;

public class LoginRequest {
    private String nomeUsuario;
    private String senha;

    public LoginRequest(String nomeUsuario, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public String getUsuario() {
        return nomeUsuario;
    }

    public void setUsuario(String nomeUsuario) {
        this.nomeUsuario = this.nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
