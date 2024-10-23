package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast; // Import Toast
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

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonLogin;
    private Button buttonHelp;
    private ImageView imageViewTogglePassword;
    private boolean isPasswordVisible = false;
    private String baseURL = "http://172.27.32.1/InventoryApp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonHelp = findViewById(R.id.buttonHelp);
        imageViewTogglePassword = findViewById(R.id.imageViewTogglePassword);

        // Toggle password visibility
        imageViewTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide the password
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewTogglePassword.setImageResource(R.drawable.visibilityon_icon);
                    isPasswordVisible = false;
                } else {
                    // Show the password
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewTogglePassword.setImageResource(R.drawable.visibilityoff_icon);
                    isPasswordVisible = true;
                }
                // Move cursor to the end of the text
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });

        // Help button click listener
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelpActivity();
            }
        });

        // Register button click listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewAccount();
            }
        });

        // Login button click listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginPage();
            }
        });
    }

    private void openHelpActivity() {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    private void registerNewAccount() {
        Log.d("MainActivity", "Register button clicked");
        final String username = editTextUsername.getText().toString().trim().toLowerCase();
        final String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!username.contains("@gmail.com")) {
            Toast.makeText(this, "Username must contain @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            Toast.makeText(this, "Password must be 8 characters or more and contain special characters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = baseURL + "register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyResponse", "Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (message.equalsIgnoreCase("Username taken")) {
                                Toast.makeText(MainActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                            } else if (message.equalsIgnoreCase("Registration successful")) {
                                editTextUsername.setText("");
                                editTextPassword.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error: " + error.toString());
                String errorMessage = error.networkResponse != null ?
                        "Network error: " + new String(error.networkResponse.data) :
                        "Network error: " + error.getMessage();
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void navigateToLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
