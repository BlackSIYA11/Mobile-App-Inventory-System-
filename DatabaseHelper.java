package com.example.inventorymanagementapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventoryapp.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "item_id"; // Modified to TEXT
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";

    // SQL statement to create the table
    private static final String SQL_CREATE_TABLE_ITEMS =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COLUMN_ITEM_ID + " TEXT PRIMARY KEY," + // Modified to TEXT
                    COLUMN_ITEM_NAME + " TEXT," +
                    COLUMN_PRICE + " REAL," +
                    COLUMN_QUANTITY + " INTEGER," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_CATEGORY + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(SQL_CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Method to insert an item into the database
    public boolean insertItem(String itemID, String itemName, double price, int quantity, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, itemID);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY, category);
        long result = db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
        // Insertion was successful if result is not -1
        return result != -1 ;
    }

    // Method to retrieve an item from the database by its ID
    public Item getItem(String itemID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null, COLUMN_ITEM_ID + "=?", new String[]{itemID}, null, null, null);
        Item item = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Extract item details from the cursor
            @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
            @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
            @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
            // Create a new Item object with the retrieved details
            item = new Item(itemID, itemName, price, quantity, description, category);
            cursor.close();
        }
        db.close(); // Close database connection
        return item;
    }

    // Method to update an item in the database
    public boolean updateItem(String itemId, String itemName, double price, int quantity, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY, category);

        // Updating row
        int rowsAffected = db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + " = ?", new String[]{itemId});
        db.close();

        // Return true if the row was updated successfully, false otherwise
        return rowsAffected > 0;
    }
}
