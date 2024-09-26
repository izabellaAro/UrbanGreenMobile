package com.example.urbangreenmobile.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Insumo.GetInsumoResponse;

import java.util.ArrayList;
import java.util.List;

public class TelaInsumosAdapter extends RecyclerView.Adapter<TelaInsumosAdapter.InsumoViewHolder> implements Filterable {
    private List<GetInsumoResponse> itemList = new ArrayList<>();
    private List<GetInsumoResponse> insumosFull;
    private TelaInsumosAdapter.OnEditClickListener onEditClickListener;

    public interface OnEditClickListener {
        void onEditClick(GetInsumoResponse insumo);
    }

    public void setOnEditClickListener(TelaInsumosAdapter.OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    class InsumoViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemQuantity, itemPrice;
        public ImageButton itemEditButton;

        InsumoViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_nome);
            itemQuantity = itemView.findViewById(R.id.item_quantidade);
            itemPrice = itemView.findViewById(R.id.item_preco);
            itemEditButton = itemView.findViewById(R.id.item_edit_button);
        }
    }

    @Override
    public InsumoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumo, parent, false);
        return new InsumoViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setProdutos(List<GetInsumoResponse> insumo) {
        this.itemList = insumo;
        insumosFull = new ArrayList<>(insumo);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TelaInsumosAdapter.InsumoViewHolder holder, int position) {
        GetInsumoResponse currentItem = itemList.get(position);

        holder.itemName.setText(currentItem.getNome());
        holder.itemQuantity.setText("Qtd: " + currentItem.getQuantidade());
        double preco = currentItem.getValor();
        holder.itemPrice.setText(String.format("R$ %.2f", preco));

        holder.itemEditButton.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(currentItem);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return insumoFilter;
    }

    private Filter insumoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GetInsumoResponse> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(insumosFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (GetInsumoResponse item : insumosFull) {
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