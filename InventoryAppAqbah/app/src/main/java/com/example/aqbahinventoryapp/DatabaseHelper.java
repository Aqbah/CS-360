package com.example.aqbahinventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite DB:
 *  - users(username TEXT PRIMARY KEY, password TEXT NOT NULL)
 *  - inventory(_id INTEGER PK, name TEXT, sku TEXT, quantity INT, threshold INT)
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "inventory_app.db";
    public static final int DB_VERSION = 1;

    // Users
    public static final String TABLE_USERS = "users";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Inventory
    public static final String TABLE_INVENTORY = "inventory";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_SKU = "sku";
    public static final String COL_QTY = "quantity";
    public static final String COL_THRESHOLD = "threshold";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USERNAME + " TEXT PRIMARY KEY, " +
                COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsers);

        String createInventory = "CREATE TABLE " + TABLE_INVENTORY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_SKU + " TEXT, " +
                COL_QTY + " INTEGER NOT NULL DEFAULT 0, " +
                COL_THRESHOLD + " INTEGER NOT NULL DEFAULT 0)";
        db.execSQL(createInventory);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------------- Users ----------------
    public boolean createUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username.trim());
        cv.put(COL_PASSWORD, password);
        long result = -1;
        try {
            result = db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (Exception ignored) {}
        return result != -1;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS,
                new String[]{COL_USERNAME},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );
        boolean ok = (c != null && c.moveToFirst());
        if (c != null) c.close();
        return ok;
    }

    // ---------------- Inventory ----------------
    public long addItem(String name, String sku, int qty, int threshold) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_SKU, sku);
        cv.put(COL_QTY, qty);
        cv.put(COL_THRESHOLD, threshold);
        return db.insert(TABLE_INVENTORY, null, cv);
    }

    public boolean deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_INVENTORY, COL_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean updateQuantity(long id, int newQty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QTY, newQty);
        int rows = db.update(TABLE_INVENTORY, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<InventoryItem> getAllItems() {
        ArrayList<InventoryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_INVENTORY,
                null, null, null, null, null, COL_NAME + " ASC");
        if (c != null) {
            while (c.moveToNext()) {
                InventoryItem item = new InventoryItem(
                        c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                        c.getString(c.getColumnIndexOrThrow(COL_SKU)),
                        c.getInt(c.getColumnIndexOrThrow(COL_QTY)),
                        c.getInt(c.getColumnIndexOrThrow(COL_THRESHOLD))
                );
                items.add(item);
            }
            c.close();
        }
        return items;
    }

    public InventoryItem getItem(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_INVENTORY, null, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        InventoryItem item = null;
        if (c != null && c.moveToFirst()) {
            item = new InventoryItem(
                    c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_SKU)),
                    c.getInt(c.getColumnIndexOrThrow(COL_QTY)),
                    c.getInt(c.getColumnIndexOrThrow(COL_THRESHOLD))
            );
            c.close();
        }
        return item;
    }
}
