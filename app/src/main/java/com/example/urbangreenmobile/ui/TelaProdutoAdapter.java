package com.example.urbangreenmobile.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;

import java.util.ArrayList;
import java.util.List;

public class TelaProdutoAdapter extends RecyclerView.Adapter<TelaProdutoAdapter.EstoqueViewHolder> implements Filterable {
    private List<GetProdutoResponse> itemList = new ArrayList<>();
    private List<GetProdutoResponse> produtosFull;
    private OnEditClickListener onEditClickListener;

    public interface OnEditClickListener {
        void onEditClick(GetProdutoResponse produto);
    }

    public void setOnEditClickListener(TelaProdutoAdapter.OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    class EstoqueViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName, itemQuantity, itemPrice;
        public ImageButton itemEditButton;

        public EstoqueViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_nome);
            itemQuantity = itemView.findViewById(R.id.item_quantidade);
            itemPrice = itemView.findViewById(R.id.item_preco);
            itemEditButton = itemView.findViewById(R.id.item_edit_button);
        }
    }

    @Override
    public EstoqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new EstoqueViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setProdutos(List<GetProdutoResponse> produto) {
        this.itemList = produto;
        produtosFull = new ArrayList<>(produto);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(EstoqueViewHolder holder, int position) {
        GetProdutoResponse currentItem = itemList.get(position);

        holder.itemName.setText(currentItem.getNome());
        holder.itemQuantity.setText("Qtd: " + currentItem.getQuantidade());
        holder.itemPrice.setText("Preço: " + currentItem.getValor());

        Glide.with(holder.itemView.getContext())
                .load(currentItem.getImagemUrl())
                .into(holder.itemImage);

        holder.itemEditButton.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(currentItem);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return produtoFilter;
    }

    private Filter produtoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GetProdutoResponse> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(produtosFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (GetProdutoResponse item : produtosFull) {
                    if (item.getNome().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}