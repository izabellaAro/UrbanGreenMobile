package com.example.urbangreenmobile.ui.Venda;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.Integrations.ApiInterface;
import com.example.urbangreenmobile.api.Integrations.ApiService;
import com.example.urbangreenmobile.api.models.Pedido.CreatePedidoRequest;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedidoRequest;
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
    private VendaSharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_vendas);

        sharedViewModel = new ViewModelProvider(this).get(VendaSharedViewModel.class);
        sharedViewModel.setItems(new ArrayList<>());

        configurarBotaoCarrinho();
        configurarBotaoFinalizarPedido();
        setupApiInterface();
        vendaAdapter = new VendaAdapter(this, sharedViewModel);
        listarProdutos();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerViewEstoque = findViewById(R.id.recyclerViewProduto);
        recyclerViewEstoque.setLayoutManager(new GridLayoutManager(this, 2));
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

    private void configurarBotaoCarrinho(){
        ImageButton button = findViewById(R.id.ic_carrinho);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarrinhoFragment dialog = new CarrinhoFragment();
                dialog.show(getSupportFragmentManager(), "Carrinho");
            }
        });
    }

    private void configurarBotaoFinalizarPedido(){
        ImageButton button = findViewById(R.id.btn_finalizarPed);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarPedido();
            }
        });
    }

    private void criarPedido(){
        EditText compradorInput = findViewById(R.id.nome_comprador);

        if (compradorInput.getText().toString().equals("")){
            Toast.makeText(this, "É necessário preencher o nome do comprador", Toast.LENGTH_SHORT).show();
            return;
        }

        CreatePedidoRequest pedidoRequest = new CreatePedidoRequest();
        pedidoRequest.setNomeComprador(compradorInput.getText().toString());

        List<ItemPedido> itensCarrinho = sharedViewModel.getItems().getValue();

        List<ItemPedidoRequest> itensPedido = new ArrayList<>();

        itensCarrinho.forEach(item
                -> itensPedido.add(new ItemPedidoRequest(item.getQuantidade(), item.getProdutoId())));

        pedidoRequest.setItens(itensPedido);
    }
}
