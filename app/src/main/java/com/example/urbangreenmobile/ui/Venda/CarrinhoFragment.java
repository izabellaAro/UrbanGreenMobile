package com.example.urbangreenmobile.ui.Venda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;

import java.util.List;

public class CarrinhoFragment extends DialogFragment implements CarrinhoAdapter.OnProductUpdateListener {
    private RecyclerView recyclerItens;
    private CarrinhoAdapter carrinhoAdapter;
    private VendaSharedViewModel sharedViewModel;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.carrinho_venda, container, false);

        carrinhoAdapter = new CarrinhoAdapter(this);

        setupRecyclerView(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(VendaSharedViewModel.class);
        sharedViewModel.getItems().observe(getViewLifecycleOwner(), carrinhoAdapter::setItensCarrinho);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void setupRecyclerView(View view) {
        recyclerItens = view.findViewById(R.id.recycler_itens_carrinho);
        recyclerItens.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerItens.setAdapter(carrinhoAdapter);
    }

    @Override
    public void OnProductUpdateListener(List<ItemPedido> pedidos) {
        double total = pedidos.stream().mapToDouble(ItemPedido::getValorTotal).sum();
        TextView totalTextView = view.findViewById(R.id.total);
        totalTextView.setText("Total: " + String.valueOf(total));
    }
}
