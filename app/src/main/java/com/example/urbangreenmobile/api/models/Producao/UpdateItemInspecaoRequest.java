package com.example.urbangreenmobile.api.models.Producao;

import java.time.LocalDateTime;

public class UpdateItemInspecaoRequest {
    private int tipoId;
    private boolean realizado;
    private LocalDateTime data;

    public UpdateItemInspecaoRequest(int tipoId, boolean realizado, LocalDateTime data){
        this.tipoId = tipoId;
        this.realizado = realizado;
        this.data = data;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data){
        this.data = data;
    }
}
