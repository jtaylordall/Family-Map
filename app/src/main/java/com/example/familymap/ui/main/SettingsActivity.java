package com.example.familymap.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.familymap.R;
import com.example.familymap.data.DataStash;
import com.example.familymap.data.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;
    private DataStash dataStash;

    private Switch lifeSwitch;
    private Switch familySwitch;
    private Switch spouseSwitch;
    private Switch fatherSwitch;
    private Switch motherSwitch;
    private Switch maleSwitch;
    private Switch femaleSwitch;
    private LinearLayout logoutLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsManager = SettingsManager.getInstance();
        dataStash = DataStash.getInstance();

        setContentView(R.layout.activity_settings);

        initViews();
        setSwitchListeners();
        setClickListeners();

    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, SettingsActivity.class);
    }

    private void initViews() {
        lifeSwitch = findViewById(R.id.switch_life);
        familySwitch = findViewById(R.id.switch_family);
        spouseSwitch = findViewById(R.id.switch_spouse);
        fatherSwitch = findViewById(R.id.switch_father);
        motherSwitch = findViewById(R.id.switch_mother);
        maleSwitch = findViewById(R.id.switch_male);
        femaleSwitch = findViewById(R.id.switch_female);
        logoutLinearLayout = findViewById(R.id.logout_linear_layout);

        lifeSwitch.setChecked(settingsManager.isLifeStoryLinesOn());
        familySwitch.setChecked(settingsManager.isFamilyTreeLinesOn());
        spouseSwitch.setChecked(settingsManager.isSpouseLinesOn());
        fatherSwitch.setChecked(settingsManager.isFathersSideOn());
        motherSwitch.setChecked(settingsManager.isMothersSideOn());
        maleSwitch.setChecked(settingsManager.isMaleEventsOn());
        femaleSwitch.setChecked(settingsManager.isFemaleEventsOn());
    }

    private void setSwitchListeners() {
        lifeSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setLifeStoryLinesOn(isChecked);
            }
        });
        familySwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setFamilyTreeLinesOn(isChecked);
            }
        });
        spouseSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setSpouseLinesOn(isChecked);
            }
        });
        fatherSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setFathersSideOn(isChecked);
            }
        });
        motherSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setMothersSideOn(isChecked);
            }
        });
        fatherSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setFathersSideOn(isChecked);
            }
        });
        maleSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setMaleEventsOn(isChecked);
            }
        });
        femaleSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setFemaleEventsOn(isChecked);
            }
        });
    }

    private void setClickListeners() {
        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataStash.setLoggedIn(false);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
