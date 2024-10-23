package com.example.inventorymanagementapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InventoryPage extends AppCompatActivity {

    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private EditText editTextCategory;
    private Button buttonSaveItem;
    private Button buttonAddItem;
    private Button buttonEditItem;
    private Button buttonDeleteItem;
    private Button buttonDisplayItems;
    private Button buttonLogout;

    private Button buttonHelp;

    private ItemDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventorypage);

        // Initialize UI components
        editTextItemID = findViewById(R.id.editTextItemID);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        buttonSaveItem = findViewById(R.id.buttonSaveItem);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonEditItem = findViewById(R.id.buttonEditItem);
        buttonDeleteItem = findViewById(R.id.buttonDeleteItem);
        buttonDisplayItems = findViewById(R.id.buttonDisplayItems);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonHelp = findViewById(R.id.buttonHelp);

        // Initialize the ItemDataSource
        dataSource = new ItemDataSource(this);
        dataSource.open();

        // Set click listener for Save button
        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItemToDatabase();
            }
        });

        // Set click listener for Add Item button
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryPage.this, AddItem.class));
            }
        });

        // Set click listener for Edit Item button
        buttonEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryPage.this, EditItem.class));
            }
        });

        // Set click listener for Delete Item button
        buttonDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryPage.this, DeleteItem.class));
            }
        });

        // Set click listener for Display Items button
        buttonDisplayItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryPage.this, DisplayItems.class));
            }
        });

        // Set click listener for Help button
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryPage.this, HelpInventory.class));
            }
        });

        // Set click listener for Logout button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }

    // Method to handle saving the item
    private void saveItemToDatabase() {
        String itemID = editTextItemID.getText().toString().trim();
        String itemName = editTextItemName.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String quantity = editTextQuantity.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();

        if (itemID.isEmpty() || itemName.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long itemId = dataSource.insertItem(itemID, itemName, Double.parseDouble(price), Integer.parseInt(quantity), description, category);
        if (itemId != -1) {
            Toast.makeText(this, "Item saved successfully with ID: " + itemId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
        }

        clearFields();
    }

    // Method to clear input fields
    private void clearFields() {
        editTextItemID.setText("");
        editTextItemName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextDescription.setText("");
        editTextCategory.setText("");
    }

    // Method to show logout confirmation dialog
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        performLogout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });
        builder.create().show();
    }

    // Method to handle logout
    private void performLogout() {
        startActivity(new Intent(InventoryPage.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
