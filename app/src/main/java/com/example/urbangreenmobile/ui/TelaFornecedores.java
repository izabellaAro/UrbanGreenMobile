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
import com.example.urbangreenmobile.api.models.Fornecedor.UpdateFornecedorRequest;
import com.example.urbangreenmobile.api.models.Insumo.GetInsumoResponse;
import com.example.urbangreenmobile.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaFornecedores extends AppCompatActivity {

    private ApiInterface apiInterface;
    private TokenManager tokenManager;
    private TelaFornecedorAdapter fornecedorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_fornecedores);

        initApiService();
        initRecyclerView();
        initSearchInput();
        initAddButton();

        listarFornecedores();
    }

    private void initApiService() {
        tokenManager = new TokenManager(this);
        apiInterface = ApiService.getClient(tokenManager).create(ApiInterface.class);
    }

    private void initRecyclerView() {
        RecyclerView recyclerViewFornecedores = findViewById(R.id.recyclerViewFornecedores);
        recyclerViewFornecedores.setLayoutManager(new LinearLayoutManager(this));

        fornecedorAdapter = new TelaFornecedorAdapter();
        recyclerViewFornecedores.setAdapter(fornecedorAdapter);

        fornecedorAdapter.setOnEditClickListener(this::abrirDialogFornecedor);
    }

    private void initSearchInput() {
        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fornecedorAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initAddButton() {
        ImageButton buttonAdd = findViewById(R.id.add_fornecedor_button);
        buttonAdd.setOnClickListener(v -> abrirDialogFornecedor(null));
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
            preencherCamposFornecedor(fornecedor, inputEmpresa, inputCnpj, inputNomeFant, inputTelefone, inputEmail, inputResponsavel, spinnerInsumos);
        } else {
            carregarInsumos(spinnerInsumos, -1);
        }

        closeButton.setOnClickListener(v1 -> dialog.dismiss());
        btnSalvar.setOnClickListener(v -> salvarFornecedor(dialog, fornecedor, inputNomeFant, inputCnpj, inputEmpresa, inputResponsavel, inputEmail, inputTelefone, spinnerInsumos));

        dialog.show();
    }

    private void preencherCamposFornecedor(GetFornecedorResponse fornecedor, EditText inputEmpresa, EditText inputCnpj, EditText inputNomeFant, EditText inputTelefone, EditText inputEmail, EditText inputResponsavel, Spinner spinnerInsumos) {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        inputEmpresa.setText(fornecedor.getNomePJ());
        inputEmpresa.setEnabled(false);

        inputCnpj.setText(pessoaJuridica.getCnpj());
        inputCnpj.setEnabled(false);

        inputNomeFant.setText(pessoaJuridica.getNomeFantasia());
        inputNomeFant.setEnabled(false);

        inputTelefone.setText(fornecedor.getTelefone());
        inputEmail.setText(fornecedor.getEmail());
        inputResponsavel.setText(fornecedor.getNome());

        carregarInsumos(spinnerInsumos, 1);
        spinnerInsumos.setEnabled(false);
    }

    private void salvarFornecedor(Dialog dialog, GetFornecedorResponse fornecedor, EditText inputNomeFant, EditText inputCnpj, EditText inputEmpresa, EditText inputResponsavel, EditText inputEmail, EditText inputTelefone, Spinner spinnerInsumos) {
        if (fornecedor == null) {
            criarNovoFornecedor(inputNomeFant, inputCnpj, inputEmpresa, inputResponsavel, inputEmail, inputTelefone, spinnerInsumos);
        } else {
            atualizarFornecedorExistente(fornecedor.getId(), inputResponsavel.getText().toString(), inputEmail.getText().toString(), inputTelefone.getText().toString());
        }

        dialog.dismiss();
    }

    private void criarNovoFornecedor(EditText inputNomeFant, EditText inputCnpj, EditText inputEmpresa, EditText inputResponsavel, EditText inputEmail, EditText inputTelefone, Spinner spinnerInsumos) {
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
    }

    private void atualizarFornecedorExistente(int id, String nome, String email, String telefone) {
        UpdateFornecedorRequest fornecedorAtualizado = new UpdateFornecedorRequest();
        fornecedorAtualizado.setNome(nome);
        fornecedorAtualizado.setEmail(email);
        fornecedorAtualizado.setTelefone(telefone);
        atualizarFornecedor(id, fornecedorAtualizado);
    }

    private void criarFornecedor(CreateFornecedorRequest fornecedor) {
        apiInterface.criarFornecedor(fornecedor).enqueue(new Callback<Void>() {
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

    private void atualizarFornecedor(int id, UpdateFornecedorRequest fornecedor) {
        apiInterface.atualizarFornecedor(id, fornecedor).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listarFornecedores();
                    Toast.makeText(TelaFornecedores.this, "Fornecedor atualizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaFornecedores.this, "Erro ao atualizar fornecedor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TelaFornecedores.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarFornecedores() {
        apiInterface.getFornecedores().enqueue(new Callback<List<GetFornecedorResponse>>() {
            @Override
            public void onResponse(Call<List<GetFornecedorResponse>> call, Response<List<GetFornecedorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fornecedorAdapter.setFornecedores(response.body());
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

    private void carregarInsumos(Spinner spinnerInsumos, int selectedInsumoId) {
        apiInterface.getInsumos().enqueue(new Callback<List<GetInsumoResponse>>() {
            @Override
            public void onResponse(Call<List<GetInsumoResponse>> call, Response<List<GetInsumoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayAdapter<GetInsumoResponse> adapter = new ArrayAdapter<>(TelaFornecedores.this, android.R.layout.simple_spinner_item, response.body());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerInsumos.setAdapter(adapter);
                    if (selectedInsumoId != -1) {
                        for (int i = 0; i < adapter.getCount(); i++) {
                            if (adapter.getItem(i).getId() == selectedInsumoId) {
                                spinnerInsumos.setSelection(i);
                                break;
                            }
                        }
                    }
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
