package com.example.urbangreenmobile.ui.Producao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;

import java.util.ArrayList;
import java.util.List;

public class ProducaoAdapter extends RecyclerView.Adapter<ProducaoAdapter.EstoqueViewHolder> {
    private List<GetProdutoResponse> produtos = new ArrayList<>();
    private OnItemButtonClickListener onEditClickListener;

    public ProducaoAdapter(OnItemButtonClickListener listener){
        onEditClickListener = listener;
    }

    @Override
    public EstoqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producao, parent, false);
        return new EstoqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EstoqueViewHolder holder, int position) {
        GetProdutoResponse currentItem = produtos.get(position);

        holder.itemName.setText(currentItem.getNome());

        byte[] imageBytes = Base64.decode(currentItem.getImagemBase64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.itemImage.setImageBitmap(bitmap);

         holder.itemEditButton.setOnClickListener(v -> {
             if (onEditClickListener != null) {
                 onEditClickListener.onItemButtonClick(currentItem.getId());
             }
         });
    }

    public void setProdutos(List<GetProdutoResponse> produtos) {
        this.produtos = produtos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class EstoqueViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName;
        public ImageButton itemEditButton;

        public EstoqueViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_nome);
            itemEditButton = itemView.findViewById(R.id.edit_button);
        }
    }
}