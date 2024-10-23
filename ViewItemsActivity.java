package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class ViewItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItems;
    private Button buttonBackToDisplay;
    private RequestQueue requestQueue;
    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        // Initialize UI components
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        buttonBackToDisplay = findViewById(R.id.buttonBackToDisplay);

        // Set RecyclerView properties
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Fetch and display items
        fetchItems();

        // Set click listener for Back to Display button
        buttonBackToDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewItemsActivity.this, DisplayItems.class);
                startActivity(intent);
            }
        });
    }

    private void fetchItems() {
        String url = "http://172.27.32.1/InventoryApp/display_items.php";
        Log.d("ViewItemsActivity", "Fetching items from URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ViewItemsActivity", "Response: " + response.toString());

                        // Set up adapter and pass the items to it
                        itemsAdapter = new ItemsAdapter(ViewItemsActivity.this, response);
                        recyclerViewItems.setAdapter(itemsAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ViewItemsActivity", "Volley error: " + error.toString());
                        Toast.makeText(ViewItemsActivity.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}
