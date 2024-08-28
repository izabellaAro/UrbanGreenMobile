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
import com.example.urbangreenmobile.api.models.Fornecedor.GetFornecedorResponse;

import java.util.ArrayList;
import java.util.List;

public class TelaFornecedorAdapter extends RecyclerView.Adapter<TelaFornecedorAdapter.FornecedorViewHolder> implements Filterable {

    private List<GetFornecedorResponse> fornecedores = new ArrayList<>();
    private List<GetFornecedorResponse> fornecedoresFull;

    @Override
    public FornecedorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fornecedor, parent, false);
        return new FornecedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FornecedorViewHolder holder, int position) {
        GetFornecedorResponse fornecedor = fornecedores.get(position);
        holder.nomePJ.setText(fornecedor.getNomePJ());
        holder.nomeFornecedor.setText(fornecedor.getNome());
        holder.email.setText(fornecedor.getEmail());
        holder.telefone.setText(fornecedor.getTelefone());
        holder.insumoName.setText(fornecedor.getInsumo());
        double preco = fornecedor.getValor();
        holder.preco.setText(String.format("R$ %.2f", preco));


    }

    @Override
    public int getItemCount() {
        return fornecedores.size();
    }

    public void setFornecedores(List<GetFornecedorResponse> fornecedores) {
        this.fornecedores = fornecedores;
        fornecedoresFull = new ArrayList<>(fornecedores);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return fornecedorFilter;
    }

    private Filter fornecedorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GetFornecedorResponse> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fornecedoresFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (GetFornecedorResponse item : fornecedoresFull) {
                    if (item.getNomePJ().toLowerCase().contains(filterPattern) ||
                            item.getInsumo().toLowerCase().contains(filterPattern) ||
                            item.getNome().toLowerCase().contains(filterPattern)) {
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
            fornecedores.clear();
            fornecedores.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class FornecedorViewHolder extends RecyclerView.ViewHolder {
        TextView nomePJ;
        TextView nomeFornecedor;
        TextView email;
        TextView telefone;
        TextView insumoName;
        TextView preco;
        ImageButton editButton;

        FornecedorViewHolder(View itemView) {
            super(itemView);
            nomePJ = itemView.findViewById(R.id.nomePJ);
            nomeFornecedor = itemView.findViewById(R.id.nome);
            email = itemView.findViewById(R.id.email);
            telefone = itemView.findViewById(R.id.telefone);
            insumoName = itemView.findViewById(R.id.insumo);
            preco = itemView.findViewById(R.id.preco);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }
}