package com.example.familymap.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.model.Event;
import com.example.familymap.support.EventFinder;

public class EventActivity extends AppCompatActivity {

    private MapFragment mapFragment;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FrameLayout frameLayout = findViewById(R.id.main_activity_frame_layout);

        String eventID = getIntent().getStringExtra("eventID");
        event = new EventFinder().findEvent(eventID);

        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("eventID", eventID);
            mapFragment.setArguments(bundle);
            fragmentManager.beginTransaction().add(R.id.main_activity_frame_layout, mapFragment).commit();
        }

    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, EventActivity.class);
    }
}
