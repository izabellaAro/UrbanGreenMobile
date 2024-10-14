package com.example.urbangreenmobile.ui.Producao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.ItemInspecao;
import com.example.urbangreenmobile.api.models.Producao.TipoItem;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;
import com.example.urbangreenmobile.ui.Produto.ProdutoAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProducaoAdapter extends RecyclerView.Adapter<ProducaoAdapter.EstoqueViewHolder> {
    private List<GetProdutoResponse> produtos = new ArrayList<>();
    private List<GetInspecaoResponse> inspecoes = new ArrayList<>();
    private List<ItemInspecao> itemInspecao = new ArrayList<>();
    private ProducaoAdapter.OnEditClickListener onEditClickListener;

    public interface OnEditClickListener {
        void onEditClick(List<GetInspecaoResponse> inspecoes);
    }

    public ProducaoAdapter() { }

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
    public void onBindViewHolder(EstoqueViewHolder holder, int position) {
        GetProdutoResponse currentItem = produtos.get(position);

        holder.itemName.setText(currentItem.getNome());
        holder.itemQuantity.setText("Qtd: " + currentItem.getQuantidade());
        holder.itemPrice.setText("Preço: " + currentItem.getValor());

        byte[] imageBytes = Base64.decode(currentItem.getImagemBase64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.itemImage.setImageBitmap(bitmap);

//         holder.itemEditButton.setOnClickListener(v -> {
//             if (onEditClickListener != null) {
// //                onEditClickListener.onEditClick(currentItem);
//             }
//         });
    }

    public void setOnEditClickListener(ProducaoAdapter.OnEditClickListener onEditClickListener) {
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