package com.example.urbangreenmobile.ui.Venda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendaAdapter extends RecyclerView.Adapter<VendaAdapter.EstoqueViewHolder> {
    private List<GetProdutoResponse> produtos = new ArrayList<>();
    private List<ItemPedido> itensPedido = new ArrayList<>();

    public VendaAdapter(LifecycleOwner lifecycleOwner, VendaSharedViewModel sharedViewModel) {
        sharedViewModel.getItems().observe(lifecycleOwner, this::setItensPedido);
    }

    private void alterarQuantidadeItem(EstoqueViewHolder holder, GetProdutoResponse produto,
                                       boolean adicionar){
        Optional<ItemPedido> itemOptional = itensPedido.stream()
                .filter(x -> x.getProdutoId() == produto.getId()).findFirst();

        ItemPedido itemPedido;

        if (!itemOptional.isPresent()){
            itemPedido = new ItemPedido();
            itemPedido.setProdutoId(produto.getId());
            itemPedido.setNomeProduto(produto.getNome());
            itensPedido.add(itemPedido);
        } else {
            itemPedido = itemOptional.get();
        }

        int quantidade = itemPedido.getQuantidade();

        if (adicionar){
            quantidade++;
        } else {
            quantidade--;
        }

        itemPedido.setQuantidade(quantidade);
        holder.quantidade.setText(String.valueOf(itemPedido.getQuantidade()));
    }

    private void configurarListeners(EstoqueViewHolder holder, GetProdutoResponse produto){
        holder.btnAdicionar
                .setOnClickListener(v -> alterarQuantidadeItem(holder, produto, true));

        holder.btnDiminuir
                .setOnClickListener(v -> alterarQuantidadeItem(holder, produto, false));
    }

    private void preencherItens(EstoqueViewHolder holder, GetProdutoResponse produto){
        holder.itemName.setText(produto.getNome());
        byte[] imageBytes = Base64.decode(produto.getImagemBase64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.itemImage.setImageBitmap(bitmap);
        holder.quantidade.setText(String.valueOf(0));
    }

    @Override
    public EstoqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_venda, parent, false);
        return new EstoqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EstoqueViewHolder holder, int position) {
        GetProdutoResponse produto = produtos.get(position);

        if (produto == null) return;

        preencherItens(holder, produto);
        configurarListeners(holder, produto);
    }

    public void setProdutos(List<GetProdutoResponse> produtos) {
        this.produtos = produtos;
        notifyDataSetChanged();
    }

    public void setItensPedido(List<ItemPedido> itens) {
        this.itensPedido = itens;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class EstoqueViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName, quantidade;
        public Button btnDiminuir, btnAdicionar;

        public EstoqueViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_nome);
            quantidade = itemView.findViewById(R.id.quantidade);
            btnDiminuir = itemView.findViewById(R.id.btn_diminuir);
            btnAdicionar = itemView.findViewById(R.id.btn_adicionar);
        }
    }
}