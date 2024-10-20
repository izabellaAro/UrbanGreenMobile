package com.example.urbangreenmobile.ui.Producao;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import com.example.urbangreenmobile.api.models.Producao.UpdateInspecaoRequest;
import com.example.urbangreenmobile.api.models.Producao.UpdateItemRequest;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarProducaoActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerItemEditarProducao;
    private ItemEditarProducaoAdapter itemEditarProducaoAdapter;
    private IProducaoService producaoService;
    private GetInspecaoResponse inspecaoResponse;
    private TextView registroInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_producao);

        setupApiInterface();

        producaoService = new ProducaoService(this.apiInterface);
        registroInput = findViewById(R.id.input_registro);

        setupRecyclerView();
        obterInspecao();
        configurarListenerBotaoSalvar();
    }

    private void setupApiInterface() {
        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
    }

    private void setupRecyclerView() {
        itemEditarProducaoAdapter = new ItemEditarProducaoAdapter();
        recyclerItemEditarProducao = findViewById(R.id.recyclerViewEditarProducao);
        recyclerItemEditarProducao.setLayoutManager(new LinearLayoutManager(this));
        recyclerItemEditarProducao.setAdapter(itemEditarProducaoAdapter);
    }

    private void obterInspecao(){
        inspecaoResponse = producaoService.getItensInspecao(1);

        itemEditarProducaoAdapter.setInspecao(inspecaoResponse);
        registroInput.setText(inspecaoResponse.getRegistro());
    }

    private void atualizarInspecao(){
        UpdateInspecaoRequest request = new UpdateInspecaoRequest();
        request.setProdutoId(inspecaoResponse.getProdutoId());
        request.setRegistro(registroInput.getText().toString());

        List<UpdateItemRequest> itens = inspecaoResponse.getItens().stream()
                .map(item -> new UpdateItemRequest(item.getTipoId(), item.isRealizado()))
                .collect(Collectors.toList());

        request.setItens(itens);

        apiInterface.atualizarInspecao(inspecaoResponse.getId(), request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarProducaoActivity.this, "Inspeção atualizada com sucesso", Toast.LENGTH_SHORT).show();
                    obterInspecao();
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

    private void configurarListenerBotaoSalvar(){
        ImageButton buttonSalvar = findViewById(R.id.btn_salvar);

        buttonSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(inspecaoResponse.getId() != 0){
                    atualizarInspecao();
                }
                else {
                    // TODO: Adicionar caso em que nao existe uma inspecao
                    Toast.makeText(getApplicationContext(), "Botão clicado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
