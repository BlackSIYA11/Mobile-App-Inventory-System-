package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html; // Import for HTML formatting
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpInventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_inventory);

        TextView helpTextView = findViewById(R.id.textViewHelp);
        Button buttonReturn = findViewById(R.id.buttonReturn); // Initialize the return button

        // Set help instructions with HTML formatting
        String helpInstructions =
                "<h1>Welcome to the Inventory Management Help Section!</h1>" +
                        "<h2>Here are some important instructions:</h2>" +
                        "<ol>" +
                        "<li><strong>Add Item:</strong> Click on the 'Add Item' button to open the form to add a new item. Look at examples provided on how to input.</li>" +
                        "<li><strong>Edit Item:</strong> Use the 'Edit Item' button to modify existing item details. Make use of the Search function to edit the item you're looking for.</li>" +
                        "<li><strong>Delete Item:</strong> Select an item and click 'Delete Item' to remove it from the inventory. Use the search function, press search, and then proceed to delete the item.</li>" +
                        "<li><strong>Display Item:</strong> Click 'Display Item' to view all items in the inventory.</li>" +
                        "<li><strong>Logout:</strong> Use the 'Logout' button to exit the inventory management system.</li>" +
                        "</ol>";

        // Set the formatted text
        helpTextView.setText(Html.fromHtml(helpInstructions, Html.FROM_HTML_MODE_LEGACY));

        // Set click listener for Return button
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToInventoryPage();
            }
        });
    }

    private void returnToInventoryPage() {
        Intent intent = new Intent(this, InventoryPage.class);
        startActivity(intent);
    }
}
