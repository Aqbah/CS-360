package com.example.aqbahinventoryapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements InventoryAdapter.InventoryCallbacks {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<InventoryItem> items = new ArrayList<>();

    // For sending SMS when low/zero stock
    private static final String TEST_PHONE = "5556"; // Emulator phone # (change as needed)

    private final ActivityResultLauncher<Intent> addItemLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> refreshList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.rvInventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InventoryAdapter(items, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddItem);
        fab.setOnClickListener(v -> addItemLauncher.launch(new Intent(this, AddItemActivity.class)));
    }

    @Override protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        items.clear();
        items.addAll(db.getAllItems());
        adapter.notifyDataSetChanged();
    }

    // Adapter callbacks
    @Override public void onDeleteClicked(InventoryItem item) {
        if (db.deleteItem(item.id)) {
            Toast.makeText(this, "Deleted " + item.name, Toast.LENGTH_SHORT).show();
            refreshList();
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onRowClicked(InventoryItem item) {
        // Edit quantity dialog
        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("New quantity");
        input.setText(String.valueOf(item.quantity));

        new AlertDialog.Builder(this)
                .setTitle("Update Quantity")
                .setMessage("Set quantity for: " + item.name)
                .setView(input)
                .setPositiveButton("Save", (DialogInterface dialog, int which) -> {
                    String txt = input.getText().toString().trim();
                    if (txt.isEmpty()) {
                        Toast.makeText(this, "Quantity is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int newQty = Integer.parseInt(txt);
                    if (db.updateQuantity(item.id, newQty)) {
                        // Re-fetch the updated item
                        InventoryItem updated = db.getItem(item.id);
                        if (updated != null && updated.quantity <= updated.threshold) {
                            maybeSendLowInventorySms(updated);
                        }
                        refreshList();
                    } else {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void maybeSendLowInventorySms(InventoryItem item) {
        // If permission is granted, send SMS. Otherwise, skip silently (app continues functioning).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            SmsUtils.sendLowInventorySms(this, TEST_PHONE, item);
            Toast.makeText(this, "Low inventory alert sent via SMS", Toast.LENGTH_SHORT).show();
        } else {
            // Optionally, direct user to permissions screen
            // startActivity(new Intent(this, SmsPermissionActivity.class));
        }
    }
}
