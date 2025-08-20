package com.example.aqbahinventoryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddItemActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etName, etSku, etQty, etThreshold;
    private static final String TEST_PHONE = "5556"; // Emulator phone # (change if needed)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etItemName);
        etSku = findViewById(R.id.etSKU);
        etQty = findViewById(R.id.etQuantity);
        etThreshold = findViewById(R.id.etThreshold);

        Button btnSave = findViewById(R.id.btnSaveItem);
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = nonNull(etName.getText());
        String sku = nonNull(etSku.getText());
        String qtyStr = nonNull(etQty.getText());
        String thrStr = nonNull(etThreshold.getText());

        if (name.isEmpty() || qtyStr.isEmpty() || thrStr.isEmpty()) {
            Toast.makeText(this, "Name, Quantity, and Threshold are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int qty = Integer.parseInt(qtyStr);
        int thr = Integer.parseInt(thrStr);

        long id = db.addItem(name, sku, qty, thr);
        if (id != -1) {
            Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();

            // Optional SMS notify if at/below threshold
            if (qty <= thr && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                InventoryItem item = new InventoryItem(id, name, sku, qty, thr);
                SmsUtils.sendLowInventorySms(this, TEST_PHONE, item);
            }

            finish();
        } else {
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
        }
    }

    private static String nonNull(CharSequence cs) {
        return cs == null ? "" : cs.toString().trim();
    }
}
