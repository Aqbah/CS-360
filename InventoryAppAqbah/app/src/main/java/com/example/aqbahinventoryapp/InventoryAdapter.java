package com.example.aqbahinventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.VH> {

    public interface InventoryCallbacks {
        void onDeleteClicked(InventoryItem item);
        void onRowClicked(InventoryItem item);
    }

    private final List<InventoryItem> items;
    private final InventoryCallbacks callbacks;

    public InventoryAdapter(List<InventoryItem> items, InventoryCallbacks callbacks) {
        this.items = items;
        this.callbacks = callbacks;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvQty;
        Button btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        InventoryItem item = items.get(pos);
        h.tvName.setText(item.name);
        h.tvQty.setText("Quantity: " + item.quantity);

        h.itemView.setOnClickListener(v -> {
            if (callbacks != null) callbacks.onRowClicked(item);
        });
        h.btnDelete.setOnClickListener(v -> {
            if (callbacks != null) callbacks.onDeleteClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
