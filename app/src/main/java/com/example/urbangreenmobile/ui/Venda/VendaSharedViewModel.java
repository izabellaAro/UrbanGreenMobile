package com.example.urbangreenmobile.ui.Venda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urbangreenmobile.api.models.Pedido.ItemPedido;

import java.util.List;

public class VendaSharedViewModel extends ViewModel {
    private final MutableLiveData<List<ItemPedido>> items = new MutableLiveData<>();

    public LiveData<List<ItemPedido>> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> itemList) {
        items.setValue(itemList);
    }
}
