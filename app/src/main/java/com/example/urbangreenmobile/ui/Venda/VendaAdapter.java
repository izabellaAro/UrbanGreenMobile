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

import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;

import java.util.ArrayList;
import java.util.List;

public class VendaAdapter extends RecyclerView.Adapter<VendaAdapter.EstoqueViewHolder> {
    private List<GetProdutoResponse> produtos = new ArrayList<>();
    private List<ItemPedido> itens;
    private VendaAdapter.OnEditClickListener onEditClickListener;

    public interface OnEditClickListener {
        //void onEditClick(List<GetProdutoResponse> produtos);
    }

    public VendaAdapter(List<ItemPedido> itens){
        this.itens = itens;
    }

    public VendaAdapter() { }

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

    @Override
    public EstoqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venda, parent, false);
        return new EstoqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EstoqueViewHolder holder, int position) {
        GetProdutoResponse currentItem = produtos.get(position);
        //ItemPedido item = itens.get(position);
        final int[] count = {0};

        holder.itemName.setText(currentItem.getNome());

        byte[] imageBytes = Base64.decode(currentItem.getImagemBase64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.itemImage.setImageBitmap(bitmap);

        holder.btnAdicionar.setOnClickListener(v -> {
            count[0]++;
            holder.quantidade.setText(String.valueOf(count[0]));
        });

        holder.btnDiminuir.setOnClickListener(v -> {
            count[0]--;
            holder.quantidade.setText(String.valueOf(count[0]));
        });
    }

    public void setOnEditClickListener(VendaAdapter.OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public void setProdutos(List<GetProdutoResponse> produtos) {
        this.produtos = produtos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}