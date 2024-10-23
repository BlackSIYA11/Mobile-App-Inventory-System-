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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DeleteItem extends AppCompatActivity {

    private EditText editTextSearch;
    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private Button buttonSearch;
    private Button buttonDelete;
    private Button buttonReturnToMenu;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteitem);

        // Initialize UI components
        editTextSearch = findViewById(R.id.editTextSearch);
        editTextItemID = findViewById(R.id.editTextItemID);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonReturnToMenu = findViewById(R.id.buttonReturnToMenu);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for "Search Item" button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });

        // Set click listener for "Delete" button
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });

        // Set click listener for "Return to Menu" button
        buttonReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });
    }

    private void searchItem() {
        String searchQuery = editTextSearch.getText().toString().trim();

        String url = "http://172.27.32.1/InventoryApp/delete_items.php?query=" + searchQuery;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("error")) {
                                Toast.makeText(DeleteItem.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                editTextItemID.setText(response.getString("itemID"));
                                editTextItemName.setText(response.getString("itemName"));
                                editTextPrice.setText(response.getString("price"));
                                editTextQuantity.setText(response.getString("quantity"));
                                editTextDescription.setText(response.getString("description"));

                                // Disable ItemID field to make it read-only
                                editTextItemID.setEnabled(false);
                                buttonDelete.setEnabled(true); // Enable the delete button
                            }
                        } catch (Exception e) {
                            Toast.makeText(DeleteItem.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeleteItem.this, "Failed to retrieve item", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteItem() {
        String itemID = editTextItemID.getText().toString().trim();

        // Create JSON object for request
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemID", itemID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send the request
        String url = "http://172.27.32.1/InventoryApp/delete_items.php"; // Ensure this is the correct URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("success")) {
                                Toast.makeText(DeleteItem.this, response.getString("success"), Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                Toast.makeText(DeleteItem.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DeleteItem.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeleteItem.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void clearFields() {
        editTextItemID.setText("");
        editTextItemName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextDescription.setText("");
        // Re-enable ItemID field if clearing fields
        editTextItemID.setEnabled(true);
        buttonDelete.setEnabled(false); // Disable the delete button until an item is found
    }

    private void returnToMenu() {
        Intent intent = new Intent(this, InventoryPage.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(this);
    }
}
