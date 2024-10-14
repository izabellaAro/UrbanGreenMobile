package com.example.urbangreenmobile.Services.Producao;

import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;

public interface IProducaoService {
    GetInspecaoResponse getInspecao(int id);
}
