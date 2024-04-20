package com.example.foodie.foodstall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodie.DiscussionCommunity;
import com.example.foodie.Feedback;
import com.example.foodie.Location;
import com.example.foodie.LocationcFourNoodlesStall;
import com.example.foodie.R;
import com.example.foodie.menu.CFourNoodlesStallMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CFourNoodlesStall extends AppCompatActivity {

    // to check whether sub FABs are visible or not
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfour_noodles_stall); // Make sure this matches your XML file name

        // Initialize your views here if needed
        Button menuButton = findViewById(R.id.menuButton);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton location = findViewById(R.id.add_location);
        FloatingActionButton discussionCommunity = findViewById(R.id.add_discussion_community);
        FloatingActionButton feedback = findViewById(R.id.add_feedback);

        location.setVisibility(View.GONE);
        discussionCommunity.setVisibility(View.GONE);
        feedback.setVisibility(View.GONE);

        // make the boolean variable as false, as all the action name and all the sub FABs are invisible
        isAllFabsVisible = false;

        // Setting a click listener on the menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CFourNoodlesStall.this, "Menu Clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CFourNoodlesStall.this, CFourNoodlesStallMenu.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs VISIBLE.
                            location.show();
                            discussionCommunity.show();
                            feedback.show();

                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = true;
                        } else {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            location.hide();
                            discussionCommunity.hide();
                            feedback.hide();

                            // make the boolean variable false
                            // as we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = false;
                        }
                    }
                });

        // below is the sample action to handle add person
        // FAB. Here it shows simple Toast msg. The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CFourNoodlesStall.this, "Navigating to Location page...", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CFourNoodlesStall.this, LocationcFourNoodlesStall.class);
                        startActivity(intent);
                    }
                });

        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        discussionCommunity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CFourNoodlesStall.this, "Navigating to Dicussion Community page...", Toast.LENGTH_SHORT).show();
                        // Create an Intent to start LocationActivity
                        Intent intent = new Intent(CFourNoodlesStall.this, DiscussionCommunity.class);
                        startActivity(intent);
                    }
                });
        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        feedback.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CFourNoodlesStall.this, "Navigating to Feedback page...", Toast.LENGTH_SHORT).show();

                        // Create an Intent to start LocationActivity
                        Intent intent = new Intent(CFourNoodlesStall.this, Feedback.class);
                        startActivity(intent);
                    }
                });
    }
}
