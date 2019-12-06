package com.example.familymap.ui.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.familymap.R;
import com.example.familymap.model.DataStash;
import com.example.familymap.model.Event;
import com.example.familymap.model.Person;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static android.graphics.Color.BLUE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    final int REQ_CODE_ORDER_INFO = 1;

    private GoogleMap map;
    private TextView markerTextView;
    private DataStash dataStash;

    private FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        frameLayout = view.findViewById(R.id.icon_marker_frame_layout);
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.drawable.ic_launcher_foreground);
        frameLayout.addView(imageView);
        markerTextView = (TextView) view.findViewById(R.id.map_marker_info);

        dataStash = DataStash.getInstance();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                initMap();
            }
        });

        return view;

    }

    static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initMap() {

//        centerMap();
//        zoomMap(10);
//        setMapType();
//        setClickListener();
//        zoomMap(2);
        addMarkers();
//        setBounds();
        setMarkerListener();
//        drawLines();
    }

    void centerMap() {
        LatLng byu = new LatLng(40.2518, -111.6493);
        CameraUpdate update = CameraUpdateFactory.newLatLng(byu);
        map.moveCamera(update);
        map.addMarker(new MarkerOptions().position(byu));
    }

    void zoomMap(float amount) {
        CameraUpdate update = CameraUpdateFactory.zoomTo(amount);
        map.moveCamera(update);
    }

    //    static final int mapType = MAP_TYPE_NORMAL;
    static final int mapType = MAP_TYPE_SATELLITE;

    void setMapType() {
        map.setMapType(mapType);
    }

    void setClickListener() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                markerTextView.setText(latLng.toString());
            }
        });
    }

    void setPersonClickListener() {
        markerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PersonActivity.newIntent(getContext());
                startActivityForResult(intent, REQ_CODE_ORDER_INFO);

            }
        });
    }

    void addMarkers() {
        Event[] events = dataStash.getEvents().getEvents();
        Map<String, Event> eventMap = new TreeMap<>();
        for (Event event : events) {
            String city = event.getCity();
            if (eventMap.containsKey(city)) {
                Event conflictingEvent = eventMap.get(city);
                if (conflictingEvent.getYear() < event.getYear()) {
                    conflictingEvent = event;
                }
            } else {
                eventMap.put(city, event);
            }
        }
        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            Event event = entry.getValue();
            addMarker(event,
                    new LatLng(event.getLatitude(), event.getLongitude()),
                    getColor(event.getEventType()));
        }
    }

    /*
    work on getting more varied colors, perhaps by incrementing instead of generating a random color
     */
    private int getColor(String eventType) {
        int out;
        Map<String, Integer> eventColors = dataStash.getEventColors();
        if (!eventColors.containsKey(eventType) || eventColors.get(eventType) == null) {
            Random rand = new Random();
            do {
                int r = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);

                out = Color.rgb(r, g, b);
            } while (eventColors.containsValue(out));
            eventColors.put(eventType, out);

        } else {
            out = eventColors.get(eventType);
        }
        return out;
    }

    private void addMarker(Event event, LatLng latLng, int colorInt) {
        MarkerOptions options =
                new MarkerOptions().position(latLng).title(event.getCity())
                        .icon(generateBitmapDescriptorMarker(colorInt));

        Marker marker = map.addMarker(options);
        marker.setTag(event);
    }

    void setBounds() {
        Event[] events = dataStash.getEvents().getEvents();
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Event event : events) {
            builder.include(new LatLng(event.getLatitude(), event.getLongitude()));
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate update =
                CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 5);
        map.moveCamera(update);
    }

    private void setMarkerListener() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                setPersonClickListener();
                Event event = (Event) marker.getTag();
                Person person = dataStash.getPersonMap().get(event.getPersonID());
                dataStash.setActivePerson(person);
                String markerData = person.getFirstName() + " " +
                        person.getLastName() + "\n" +
                        event.getEventType().toUpperCase() + ": " +
                        event.getCity() + ", " +
                        event.getCountry() +
                        " (" + event.getYear() + ")";
                markerTextView.setText(markerData);
                setGenderIcon(person.getGender());
                return false;
            }
        });
    }

    void drawLines() {
        LatLng lastCity = null;
        Event[] events = dataStash.getEvents().getEvents();
        for (Event event : events) {
            LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
            if (lastCity != null)
                drawLine(lastCity, latLng);
            lastCity = latLng;
        }
    }

    static final float WIDTH = 10;  // in pixels
    static final int color = BLUE;
//    static final int color = BLACK;

    private void drawLine(LatLng point1, LatLng point2) {
        PolylineOptions options =
                new PolylineOptions().add(point1, point2)
                        .color(color).width(WIDTH);
        map.addPolyline(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private void setGenderIcon(String gender) {
        ImageView imageView = new ImageView(getContext());
        if ("m".equals(gender)) {
            imageView.setBackgroundResource(R.drawable.icon_male1);
        } else {
            imageView.setBackgroundResource(R.drawable.icon_female1);
        }
        frameLayout.removeAllViews();
        frameLayout.setPadding(80, 52, 80, 52);
        frameLayout.addView(imageView);
    }

    private BitmapDescriptor generateBitmapDescriptorMarker(int color) {

        Drawable drawable = new IconDrawable(getActivity(),
                FontAwesomeIcons.fa_map_marker).color(color).sizeDp(40);

        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
