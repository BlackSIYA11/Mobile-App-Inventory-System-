package com.example.inventorymanagementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemDataSource {

    private static final String TAG = "ItemDataSource";

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ItemDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open the database connection
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }

    // Insert a new item into the database
    public long insertItem(String itemID, String itemName, double price, int quantity, String description, String category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_ID, itemID);
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);

        long result = database.insert(DatabaseHelper.TABLE_ITEMS, null, values);

        // Log insertion result
        if (result != -1) {
            Log.d(TAG, "Item inserted successfully with ID: " + itemID);
        } else {
            Log.e(TAG, "Failed to insert item");
        }

        return result;
    }

    // Update an existing item in the database
    public int updateItem(String itemID, String itemName, double price, int quantity, String description, String category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);

        Log.d(TAG, "Updating item: ID=" + itemID + ", Name=" + itemName + ", Price=" + price + ", Quantity=" + quantity + ", Description=" + description + ", Category=" + category);

        int rowsAffected = database.update(DatabaseHelper.TABLE_ITEMS, values, DatabaseHelper.COLUMN_ITEM_ID + " = ?",
                new String[]{itemID});

        Log.d(TAG, "Rows affected: " + rowsAffected);
        return rowsAffected;
    }

    // Delete an item from the database
    public int deleteItem(String itemID) {
        return database.delete(DatabaseHelper.TABLE_ITEMS, DatabaseHelper.COLUMN_ITEM_ID + " = ?",
                new String[]{itemID});
    }

    // Retrieve all items from the database
    public Cursor getAllItems() {
        return database.query(DatabaseHelper.TABLE_ITEMS, null, null, null, null, null, null);
    }

    // Retrieve an item by its ID
    public Cursor getItemById(String itemId) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ITEMS,
                null,
                DatabaseHelper.COLUMN_ITEM_ID + " = ?",
                new String[]{itemId},
                null,
                null,
                null
        );

        // Log cursor content
        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "Cursor content: " + cursorToString(cursor));
        } else {
            Log.d(TAG, "No item found with ID: " + itemId);
        }

        return cursor;
    }

    // Retrieve an item by its name
    public Cursor getItemByName(String name) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ITEMS,
                null,
                DatabaseHelper.COLUMN_ITEM_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        // Log cursor content
        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "Cursor content: " + cursorToString(cursor));
        } else {
            Log.d(TAG, "No item found with name: " + name);
        }

        return cursor;
    }

    // Helper method to convert cursor to string for logging
    private String cursorToString(Cursor cursor) {
        StringBuilder builder = new StringBuilder();
        if (cursor != null) {
            int numColumns = cursor.getColumnCount();
            do {
                for (int i = 0; i < numColumns; i++) {
                    builder.append(cursor.getColumnName(i)).append(": ").append(cursor.getString(i)).append(", ");
                }
                builder.append("\n");
            } while (cursor.moveToNext());
        }
        return builder.toString();
    }
}
