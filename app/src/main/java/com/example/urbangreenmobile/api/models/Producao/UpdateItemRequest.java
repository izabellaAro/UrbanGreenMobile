package com.example.urbangreenmobile.api.models.Producao;

import java.time.LocalTime;
import java.util.Date;

public class UpdateItemRequest {
    private int tipoId;
    private boolean realizado;
    private LocalTime data;

    public UpdateItemRequest(int tipoId, boolean realizado){
        this.tipoId = tipoId;
        this.realizado = realizado;
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

    public LocalTime getData() {
        return data;
    }

    public void setData(LocalTime data) {
        this.data = data;
    }
}
