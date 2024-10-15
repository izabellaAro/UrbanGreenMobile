package com.example.urbangreenmobile.ui.Venda;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.Integrations.ApiService;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendaActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewEstoque;
    private VendaAdapter vendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_vendas);

        setupRecyclerView();
        setupApiInterface();
        listarProdutos();
    }

    private void setupRecyclerView() {
        recyclerViewEstoque = findViewById(R.id.recyclerViewProduto);
        recyclerViewEstoque.setLayoutManager(new GridLayoutManager(this, 2));

        vendaAdapter = new VendaAdapter();
        recyclerViewEstoque.setAdapter(vendaAdapter);
    }

    private void setupApiInterface() {
        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
    }

    private void listarProdutos() {
        apiInterface.getProdutos().enqueue(new Callback<List<GetProdutoResponse>>() {
            @Override
            public void onResponse(Call<List<GetProdutoResponse>> call, Response<List<GetProdutoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    vendaAdapter.setProdutos(response.body());
                } else {
                    Toast.makeText(VendaActivity.this, "Erro ao carregar itens", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetProdutoResponse>> call, Throwable t) {
                Toast.makeText(VendaActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
