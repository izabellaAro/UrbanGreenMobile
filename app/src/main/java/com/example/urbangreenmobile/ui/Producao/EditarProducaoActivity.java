package com.example.urbangreenmobile.ui.Producao;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.Services.Producao.IProducaoService;
import com.example.urbangreenmobile.Services.Producao.ProducaoService;
import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.Integrations.ApiService;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.ItemInspecao;
import com.example.urbangreenmobile.api.models.Producao.UpdateInspecaoRequest;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarProducaoActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerEditarProducao;
    private EditarProducaoAdapter editarProducaoAdapter;
    private IProducaoService producaoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_producao);

        setupApiInterface();
        producaoService = new ProducaoService(this.apiInterface);

        setupRecyclerView();
        listarTipoItens();
    }

    private void setupApiInterface() {
        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
    }

    private void setupRecyclerView() {
        recyclerEditarProducao = findViewById(R.id.recyclerViewEditarProducao);
        recyclerEditarProducao.setLayoutManager(new LinearLayoutManager(this));

        editarProducaoAdapter = new EditarProducaoAdapter();
        recyclerEditarProducao.setAdapter(editarProducaoAdapter);
//        editarProducaoAdapter.setOnEditClickListener(this::abrirDialogProduto);
    }

    private void listarTipoItens(){
        editarProducaoAdapter.setInspecao(producaoService.getItensInspecao(1));
    }

    private void salvarInspecao (Dialog dialog, GetInspecaoResponse inspecao, TextView
            tipoInspecao, TextView dataInspecao, CheckBox
                                         isRealizado){
        //atualizarInspecaoExistente(inspecao.getId(), tipoInspecao.getText().toString(), dataInspecao.getText().toString(), isRealizado.isChecked());
        dialog.dismiss();
    }

    private void atualizarInspecaoExistente ( int id, String registro, CheckBox isChecked){
        UpdateInspecaoRequest inspecaoAtualizada = new UpdateInspecaoRequest();
        inspecaoAtualizada.setRegistro(registro);
        inspecaoAtualizada.setItens((List<ItemInspecao>) isChecked);
        atualizarInspecao(id, inspecaoAtualizada);
    }

    private void atualizarInspecao ( int id, UpdateInspecaoRequest inspecao){
        apiInterface.atualizarInspecao(id, inspecao).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //listarInspecao();
                    Toast.makeText(EditarProducaoActivity.this, "Inspeção atualizada com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarProducaoActivity.this, "Erro ao atualizar inspeção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditarProducaoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirDialogInspecao(View view) {
        Dialog dialog = new Dialog(EditarProducaoActivity.this);
        dialog.setContentView(R.layout.editar_producao);

        listarTipoItens();
    }

//    public void filtrarInspecoesPorData(int id, String data) {
//        Call<List<GetInspecaoResponse>> call = apiInterface.getInspecao(id);
//        call.enqueue(new Callback<List<GetInspecaoResponse>>() {
//            @Override
//            public void onResponse(Call<List<GetInspecaoResponse>> call, Response<List<GetInspecaoResponse>> response) {
//                if (response.isSuccessful()) {
//                    List<GetInspecaoResponse> inspecoes = response.body();
//                    // Atualizar o adapter com os dados retornados
//                    //editarProducaoAdapter.updateInspecoes(inspecoes);
//                } else {
//                    Toast.makeText(EditarProducaoActivity.this, "Erro ao carregar inspeções", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<GetInspecaoResponse>> call, Throwable t) {
//                Toast.makeText(EditarProducaoActivity.this, "Erro ao se conectar à API", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
