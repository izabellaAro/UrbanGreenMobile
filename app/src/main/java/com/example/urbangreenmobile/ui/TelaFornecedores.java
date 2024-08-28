package com.example.urbangreenmobile.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.ApiInterface;
import com.example.urbangreenmobile.api.ApiService;
import com.example.urbangreenmobile.api.models.Fornecedor.CreateFornecedorRequest;
import com.example.urbangreenmobile.api.models.Fornecedor.GetFornecedorResponse;
import com.example.urbangreenmobile.api.models.Fornecedor.PessoaJuridica;
import com.example.urbangreenmobile.api.models.Insumo.GetInsumoResponse;
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
    private List<GetFornecedorResponse> fornecedorList;

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
            abrirDialogFornecedor(null);
        });
    }

    private void abrirDialogFornecedor(GetFornecedorResponse fornecedor) {
        Dialog dialog = new Dialog(TelaFornecedores.this);
        dialog.setContentView(R.layout.editar_fornecedor);

        EditText inputEmpresa = dialog.findViewById(R.id.input_rzSocial);
        EditText inputCnpj = dialog.findViewById(R.id.input_cnpj);
        EditText inputNomeFant = dialog.findViewById(R.id.input_nomeFantasia);
        EditText inputResponsavel = dialog.findViewById(R.id.input_responsavel);
        EditText inputTelefone = dialog.findViewById(R.id.input_telefone);
        EditText inputEmail = dialog.findViewById(R.id.input_email);
        Spinner spinnerInsumos = dialog.findViewById(R.id.spinner_insumos);

        ImageButton btnSalvar = dialog.findViewById(R.id.btn_salvar);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);

        if (fornecedor != null) {
            inputEmpresa.setText(fornecedor.getNomePJ());
            inputResponsavel.setText(fornecedor.getNome());
            inputTelefone.setText(fornecedor.getTelefone());
            inputEmail.setText(fornecedor.getEmail());
        }

        carregarInsumos(spinnerInsumos);

        closeButton.setOnClickListener(v1 -> dialog.dismiss());

        btnSalvar.setOnClickListener(v -> {
            PessoaJuridica pessoaJuridica = new PessoaJuridica(
                    inputNomeFant.getText().toString(),
                    inputCnpj.getText().toString(),
                    inputEmpresa.getText().toString(),
                    inputEmail.getText().toString(),
                    inputTelefone.getText().toString()
            );

            GetInsumoResponse selectedInsumo = (GetInsumoResponse) spinnerInsumos.getSelectedItem();
            int insumoId = selectedInsumo.getId();

            CreateFornecedorRequest novoFornecedor = new CreateFornecedorRequest();
            novoFornecedor.setNome(inputResponsavel.getText().toString());
            novoFornecedor.setInsumoId(insumoId);
            novoFornecedor.setPessoaJuridica(pessoaJuridica);

            criarFornecedor(novoFornecedor);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void criarFornecedor(CreateFornecedorRequest fornecedor) {
        Call<Void> call = apiInterface.criarFornecedor(fornecedor);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listarFornecedores();
                    Toast.makeText(TelaFornecedores.this, "Fornecedor criado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaFornecedores.this, "Erro ao criar fornecedor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TelaFornecedores.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarFornecedores() {
        Call<List<GetFornecedorResponse>> call = apiInterface.getFornecedores();
        call.enqueue(new Callback<List<GetFornecedorResponse>>() {
            @Override
            public void onResponse(Call<List<GetFornecedorResponse>> call, Response<List<GetFornecedorResponse>> response) {
                if (response.isSuccessful()) {
                    fornecedorList = response.body();
                    fornecedorAdapter.setFornecedores(fornecedorList);
                } else {
                    Toast.makeText(TelaFornecedores.this, "Erro ao carregar fornecedores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetFornecedorResponse>> call, Throwable t) {
                Toast.makeText(TelaFornecedores.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarInsumos(Spinner spinnerInsumos) {
        Call<List<GetInsumoResponse>> call = apiInterface.getInsumos();
        call.enqueue(new Callback<List<GetInsumoResponse>>() {
            @Override
            public void onResponse(Call<List<GetInsumoResponse>> call, Response<List<GetInsumoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetInsumoResponse> insumos = response.body();
                    ArrayAdapter<GetInsumoResponse> adapter = new ArrayAdapter<>(TelaFornecedores.this,
                            android.R.layout.simple_spinner_item, insumos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerInsumos.setAdapter(adapter);
                } else {
                    Toast.makeText(TelaFornecedores.this, "Erro ao carregar insumos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetInsumoResponse>> call, Throwable t) {
                Toast.makeText(TelaFornecedores.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
