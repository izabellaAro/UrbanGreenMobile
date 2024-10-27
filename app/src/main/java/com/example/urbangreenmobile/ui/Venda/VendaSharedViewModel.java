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

    public void updateItem(ItemPedido updatedItem, int position) {
        List<ItemPedido> currentItems = items.getValue();
        if (currentItems != null) {
            currentItems.set(position, updatedItem);
            items.setValue(currentItems);  // Notifica os observadores
        }
    }
}
