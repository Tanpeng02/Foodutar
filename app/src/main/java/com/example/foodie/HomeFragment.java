package com.example.foodie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private CardView foodStallCard, locationCard, discussionCommunityCard, feedbackCard;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getActivity();

        // Initialize the CardViews
        foodStallCard = view.findViewById(R.id.FoodStallCard);
        locationCard = view.findViewById(R.id.LocationCard);
        discussionCommunityCard = view.findViewById(R.id.DiscussionCommunityCard);
        feedbackCard = view.findViewById(R.id.FeedbackCard);

        // Set click listeners for the CardViews
        foodStallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FoodStallActivity when the FoodStallCard is clicked
                startActivity(new Intent(context, FoodStall.class));
            }
        });

        discussionCommunityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the DiscussionCommunityActivity when the DiscussionCommunityCard is clicked
                startActivity(new Intent(context, DiscussionCommunity.class));
            }
        });

        locationCard.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                // Start the LocationActivity when the LocationCard is clicked
                startActivity(new Intent(context, Location.class));
            }
        });

        feedbackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the FeedbackActivity when the FeedbackCard is clicked
                startActivity(new Intent(context, Feedback.class));
            }
        });

        return view;
    }
}
