package com.example.inventorymanagementapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EditItem extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText editTextSearch;
    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private Button buttonSearch;
    private Button buttonSaveChanges;
    private Button buttonReturnToMenu;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        // Initialize UI components
        editTextSearch = findViewById(R.id.editTextSearch);
        editTextItemID = findViewById(R.id.editTextItemID);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonReturnToMenu = findViewById(R.id.buttonReturnToMenu);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Define categories array
        final String[] categories = {"Engineering", "Road and Earthworks", "Repairs and Maintenance", "Construction", "Category 5"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set click listener for "Search Item" button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });

        // Set click listener for "Save Changes" button
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSaveChanges();
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

        String url = "http://172.27.32.1/InventoryApp/edit_items.php?query=" + searchQuery;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("error")) {
                                Toast.makeText(EditItem.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                editTextItemID.setText(response.getString("itemID"));
                                editTextItemName.setText(response.getString("itemName"));
                                editTextPrice.setText(response.getString("price"));
                                editTextQuantity.setText(response.getString("quantity"));
                                editTextDescription.setText(response.getString("description"));
                                setSpinnerSelection(spinnerCategory, response.getString("category"));

                                // Disable ItemID field to make it read-only
                                editTextItemID.setEnabled(false);
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditItem.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditItem.this, "Failed to retrieve item", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void confirmSaveChanges() {
        new AlertDialog.Builder(this)
                .setTitle("Save Changes")
                .setMessage("Are you sure you want to save changes?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveChanges();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void saveChanges() {
        // Create a JSONObject for the POST request
        JSONObject jsonObject = new JSONObject();
        try {
            String itemID = editTextItemID.getText().toString().trim();
            // Ensure itemID is present
            if (!itemID.isEmpty()) {
                jsonObject.put("itemID", itemID);

                String itemName = editTextItemName.getText().toString().trim();
                if (!itemName.isEmpty()) jsonObject.put("itemName", itemName);

                String priceStr = editTextPrice.getText().toString().trim();
                if (!priceStr.isEmpty()) jsonObject.put("price", Double.parseDouble(priceStr));

                String quantityStr = editTextQuantity.getText().toString().trim();
                if (!quantityStr.isEmpty()) jsonObject.put("quantity", Integer.parseInt(quantityStr));

                String description = editTextDescription.getText().toString().trim();
                if (!description.isEmpty()) jsonObject.put("description", description);

                String category = spinnerCategory.getSelectedItem().toString().trim();
                if (!category.isEmpty()) jsonObject.put("category", category);
            } else {
                Toast.makeText(EditItem.this, "Item ID is required", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the request
        String url = "http://172.27.32.1/InventoryApp/edit_items.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("error")) {
                                Toast.makeText(EditItem.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                            } else if (response.has("success")) {
                                Toast.makeText(EditItem.this, response.getString("success"), Toast.LENGTH_SHORT).show();
                                clearFields();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditItem.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditItem.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void setSpinnerSelection(Spinner spinner, String category) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(category);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    private void clearFields() {
        editTextItemID.setText("");
        editTextItemName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextDescription.setText("");
        // Ensure ItemID field is re-enabled if clearing fields
        editTextItemID.setEnabled(true);
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
