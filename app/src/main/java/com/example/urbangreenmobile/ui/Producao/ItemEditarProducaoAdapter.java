package com.example.urbangreenmobile.ui.Producao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.ItemInspecao;

public class ItemEditarProducaoAdapter extends RecyclerView.Adapter<ItemEditarProducaoAdapter.ViewHolder>{
    private GetInspecaoResponse inspecao;
    private ConditionMetListener listener;

    public ItemEditarProducaoAdapter(ConditionMetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producao_edicao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemInspecao item = inspecao.getItens().get(position);
        holder.nomeItem.setText(item.getNome());
        holder.itemInspecaoCheckbox.setChecked(item.isRealizado());
        holder.data.setText(String.format("%s%s", holder.data.getText(), item.getData()));

        holder.itemInspecaoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setRealizado(isChecked);
                buttonView.setChecked(isChecked);

                boolean ativarCampo = false;
                if(inspecao.getItens().stream().allMatch(ItemInspecao::isRealizado)){
                    ativarCampo = true;
                }

                listener.conditionMet(ativarCampo);
            }
        });
    }

    public void setInspecao(GetInspecaoResponse inspecao){
        this.inspecao = inspecao;
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return this.inspecao.getItens().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox itemInspecaoCheckbox;
        public TextView nomeItem, data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemInspecaoCheckbox = itemView.findViewById(R.id.check_isRealizado);
            nomeItem = itemView.findViewById(R.id.textViewTipoInspecao);
            data = itemView.findViewById(R.id.textViewDataInspecao);
        }
    }

    public interface ConditionMetListener {
        void conditionMet(boolean isMet);
    }
}
