package com.example.urbangreenmobile.ui.Producao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.Integrations.ApiService;
import com.example.urbangreenmobile.api.models.Producao.TipoItem;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducaoActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private EditText editTextDataFiltro;
    private RecyclerView recyclerViewEstoque;
    private ProducaoAdapter producaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_producao);

        setupRecyclerView();
        setupApiInterface();
        listarProdutos();

//        editTextDataFiltro = findViewById(R.id.editTextDataFiltro);

//        findViewById(R.id.editTextDataFiltro).setOnClickListener(v -> {
//            String dataFiltro = editTextDataFiltro.getText().toString();
//            if (!dataFiltro.isEmpty()) {
//                //filtrarInspecoesPorData(dataFiltro);
//            } else {
//                Toast.makeText(this, "Por favor, insira uma data válida", Toast.LENGTH_SHORT).show();
//            }
//        });
//        ImageButton editar = findViewById(R.id.item_edit_button);
//        editar.setOnClickListener(this::navegar);
    }
    private void navegar(View view){
        Intent intent = new Intent(ProducaoActivity.this, EditarProducaoActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        recyclerViewEstoque = findViewById(R.id.recyclerViewProduto);
        recyclerViewEstoque.setLayoutManager(new GridLayoutManager(this, 2));

        producaoAdapter = new ProducaoAdapter();
        recyclerViewEstoque.setAdapter(producaoAdapter);
//        producaoAdapter.setOnEditClickListener(this::abrirDialogProduto);
    }

    private void setupApiInterface() {
        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
    }

    private void listarProdutos() {
        apiInterface.getProdutos().enqueue(new Callback<List<GetProdutoResponse>>() {
            @Override
            public void onResponse(Call<List<GetProdutoResponse>> call, Response<List<GetProdutoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producaoAdapter.setProdutos(response.body());
                } else {
                    Toast.makeText(ProducaoActivity.this, "Erro ao carregar itens", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetProdutoResponse>> call, Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}