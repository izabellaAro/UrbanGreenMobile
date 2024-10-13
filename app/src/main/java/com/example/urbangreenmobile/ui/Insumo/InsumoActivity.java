package com.example.urbangreenmobile.ui.Insumo;

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
import com.example.urbangreenmobile.api.models.Insumo.CreateInsumoRequest;
import com.example.urbangreenmobile.api.models.Insumo.GetInsumoResponse;
import com.example.urbangreenmobile.api.models.Insumo.UpdateInsumoRequest;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsumoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInsumo;
    private InsumoAdapter insumosAdapter;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_insumos);

        recyclerViewInsumo = findViewById(R.id.recyclerViewInsumo);
        recyclerViewInsumo.setLayoutManager(new GridLayoutManager(this, 2));

        insumosAdapter = new InsumoAdapter();
        recyclerViewInsumo.setAdapter(insumosAdapter);

        insumosAdapter.setOnEditClickListener(this::abrirDialogInsumo);

        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
        listarItens();
        initAddButton();
        initSearchInput();
    }

    private void initAddButton() {
        ImageButton buttonAdd = findViewById(R.id.add_insumo_button);
        buttonAdd.setOnClickListener(v -> abrirDialogInsumo(null));
    }

    private void initSearchInput() {
        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insumosAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void abrirDialogInsumo(GetInsumoResponse insumo) {
        Dialog dialog = new Dialog(InsumoActivity.this);
        dialog.setContentView(R.layout.editar_insumo);

        EditText inputInsumo = dialog.findViewById(R.id.input_insumo);
        EditText inputQuantidade = dialog.findViewById(R.id.input_quantidade);
        EditText inputValor = dialog.findViewById(R.id.input_valor);
        ImageButton btnSalvar = dialog.findViewById(R.id.btn_salvar);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);

        if (insumo != null) {
            preencherCamposInsumo(insumo, inputInsumo, inputQuantidade, inputValor);
        }

        closeButton.setOnClickListener(v1 -> dialog.dismiss());
        btnSalvar.setOnClickListener(v -> salvarInsumo(dialog, insumo, inputInsumo, inputQuantidade, inputValor));

        dialog.show();
    }

    private void preencherCamposInsumo(GetInsumoResponse insumo, EditText inputInsumo, EditText inputQuantidade, EditText inputValor) {
        inputInsumo.setText(insumo.getNome());
        inputQuantidade.setText(String.valueOf(insumo.getQuantidade()));
        inputValor.setText(String.valueOf(insumo.getValor()));
    }

    private void salvarInsumo(Dialog dialog, GetInsumoResponse insumo, EditText inputInsumo, EditText inputQuantidade, EditText inputValor) {
        if (insumo == null) {
            criarNovoInsumo(inputInsumo, inputQuantidade, inputValor);
        } else {
            atualizarInsumoExistente(insumo.getId(), inputInsumo.getText().toString(), Integer.parseInt(inputQuantidade.getText().toString()), Double.parseDouble(inputValor.getText().toString()));
        }

        dialog.dismiss();
    }

    private void criarNovoInsumo(EditText inputInsumo, EditText inputQuantidade, EditText inputValor) {
        CreateInsumoRequest insumo = new CreateInsumoRequest();
        insumo.setNome(inputInsumo.getText().toString());
        insumo.setQuantidade(Integer.parseInt(inputQuantidade.getText().toString()));
        insumo.setValor(Double.parseDouble(inputValor.getText().toString()));

        criarInsumo(insumo);
    }

    private void criarInsumo(CreateInsumoRequest insumo){
        apiInterface.criarInsumo(insumo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listarItens();
                    Toast.makeText(InsumoActivity.this, "Insumo criado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InsumoActivity.this, "Insumo ao criar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(InsumoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarInsumoExistente(int id, String insumo, int quantidade, double valor) {
        UpdateInsumoRequest insumoAtualizado = new UpdateInsumoRequest();
        insumoAtualizado.setNome(insumo);
        insumoAtualizado.setQuantidade(quantidade);
        insumoAtualizado.setValor(valor);

        atualizarInsumo(id, insumoAtualizado);
    }

    private void atualizarInsumo(int id, UpdateInsumoRequest insumo) {
        apiInterface.atualizarInsumo(id, insumo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listarItens();
                    Toast.makeText(InsumoActivity.this, "Insumo atualizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InsumoActivity.this, "Insumo ao atualizar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(InsumoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarItens() {
        Call<List<GetInsumoResponse>> call = apiInterface.getInsumos();
        call.enqueue(new Callback<List<GetInsumoResponse>>() {
            @Override
            public void onResponse(Call<List<GetInsumoResponse>> call, Response<List<GetInsumoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    insumosAdapter.setProdutos(response.body());
                } else {
                    Toast.makeText(InsumoActivity.this, "Erro ao carregar itens", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetInsumoResponse>> call, Throwable t) {
                Toast.makeText(InsumoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}