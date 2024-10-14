package com.example.urbangreenmobile.Services.Producao;

import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.ItemInspecao;
import com.example.urbangreenmobile.api.models.Producao.TipoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;

public class ProducaoService implements IProducaoService {
    private ApiInterface apiInterface;

    public ProducaoService(ApiInterface apiInterface){
        this.apiInterface = apiInterface;
    }

    private CompletableFuture<List<TipoItem>> listarTipoItensFuture(){
        return CompletableFuture.supplyAsync(() -> {
            try {
                Response<List<TipoItem>> response = apiInterface.getTiposDeItens().execute();
                return response.body();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private CompletableFuture<GetInspecaoResponse> obterInspecaoFuture(int id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Response<GetInspecaoResponse> response = apiInterface.getInspecao(id).execute();
                return response.body();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private List<ItemInspecao> mergeItens(List<TipoItem> tiposItens, GetInspecaoResponse inspecao)
    {
        List<ItemInspecao> itens = new ArrayList<>();

        for (TipoItem item : tiposItens) {

            ItemInspecao novoItem;

            if (inspecao != null) {
                Optional<ItemInspecao> itemRealizadoOptional = inspecao.getItens()
                        .stream()
                        .filter(x -> x.getTipoId() == item.getId())
                        .findFirst();

                if (itemRealizadoOptional.isPresent())
                {
                    ItemInspecao itemRealizado = itemRealizadoOptional.get();
                    novoItem = new ItemInspecao(item.getNome(), item.getId(),
                            itemRealizado.getData(), itemRealizado.isRealizado());
                } else {
                    novoItem = new ItemInspecao(item.getNome(), item.getId());
                }
            } else {
                novoItem = new ItemInspecao(item.getNome(), item.getId());
            }

            itens.add(novoItem);
        }

        return itens;
    }

    @Override
    public GetInspecaoResponse getInspecao(int id) {
        CompletableFuture<List<TipoItem>> listarTipoItensFuture = listarTipoItensFuture();
        CompletableFuture<GetInspecaoResponse> obterInspecaoFuture = obterInspecaoFuture(id);

        CompletableFuture<List<ItemInspecao>> combinedFuture = listarTipoItensFuture
                .thenCombine(obterInspecaoFuture, this::mergeItens);

        try {
            GetInspecaoResponse inspecao = obterInspecaoFuture.get();
            List<ItemInspecao> itens = combinedFuture.get();
            inspecao.setItens(itens);

            return inspecao;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
