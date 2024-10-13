package com.example.urbangreenmobile.ui.Producao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.ItemInspecao;
import com.example.urbangreenmobile.api.models.Producao.TipoItem;

import java.util.ArrayList;
import java.util.List;

public class ProducaoAdapter extends RecyclerView.Adapter<ProducaoAdapter.ViewHolder> {

    private List<TipoItem> tiposItens;
    private List<GetInspecaoResponse> inspecoes = new ArrayList<>();
    private ProducaoAdapter.OnEditClickListener onEditClickListener;
    private List<ItemInspecao> itemInspecao = new ArrayList<>();

    public interface OnEditClickListener {
        void onEditClick(List<GetInspecaoResponse> inspecoes);
    }

    public ProducaoAdapter(List<TipoItem> tiposItens) {
        this.tiposItens = tiposItens;
    }

    @NonNull
    @Override
    public ProducaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producao_edicao,
                parent, false);
        return new ViewHolder(view);
    }

    public void setOnEditClickListener(ProducaoAdapter.OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ProducaoAdapter.ViewHolder holder, int position) {
        ItemInspecao inspecao = itemInspecao.get(position);
        GetInspecaoResponse response = inspecoes.get(position);
        TipoItem item = tiposItens.get(position);
        holder.nomeItem.setText(item.getNome());
        //holder.nomeItem.setText(inspecao.getNome());
        //holder.itemInspecaoCheckbox.setChecked(false);
        //holder.data.setText(inspecao.getData());
        //holder.registro.setText(response.getRegistro());

        holder.itemInspecaoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                tipoItem.(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inspecoes.size();
    }

    public void setInspecoes(List<GetInspecaoResponse> inspecaoResponse) {
        this.inspecoes = inspecaoResponse;
        notifyDataSetChanged();
    }

    public void setTiposItens(List<TipoItem> listItens){
        this.tiposItens = listItens;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox itemInspecaoCheckbox;
        public TextView nomeItem, data;
        public EditText registro;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemInspecaoCheckbox = itemView.findViewById(R.id.check_isRealizado);
            nomeItem = itemView.findViewById(R.id.textViewTipoInspecao);
            data = itemView.findViewById(R.id.textViewDataInspecao);
            registro = itemView.findViewById(R.id.input_registro);
        }
    }
}