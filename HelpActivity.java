package com.example.inventorymanagementapp;

import android.os.Bundle;
import android.text.Html; // Import for HTML formatting
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    private TextView textViewInstructions;
    private Button buttonCloseHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        textViewInstructions = findViewById(R.id.textViewInstructions);
        buttonCloseHelp = findViewById(R.id.buttonCloseHelp);

        // Set help instructions with HTML headings
        String helpInstructions =
                "<h1>Help & Navigation</h1>" +
                        "<p>Welcome to the Inventory Management App!  <br>" +
                        "This guide will help you navigate and use the app effectively.</p>" +
                        "<h2>How to Register</h2>" +
                        "<ol>" +
                        "<li>Enter Your Username: Use a valid email format (e.g., Username1@gmail.com).</li>" +
                        "<li>Set Your Password: The password must be at least 8 characters long and include special characters and numbers.</li>" +
                        "<li>You can make use of the visibility on and off icon on the right hand side of your password textbox to see if your password is correct.</li>" +
                        "<li>Click the 'Register' Button: Once you've filled in your details, click the button to create your account.</li>" +
                        "<li>Once done, click the Login Button, which will take you to the Login Page where you Login.</li>" +
                        "</ol>" +
                        "<h2>How to Log In</h2>" +
                        "<ol>" +
                        "<li>Input Your Credentials: Enter your registered username and password.</li>" +
                        "<li>Click the 'Login' Button: Press the button to access the Inventory Page.</li>" +
                        "</ol>";

        // Set the formatted text
        textViewInstructions.setText(Html.fromHtml(helpInstructions, Html.FROM_HTML_MODE_LEGACY));

        // Close button click listener
        buttonCloseHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the HelpActivity
            }
        });
    }
}
