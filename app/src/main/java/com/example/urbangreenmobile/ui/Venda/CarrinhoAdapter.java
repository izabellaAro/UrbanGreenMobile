package com.example.urbangreenmobile.ui.Venda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private final OnProductUpdateListener listener;

    public CarrinhoAdapter(OnProductUpdateListener listener) { this.listener = listener; }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrinho, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        ItemPedido item = itensCarrinho.get(position);
        holder.itemNome.setText(holder.itemNome.getText() + item.getNomeProduto());
        holder.quantidade.setText(holder.quantidade.getText() + String.valueOf(item.getQuantidade()));
        listener.OnProductUpdateListener(itensCarrinho);
    }

    public void setItensCarrinho(List<ItemPedido> itensCarrinho){
        this.itensCarrinho = itensCarrinho;
    }

    @Override
    public int getItemCount() {
        return itensCarrinho.size();
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        TextView quantidade, itemNome;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            quantidade = itemView.findViewById(R.id.quantidade);
            itemNome = itemView.findViewById(R.id.nome_item);
        }
    }

    public interface OnProductUpdateListener {
        void OnProductUpdateListener(List<ItemPedido> pedidos);
    }
}

