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
    private CarrinhoAdapter carrinhoAdapter = new CarrinhoAdapter(this, getItensCarrinho());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrinho_venda);

        setupRecyclerView();
    }

    protected void onResume(){
        super.onResume();
        carrinhoAdapter.atualizar(getItensCarrinho());
    }

    private void setupRecyclerView() {
        recyclerItens = findViewById(R.id.recycler_itens_carrinho);
        recyclerItens.setLayoutManager(new LinearLayoutManager(this));

        carrinhoAdapter = new CarrinhoAdapter();
        recyclerItens.setAdapter(carrinhoAdapter);
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
    
}

