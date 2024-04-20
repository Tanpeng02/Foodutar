package com.example.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodie.R;

public class Feedback extends AppCompatActivity {

    private Spinner foodStallSpinner;
    private RatingBar question1RatingBar;
    private EditText descriptionEditText;
    private Button submitFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        foodStallSpinner = findViewById(R.id.foodStallSpinner);
        question1RatingBar = findViewById(R.id.question1Rating);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_stalls_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodStallSpinner.setAdapter(adapter);
        foodStallSpinner.setPrompt("Select a food stall");
        foodStallSpinner.setSelection(0);

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String selectedFoodStall = foodStallSpinner.getSelectedItem().toString();
        float question1Rating = question1RatingBar.getRating();
        String feedbackDescription = descriptionEditText.getText().toString();

        String feedbackDetails = "Food Stall: " + selectedFoodStall + "\n" +
                "Question 1 Rating: " + question1Rating + "\n" +
                "Feedback Description: " + feedbackDescription;

        Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show();

        // Refresh the activity
        refreshActivity();
    }

    private void refreshActivity() {
        finish(); // Finish the current activity
        startActivity(getIntent()); // Restart the activity
    }
}
