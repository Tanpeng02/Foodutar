package com.example.foodie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodie.R;

public class LocationMCafeEconomyRiceStall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_mcafe_economy_rice_stall);

        EditText editTextSource = findViewById(R.id.Mcafesource);
        EditText editTextDestination = findViewById(R.id.Mcafedestination);
        Button button = findViewById(R.id.navigationbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source = editTextSource.getText().toString();
                String destination = editTextDestination.getText().toString();
                if (source.isEmpty() || destination.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter both source and destination", Toast.LENGTH_SHORT).show();
                } else {
                    // Encode source and destination for URI
                    String encodedSource = Uri.encode(source);
                    String encodedDestination = Uri.encode(destination);

                    // Construct URI with source and destination
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + encodedSource + "&destination=" + encodedDestination);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

}
