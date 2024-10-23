package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    private String baseURL = "http://172.27.32.1/InventoryApp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Add password visibility toggle functionality
        editTextPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Right side drawable
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    // Toggle password visibility
                    if (editTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, R.drawable.visibilityoff_icon, 0);
                    } else {
                        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, R.drawable.visibilityon_icon, 0);
                    }
                    // Move cursor to the end of the input
                    editTextPassword.setSelection(editTextPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        // Set click listener for the Login button
        buttonLogin.setOnClickListener(v -> login());

        // Set click listener for the Register button
        buttonRegister.setOnClickListener(v -> navigateToRegisterPage());
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim().toLowerCase(); // Normalize username
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = baseURL + "login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("LoginResponse", response); // Add this line for debugging
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        if ("Login successful".equalsIgnoreCase(message)) {
                            Intent intent = new Intent(LoginActivity.this, InventoryPage.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMessage = "Network error";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = "Network error: " + new String(error.networkResponse.data);
                    } else if (error.getCause() != null) {
                        errorMessage = "Network error: " + error.getCause().getMessage();
                    } else {
                        errorMessage = "Network error: " + error.getMessage();
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void navigateToRegisterPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
