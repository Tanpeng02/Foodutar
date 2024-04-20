package com.example.foodie;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.foodie.R;

public class ThemeFragment extends Fragment {

    private Switch dayModeSwitch;
    private Switch nightModeSwitch;
    private ImageView themeMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        dayModeSwitch = view.findViewById(R.id.dayModeSwitch);
        nightModeSwitch = view.findViewById(R.id.nightModeSwitch);
        themeMode = view.findViewById(R.id.themeMode);

        // Set initial UI state
        int currentMode = getCurrentNightMode();
        Toast.makeText(getContext(), "mode" + currentMode, Toast.LENGTH_SHORT).show();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_NO) {
            dayModeSwitch.setChecked(true);
            updateBackground(currentMode);
        } else {
            nightModeSwitch.setChecked(true);
            updateBackground(currentMode);
        }

        // Set listeners for day and night switches
        dayModeSwitch.setOnCheckedChangeListener(dayCheckedChangeListener);
        nightModeSwitch.setOnCheckedChangeListener(nightCheckedChangeListener);

        // Update the UI to reflect the current theme mode
        updateUI(currentMode);

        return view;
    }

    private int getCurrentNightMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                return AppCompatDelegate.MODE_NIGHT_YES;
            case Configuration.UI_MODE_NIGHT_NO:
                return AppCompatDelegate.MODE_NIGHT_NO;
            default:
                return AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
        }
    }

    private final CompoundButton.OnCheckedChangeListener dayCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                nightModeSwitch.setChecked(false);
            } else if (!dayModeSwitch.isChecked()) {
                // Both switches are off, toggle the night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                nightModeSwitch.setChecked(true);
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener nightCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                dayModeSwitch.setChecked(false);
            } else if (!nightModeSwitch.isChecked()) {
                // Both switches are off, toggle the night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                dayModeSwitch.setChecked(true);
            }
        }
    };

    private void updateUI(int nightMode) {
        // Update the checked state of switches
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            nightModeSwitch.setChecked(true);
            dayModeSwitch.setChecked(false);
        } else {
            dayModeSwitch.setChecked(true);
            nightModeSwitch.setChecked(false);
        }

        // Update the background image based on the current theme mode
        updateBackground(nightMode);
    }

    private void updateBackground(int nightMode) {
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            themeMode.setImageResource(R.drawable.night);
        } else {
            themeMode.setImageResource(R.drawable.day);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the UI to reflect the current theme mode when the fragment is resumed
        int currentMode = getCurrentNightMode();
        updateUI(currentMode);
    }
}
