package com.example.urbangreenmobile.ui.Venda;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.Integrations.ApiService;
import com.example.urbangreenmobile.api.models.Pedido.CreatePedidoRequest;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarrinhoActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerItens;
    private CarrinhoAdapter carrinhoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrinho_venda);

        setupRecyclerView();
        setupApiInterface();
    }

    private void setupRecyclerView() {
        recyclerItens = findViewById(R.id.recycler_itens_carrinho);
        recyclerItens.setLayoutManager(new LinearLayoutManager(this));

        carrinhoAdapter = new CarrinhoAdapter();
        recyclerItens.setAdapter(carrinhoAdapter);
    }

    private void setupApiInterface() {
        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
    }

    private List<ItemPedido> itensCarrinho = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        itensCarrinho.add(item);
    }

    public List<ItemPedido> getItensCarrinho() {
        return itensCarrinho;
    }

    public int calcularTotal() {
        int total = 0;
        for (ItemPedido item : itensCarrinho) {
            total += item.getQuantidade();
        }
        return total;
    }


    private void enviarItens(ItemPedido itemPedido) {
        String nomeComprador = ((EditText) findViewById(R.id.nome_comprador)).getText().toString();
        List<Integer> itensPedidoIds = new ArrayList<>();
        for (ItemPedido item : getItensCarrinho()) {
            apiInterface.criarItemPedido(item).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        //itensPedidoIds.add(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CarrinhoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void criarPedido(CreatePedidoRequest pedido) {
        apiInterface.criarPedido(pedido).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CarrinhoActivity.this, "Pedido criado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CarrinhoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

