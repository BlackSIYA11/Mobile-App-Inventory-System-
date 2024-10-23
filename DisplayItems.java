package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayItems extends AppCompatActivity {

    private LinearLayout layoutItemsContainer;
    private TextView textViewTotalAmount;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayitems);

        // Initialize UI components
        layoutItemsContainer = findViewById(R.id.layoutItemsContainer);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        Button buttonDisplayTotal = findViewById(R.id.buttonDisplayTotal);
        Button buttonReturnToMenu = findViewById(R.id.buttonReturnToMenu);
        Button buttonViewItems = findViewById(R.id.buttonViewItems);
        Button buttonClearTotal = findViewById(R.id.buttonClearTotal);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for Display Total Amount button
        buttonDisplayTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTotalAmount();  // Fetch and display total amount when the button is clicked
            }
        });

        // Set click listener for View Items button
        buttonViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayItems.this, ViewItemsActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for Clear Total Amount button
        buttonClearTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTotalAmount();  // Clear the total amount displayed
            }
        });

        // Set click listener for Return to Menu button
        buttonReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });
    }
    private void clearTotalAmount() {
        textViewTotalAmount.setText("Total Amount: R0.00"); // Clear total amount display
        Toast.makeText(this, "Total amount cleared", Toast.LENGTH_SHORT).show(); // Feedback message
    }

    private void fetchItems() {
        String url = "http://172.27.32.1/InventoryApp/display_items.php";
        Log.d("DisplayItems", "Fetching items from URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DisplayItems", "Response: " + response.toString());
                        layoutItemsContainer.removeAllViews(); // Clear previous views
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = response.getJSONObject(i);

                                String itemDetails = "ID: " + item.getString("id") +
                                        ", Name: " + item.getString("name") +
                                        ", Price: R" + item.getDouble("price") +
                                        ", Quantity: " + item.getInt("quantity") +
                                        ", Description: " + item.getString("description") +
                                        ", Category: " + item.getString("category");

                                // Create a TextView for each item and add it to the layout
                                TextView textView = new TextView(DisplayItems.this);
                                textView.setText(itemDetails);
                                textView.setTextSize(16);
                                layoutItemsContainer.addView(textView);
                            } catch (JSONException e) {
                                Log.e("DisplayItems", "Error parsing item data", e);
                                Toast.makeText(DisplayItems.this, "Error parsing item data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DisplayItems", "Volley error: " + error.toString());
                        Toast.makeText(DisplayItems.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchTotalAmount() {
        String url = "http://172.27.32.1/InventoryApp/display_total_amount.php"; // Ensure this points to the correct script

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DisplayItems", "Response: " + response.toString());
                        StringBuilder totalsBuilder = new StringBuilder();
                        totalsBuilder.append("Total Amounts by Category:\n");  // Add a header for better readability
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject categoryData = response.getJSONObject(i);
                                String category = categoryData.getString("category");
                                double totalAmount = categoryData.getDouble("total_amount");
                                totalsBuilder.append("\n• Category: ").append(category)
                                        .append("\n  → Total Amount: R").append(String.format("%.2f", totalAmount))
                                        .append("\n");  // Indent the amount for clarity
                            }
                            textViewTotalAmount.setText(totalsBuilder.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DisplayItems.this, "Error parsing total amount data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DisplayItems", "Error: " + error.toString());
                        Toast.makeText(DisplayItems.this, "Failed to retrieve total amount", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }


    private void returnToMenu() {
        Intent intent = new Intent(this, InventoryPage.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}