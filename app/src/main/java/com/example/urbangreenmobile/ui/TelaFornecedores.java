package com.example.urbangreenmobile.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.ApiInterface;
import com.example.urbangreenmobile.api.ApiService;
import com.example.urbangreenmobile.api.models.Fornecedor;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaFornecedores extends AppCompatActivity {

    private ApiInterface apiInterface;
    private TokenManager tokenManager;
    private RecyclerView recyclerViewFornecedores;
    private TelaFornecedorAdapter fornecedorAdapter;
    private List<Fornecedor> fornecedorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_fornecedores);

        recyclerViewFornecedores = findViewById(R.id.recyclerViewFornecedores);
        recyclerViewFornecedores.setLayoutManager(new LinearLayoutManager(this));

        tokenManager = new TokenManager(this);
        apiInterface = ApiService.getClient(tokenManager).create(ApiInterface.class);

        fornecedorAdapter = new TelaFornecedorAdapter();
        recyclerViewFornecedores.setAdapter(fornecedorAdapter);

        listarFornecedores();

        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fornecedorAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton buttonAdd = findViewById(R.id.add_fornecedor_button);
        buttonAdd.setOnClickListener(v -> {
            Dialog dialog = new Dialog(TelaFornecedores.this);
            dialog.setContentView(R.layout.editar_fornecedor);

            ImageButton closeButton = dialog.findViewById(R.id.close_button);
            closeButton.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });
    }

    private void listarFornecedores() {
        Call<List<Fornecedor>> call = apiInterface.getFornecedores();
        call.enqueue(new Callback<List<Fornecedor>>() {
            @Override
            public void onResponse(Call<List<Fornecedor>> call, Response<List<Fornecedor>> response) {
                if (response.isSuccessful()) {
                    fornecedorList = response.body();
                    fornecedorAdapter.setFornecedores(fornecedorList);
                } else {
                    Toast.makeText(TelaFornecedores.this, "Erro ao carregar fornecedores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Fornecedor>> call, Throwable t) {
                Toast.makeText(TelaFornecedores.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Implementar métodos para criar, atualizar e deletar fornecedores
}
