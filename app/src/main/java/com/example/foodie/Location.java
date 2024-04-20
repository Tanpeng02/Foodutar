package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Location extends AppCompatActivity {

    private CardView locationEggettePopCard , locationmCafeEconomyRiceStallCard , locationcakersInnBakeryCard , locationauntyJuliaCafeteriaCard , locationuncleAlanVegetarianCard , locationcFourNoodlesStallCard, locationcharisFoodCornerCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationEggettePopCard = findViewById(R.id.LocationEggettePopCard);
        locationmCafeEconomyRiceStallCard = findViewById(R.id.LocationMCafeEconomyRiceStallCard);
        locationcakersInnBakeryCard = findViewById(R.id.LocationCakersInnBakeryCard);
        locationauntyJuliaCafeteriaCard =findViewById(R.id.LocationAuntyJuliaCafeteriaCard);
        locationuncleAlanVegetarianCard = findViewById(R.id.LocationUncleAlanVegetarianCard);
        locationcFourNoodlesStallCard = findViewById(R.id.LocationCFourNoodlesStallCard);
        locationcharisFoodCornerCard = findViewById(R.id.LocationCharisFoodCornerCard);

        locationEggettePopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationEggettePop.class));
            }
        });

        locationmCafeEconomyRiceStallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationMCafeEconomyRiceStall.class));
            }
        });

        locationcakersInnBakeryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationcakersInnBakery.class));
            }
        });

        locationauntyJuliaCafeteriaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationAuntyJuliaCafeteria.class));
            }
        });

        locationuncleAlanVegetarianCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationLocationUncleAlanVegetarian.class));
            }
        });

        locationcFourNoodlesStallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationcFourNoodlesStall.class));
            }
        });

        locationcharisFoodCornerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Location.this, LocationCharisFoodCorner.class));
            }
        });
    }
}