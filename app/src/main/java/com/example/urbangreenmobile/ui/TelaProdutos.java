package com.example.urbangreenmobile.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.ApiInterface;
import com.example.urbangreenmobile.api.ApiService;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaProdutos extends AppCompatActivity {

    private RecyclerView recyclerViewEstoque;
    private TelaProdutoAdapter itemAdapter;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_produtos);

        recyclerViewEstoque = findViewById(R.id.recyclerViewEstoque);
        recyclerViewEstoque.setLayoutManager(new GridLayoutManager(this, 2));

        itemAdapter = new TelaProdutoAdapter();
        recyclerViewEstoque.setAdapter(itemAdapter);

        itemAdapter.setOnEditClickListener(this::abrirDialogProduto);

        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
        listarItens();
        initAddButton();
        initSearchInput();
    }

    private void initAddButton() {
        ImageButton buttonAdd = findViewById(R.id.add_produto_button);
        buttonAdd.setOnClickListener(v -> abrirDialogProduto(null));
    }

    private void initSearchInput() {
        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void abrirDialogProduto(GetProdutoResponse produto) {
        Dialog dialog = new Dialog(TelaProdutos.this);
        dialog.setContentView(R.layout.editar_produto);

        EditText inputProduto = dialog.findViewById(R.id.input_produto);
        EditText inputQuantidade = dialog.findViewById(R.id.input_quantidade);
        EditText inputValor = dialog.findViewById(R.id.input_valor);
        ImageButton btnSalvar = dialog.findViewById(R.id.btn_salvar);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);

        if (produto != null) {
            preencherCamposProduto(produto, inputProduto, inputQuantidade, inputValor);
        }

        closeButton.setOnClickListener(v1 -> dialog.dismiss());
        btnSalvar.setOnClickListener(v -> salvarProduto(dialog, produto, inputProduto, inputQuantidade, inputValor));

        dialog.show();
    }

    private void preencherCamposProduto(GetProdutoResponse produto, EditText inputProduto, EditText inputQuantidade, EditText inputValor) {
        inputProduto.setText(produto.getNome());
        inputQuantidade.setText(produto.getQuantidade());
        Double valor = produto.getValor();
        inputValor.setText(valor.toString());
    }

    private void salvarProduto(Dialog dialog, GetProdutoResponse produto, EditText inputProduto, EditText inputQuantidade, EditText inputValor) {
        if (produto == null) {
            criarNovoProduto(inputProduto, inputQuantidade, inputValor);
        } else {
            atualizarProdutoExistente(produto.getId(), inputProduto.getText().toString(), Integer.parseInt(inputQuantidade.getText().toString()), Double.parseDouble(inputValor.getText().toString()));
        }

        dialog.dismiss();
    }

    private void criarNovoProduto(EditText inputProduto, EditText inputQuantidade, EditText inputValor) {
        String nome = inputProduto.getText().toString();
        int quantidade = Integer.parseInt(inputQuantidade.getText().toString());
        double valor = Double.parseDouble(inputValor.getText().toString());

        apiInterface.criarProduto(nome, quantidade, valor).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listarItens();
                    Toast.makeText(TelaProdutos.this, "Produto criado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaProdutos.this, "Erro ao criar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TelaProdutos.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarProdutoExistente(int id, String nome, int quantidade, double valor) {
        apiInterface.atualizarProduto(id, nome, quantidade, valor).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listarItens();
                    Toast.makeText(TelaProdutos.this, "Produto atualizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaProdutos.this, "Erro ao atualizar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TelaProdutos.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarItens() {
        Call<List<GetProdutoResponse>> call = apiInterface.getProdutos();
        call.enqueue(new Callback<List<GetProdutoResponse>>() {
            @Override
            public void onResponse(Call<List<GetProdutoResponse>> call, Response<List<GetProdutoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemAdapter.setProdutos(response.body());
                } else {
                    Toast.makeText(TelaProdutos.this, "Erro ao carregar itens", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetProdutoResponse>> call, Throwable t) {
                Toast.makeText(TelaProdutos.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
