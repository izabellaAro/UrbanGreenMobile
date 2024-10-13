package com.example.urbangreenmobile.ui.Producao;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.ApiInterface;
import com.example.urbangreenmobile.api.ApiService;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.ItemInspecao;
import com.example.urbangreenmobile.api.models.Producao.TipoItem;
import com.example.urbangreenmobile.api.models.Producao.UpdateInspecaoRequest;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducaoActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private EditText editTextDataFiltro;
    private ProducaoAdapter producaoAdapter;
    private List<TipoItem> itensInspecao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_producao);

        editTextDataFiltro = findViewById(R.id.editTextDataFiltro);

        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);

        findViewById(R.id.editTextDataFiltro).setOnClickListener(v -> {
            String dataFiltro = editTextDataFiltro.getText().toString();
            if (!dataFiltro.isEmpty()) {
                //filtrarInspecoesPorData(dataFiltro);
            } else {
                Toast.makeText(this, "Por favor, insira uma data válida", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerViewProducao = findViewById(R.id.recyclerViewEditarProducao);
        recyclerViewProducao.setLayoutManager(new LinearLayoutManager(this));

        //producaoAdapter = new EditarProducaoAdapter();
        recyclerViewProducao.setAdapter(producaoAdapter);

        ImageButton editar = findViewById(R.id.edit_button);
        editar.setOnClickListener(this::abrirDialogInspecao);
    }

    private void abrirDialogInspecao(View view) {
        Dialog dialog = new Dialog(ProducaoActivity.this);
        dialog.setContentView(R.layout.editar_producao);

        listarTipoItens();
    }

    public void filtrarInspecoesPorData(int id, String data) {
        Call<List<GetInspecaoResponse>> call = apiInterface.getInspecao(id);
        call.enqueue(new Callback<List<GetInspecaoResponse>>() {
            @Override
            public void onResponse(Call<List<GetInspecaoResponse>> call, Response<List<GetInspecaoResponse>> response) {
                if (response.isSuccessful()) {
                    List<GetInspecaoResponse> inspecoes = response.body();
                    // Atualizar o adapter com os dados retornados
                    //editarProducaoAdapter.updateInspecoes(inspecoes);
                } else {
                    Toast.makeText(ProducaoActivity.this, "Erro ao carregar inspeções", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetInspecaoResponse>> call, Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro ao se conectar à API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarInspecao(int id) {
        apiInterface.getInspecao(id).enqueue(new Callback<List<GetInspecaoResponse>>() {
            @Override
            public void onResponse(Call<List<GetInspecaoResponse>> call, Response<List<GetInspecaoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producaoAdapter.setInspecoes(response.body());
                } else {
                    Toast.makeText(ProducaoActivity.this, "Erro ao carregar inspeção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetInspecaoResponse>> call, Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarTipoItens(){
        apiInterface.getTiposDeItens().enqueue(new Callback<List<TipoItem>>() {
            @Override
            public void onResponse(Call<List<TipoItem>> call, Response<List<TipoItem>> response) {
                if(response.isSuccessful() && response.body() != null){
                    producaoAdapter.setTiposItens(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<TipoItem>> call, Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
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
                    Toast.makeText(ProducaoActivity.this, "Inspeção atualizada com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProducaoActivity.this, "Erro ao atualizar inspeção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}