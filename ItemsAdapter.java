package com.example.inventorymanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private JSONArray items;
    private Context context;

    // Constructor to pass the context and the list of items
    public ItemsAdapter(Context context, JSONArray items) {
        this.context = context;
        this.items = items;
    }

    // Create a new view for each item (invoked by the LayoutManager)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(view);
    }

    // Replace the contents of a view (invoked by the LayoutManager)
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        try {
            JSONObject item = items.getJSONObject(position);  // Get item data for the current position

            // Extract and set the item details
            String itemName = item.getString("name");
            String itemDetails = "ID: " + item.getString("id") +
                    "\nPrice: R" + item.getDouble("price") +
                    "\nQuantity: " + item.getInt("quantity") +
                    "\nCategory: " + item.getString("category") +
                    "\nDescription: " + item.getString("description");

            holder.textItemName.setText(itemName);  // Set item name
            holder.textItemDetails.setText(itemDetails);  // Set item details
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the total number of items in the data set
    @Override
    public int getItemCount() {
        return items.length();
    }

    // Provide a reference to the views for each item
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textItemDetails;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);  // Bind item name TextView
            textItemDetails = itemView.findViewById(R.id.textItemDetails);  // Bind item details TextView
        }
    }
}
