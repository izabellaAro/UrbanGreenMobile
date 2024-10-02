package com.example.urbangreenmobile.ui;

import static com.example.urbangreenmobile.ui.MultipartHelper.prepararArquivoImagem;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.ApiInterface;
import com.example.urbangreenmobile.api.ApiService;
import com.example.urbangreenmobile.api.models.Produto.CreateProdutoRequest;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;
import com.example.urbangreenmobile.api.models.Produto.UpdateProdutoRequest;
import com.example.urbangreenmobile.utils.TokenManager;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaProdutos extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerViewEstoque;
    private TelaProdutoAdapter itemAdapter;
    private ApiInterface apiInterface;
    private ImageView imagemProduto;
    private Bitmap imagemProdutoBitmap;
    private String imagemOriginalBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_produtos);

        setupRecyclerView();
        setupApiInterface();
        listarItens();
        initAddButton();
        initSearchInput();
    }

    private void setupRecyclerView() {
        recyclerViewEstoque = findViewById(R.id.recyclerViewProduto);
        recyclerViewEstoque.setLayoutManager(new GridLayoutManager(this, 2));

        itemAdapter = new TelaProdutoAdapter();
        recyclerViewEstoque.setAdapter(itemAdapter);
        itemAdapter.setOnEditClickListener(this::abrirDialogProduto);
    }

    private void setupApiInterface() {
        apiInterface = ApiService.getClient(new TokenManager(this)).create(ApiInterface.class);
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
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.editar_produto);

        EditText inputProduto = dialog.findViewById(R.id.input_produto);
        EditText inputQuantidade = dialog.findViewById(R.id.input_quantidade);
        EditText inputValor = dialog.findViewById(R.id.input_valor);
        ImageButton btnSalvar = dialog.findViewById(R.id.btn_salvar);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        Button btnSelecionarImagem = dialog.findViewById(R.id.btn_selecionar_imagem);
        imagemProduto = dialog.findViewById(R.id.imagem_produto);

        btnSelecionarImagem.setOnClickListener(v -> selecionarImagem());

        if (produto != null) {
            preencherCamposProduto(produto, inputProduto, inputQuantidade, inputValor, imagemProduto);
        }

        closeButton.setOnClickListener(v -> dialog.dismiss());

        btnSalvar.setOnClickListener(v -> salvarProduto(dialog, produto, inputProduto, inputQuantidade, inputValor, imagemProdutoBitmap));

        dialog.show();
    }

    private void selecionarImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma Imagem"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            carregarImagem(imageUri);
        }
    }

    private void carregarImagem(Uri imageUri) {
        try {
            imagemProdutoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            imagemProduto.setImageBitmap(imagemProdutoBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void preencherCamposProduto(GetProdutoResponse produto, EditText inputProduto, EditText inputQuantidade, EditText inputValor, ImageView imagemProduto) {
        inputProduto.setText(produto.getNome());
        inputQuantidade.setText(String.valueOf(produto.getQuantidade()));
        inputValor.setText(String.valueOf(produto.getValor()));
        if (produto.getImagemBase64() != null && !produto.getImagemBase64().isEmpty()) {
            imagemOriginalBase64 = produto.getImagemBase64();
            Bitmap bitmap = MultipartHelper.base64ToBitmap(produto.getImagemBase64());
            imagemProduto.setImageBitmap(bitmap);
        }
    }

    private void salvarProduto(Dialog dialog, GetProdutoResponse produto, EditText inputProduto, EditText inputQuantidade, EditText inputValor, Bitmap imagemProdutoBitmap) {
        if (produto == null) {
            criarNovoProduto(inputProduto, inputQuantidade, inputValor, imagemProdutoBitmap);
        } else if (imagemProdutoBitmap == null && imagemOriginalBase64 != null) {
            imagemProdutoBitmap = MultipartHelper.base64ToBitmap(imagemOriginalBase64);
        } else {
            atualizarProdutoExistente(produto.getId(),
                    inputProduto.getText().toString(),
                    Integer.parseInt(inputQuantidade.getText().toString()),
                    Double.parseDouble(inputValor.getText().toString()),
                    imagemProdutoBitmap);
        }
        dialog.dismiss();
    }

    private void criarNovoProduto(EditText inputProduto, EditText inputQuantidade, EditText inputValor, Bitmap imagemProdutoBitmap) {
        CreateProdutoRequest novoProduto = new CreateProdutoRequest(
                inputProduto.getText().toString(),
                Integer.parseInt(inputQuantidade.getText().toString()),
                Double.parseDouble(inputValor.getText().toString())
        );

        RequestBody nomeBody = MultipartHelper.createPartFromString(novoProduto.getNome());
        RequestBody quantidadeBody = MultipartHelper.createPartFromInt(novoProduto.getQuantidade());
        RequestBody valorBody = MultipartHelper.createPartFromDouble(novoProduto.getValor());

        MultipartBody.Part imagemProdutoPart = prepararArquivoImagem(imagemProdutoBitmap, this);

        apiInterface.criarProduto(nomeBody, quantidadeBody, valorBody, imagemProdutoPart).enqueue(new ProdutoCallback("Produto criado com sucesso"));
    }

    private void atualizarProdutoExistente(int id, String nome, int quantidade, double valor, Bitmap imagemProdutoBitmap) {
        UpdateProdutoRequest produtoAtualizado = new UpdateProdutoRequest(
                nome,
                quantidade,
                valor
        );

        RequestBody nomeBody = MultipartHelper.createPartFromString(produtoAtualizado.getNome());
        RequestBody quantidadeBody = MultipartHelper.createPartFromInt(produtoAtualizado.getQuantidade());
        RequestBody valorBody = MultipartHelper.createPartFromDouble(produtoAtualizado.getValor());

        MultipartBody.Part imagemProdutoPart = null;

        if (imagemProdutoBitmap != null) {
            imagemProdutoPart = MultipartHelper.prepararArquivoImagem(imagemProdutoBitmap, this);
        }

        if (imagemProdutoPart != null) {
            apiInterface.atualizarProduto(id, nomeBody, quantidadeBody, valorBody, imagemProdutoPart)
                    .enqueue(new ProdutoCallback("Produto atualizado com sucesso"));
        } else {
            apiInterface.atualizarProduto(id, nomeBody, quantidadeBody, valorBody)
                    .enqueue(new ProdutoCallback("Produto atualizado com sucesso"));
        }
    }

    private class ProdutoCallback implements Callback<Void> {
        private final String successMessage;

        ProdutoCallback(String successMessage) {
            this.successMessage = successMessage;
        }

        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                listarItens();
                Toast.makeText(TelaProdutos.this, successMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TelaProdutos.this, "Erro ao processar o produto", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(TelaProdutos.this, "Erro de rede", Toast.LENGTH_SHORT).show();
        }
    }

    private void listarItens() {
        apiInterface.getProdutos().enqueue(new Callback<List<GetProdutoResponse>>() {
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
