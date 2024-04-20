package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodie.foodstall.AuntyJuliaCafeteria;
import com.example.foodie.foodstall.CFourNoodlesStall;
import com.example.foodie.foodstall.CakersInnBakery;
import com.example.foodie.foodstall.CharisFoodCorner;
import com.example.foodie.foodstall.EggettePop;
import com.example.foodie.foodstall.MCafeEconomyRiceStall;
import com.example.foodie.foodstall.UncleAlanVegetarian;

public class FoodStall extends AppCompatActivity {

    private CardView eggettePopCard, mCafeEconomyRiceStallCard, cakersInnBakeryCard, auntyJuliaCafeteriaCard,
            uncleAlanVegetarianCard, cFourNoodlesStallCard, charisFoodCornerCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stall);

        // Initialize the CardViews
        eggettePopCard = findViewById(R.id.EggettePopCard);
        mCafeEconomyRiceStallCard = findViewById(R.id.MCafeEconomyRiceStallCard);
        cakersInnBakeryCard = findViewById(R.id.CakersInnBakeryCard);
        auntyJuliaCafeteriaCard = findViewById(R.id.AuntyJuliaCafeteriaCard);
        uncleAlanVegetarianCard = findViewById(R.id.UncleAlanVegetarianCard);
        cFourNoodlesStallCard = findViewById(R.id.CFourNoodlesStallCard);
        charisFoodCornerCard = findViewById(R.id.CharisFoodCornerCard);

        // Set click listeners for the CardViews
        eggettePopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the EggettePopActivity when the EggettePopCard is clicked
                startActivity(new Intent(FoodStall.this, EggettePop.class));
            }
        });

        mCafeEconomyRiceStallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the DiscussionCommunityActivity when the DiscussionCommunityCard is clicked
                startActivity(new Intent(FoodStall.this, MCafeEconomyRiceStall.class));
            }
        });

        cakersInnBakeryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the LocationActivity when the LocationCard is clicked
                startActivity(new Intent(FoodStall.this, CakersInnBakery.class));
            }
        });

        auntyJuliaCafeteriaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FeedbackActivity when the FeedbackCard is clicked
                startActivity(new Intent(FoodStall.this, AuntyJuliaCafeteria.class));
            }
        });

        uncleAlanVegetarianCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FeedbackActivity when the FeedbackCard is clicked
                startActivity(new Intent(FoodStall.this, UncleAlanVegetarian.class));
            }
        });

        cFourNoodlesStallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FeedbackActivity when the FeedbackCard is clicked
                startActivity(new Intent(FoodStall.this, CFourNoodlesStall.class));
            }
        });

        charisFoodCornerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FeedbackActivity when the FeedbackCard is clicked
                startActivity(new Intent(FoodStall.this, CharisFoodCorner.class));
            }
        });
    }
}