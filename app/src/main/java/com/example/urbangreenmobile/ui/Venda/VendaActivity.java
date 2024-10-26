package com.example.urbangreenmobile.ui.Venda;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.Integrations.ApiService;
import com.example.urbangreenmobile.api.models.Pedido.CreatePedidoRequest;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendaActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewEstoque;
    private VendaAdapter vendaAdapter;
    private CarrinhoActivity carrinhoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_vendas);

        setupRecyclerView();
        setupApiInterface();
        listarProdutos();
        initCarrinho();
    }

    private void setupRecyclerView() {
        recyclerViewEstoque = findViewById(R.id.recyclerViewProduto);
        recyclerViewEstoque.setLayoutManager  (new GridLayoutManager(this, 2));

        vendaAdapter = new VendaAdapter();
        recyclerViewEstoque.setAdapter(vendaAdapter);
    }


    private void initCarrinho(){
        ImageButton carrinho = findViewById(R.id.ic_carrinho);
        carrinho.setOnClickListener(carrinhoActivity);
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

    private void criarItemPedido(GetProdutoResponse produto, TextView quantidadeTxt){
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setProdutoId(produto.getId());
        itemPedido.setQuantidade(Integer.parseInt(quantidadeTxt.getText().toString()));
        enviarItemPedido(itemPedido);
    }

    private void enviarItemPedido(ItemPedido itemPedido){
        apiInterface.criarItemPedido(itemPedido).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VendaActivity.this, "Itens enviados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VendaActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void criarPedido(EditText nome, ItemPedido itemPedido){
        CreatePedidoRequest pedido = new CreatePedidoRequest();

        pedido.setNomeComprador(String.valueOf(nome));
    }
}
