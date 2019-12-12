package com.example.familymap.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.familymap.R;
import com.example.familymap.data.DataStash;
import com.example.familymap.data.SettingsManager;
import com.example.familymap.model.Event;
import com.example.familymap.model.Person;
import com.example.familymap.support.EventFinder;
import com.example.familymap.support.FamilyFinder;
import com.example.familymap.support.Filter;
import com.example.familymap.support.Search;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import static com.example.familymap.data.Values.*;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private TextView markerTextView;
    private DataStash dataStash;
    private SettingsManager settingsManager;
    private Person activePerson;
    private Event event;
    private FrameLayout frameLayout;
    private Vector<Polyline> polylines;
    private boolean inEventActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        polylines = new Vector<>();
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        frameLayout = view.findViewById(R.id.icon_marker_frame_layout);
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.drawable.ic_launcher_foreground);

        frameLayout.addView(imageView);
        markerTextView = view.findViewById(R.id.map_marker_info);
        if (getActivity().getClass() == MainActivity.class) {
            ((MainActivity) getActivity()).createMenu();
        }
        dataStash = DataStash.getInstance();
        settingsManager = SettingsManager.getInstance();
        inEventActivity = false;

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

        Event[] events = dataStash.getEventWrapper().getEvents();
        events = filter(events);
        centerMap(events);
//        zoomMap(10);
//        setMapType();
//        setClickListener();
//        zoomMap(2);
        addMarkers(events);
        //assessLineSettings(events);
//        setBounds();
        setMarkerListener(events);
//        drawLines();
    }

    private void centerMap(Event[] events) {
        if (this.getArguments().containsKey("eventID") ||
                dataStash.getSelectedEventInMainActivity() != null) {
            Event event;
            if (this.getArguments().containsKey("eventID")) {
                event = new EventFinder()
                        .findEvent(this.getArguments().getString("eventID"));
                inEventActivity = true;
            } else {
                inEventActivity = false;
                event = dataStash.getSelectedEventInMainActivity();
            }
            //Log.d("EVENT", event.toString());

            LatLng center = new LatLng(event.getLatitude(), event.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLng(center);
            map.moveCamera(update);
            if (new Search().eventPassesFilters(event)) {
                MarkerOptions options =
                        new MarkerOptions().position(center).title(event.getCity())
                                .icon(generateBitmapDescriptorMarker(dataStash.getEventColor(event.getEventType())));
                Marker marker = map.addMarker(options);
                marker.setTag(event);

                setMarkerListener(events);
                assessLineSettings(events, event);
                Person person = dataStash.getPersonMap().get(event.getPersonID());
                activePerson = person;
                String markerData = getMarkerData(person, event);
                markerTextView.setText(markerData);
                setGenderIcon(person.getGender());
                setPersonClickListener();
                zoomMap(WIDTH);
            }
        }
    }

    private void zoomMap(float amount) {
        CameraUpdate update = CameraUpdateFactory.zoomTo(1);
        map.moveCamera(update);
    }

    void setMapType() {
        map.setMapType(mapType2);
    }

    private void setPersonClickListener() {
        markerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PersonActivity.newIntent(getContext());
                intent.putExtra("personID", activePerson.getPersonID());
                Log.d("Map to Person tag", activePerson.toString());
                startActivityForResult(intent, REQ_CODE_ORDER_INFO);

            }
        });
    }

    private void setMarkerListener(final Event[] events) {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearPolylines();
                zoomMap(WIDTH);
                Event event = (Event) marker.getTag();
                if (!inEventActivity) {
                    dataStash.setSelectedEventInMainActivity(event);
                }
                Person person = dataStash.getPersonMap().get(event.getPersonID());
                //dataStash.setActivePerson(person);
                activePerson = person;
                String markerData = getMarkerData(person, event);
                markerTextView.setText(markerData);
                setGenderIcon(person.getGender());
                setPersonClickListener();
                assessLineSettings(events, event);
                return false;
            }
        });
    }

    private void setMarkerListener() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                zoomMap(WIDTH);
                clearPolylines();
                Event event = (Event) marker.getTag();
                Person person = dataStash.getPersonMap().get(event.getPersonID());
                //dataStash.setActivePerson(person);
                activePerson = person;
                String markerData = getMarkerData(person, event);
                markerTextView.setText(markerData);
                setGenderIcon(person.getGender());
                setPersonClickListener();
                return false;
            }
        });
    }

    private void addMarkers(Event[] events) {
        Map<String, Event> eventMap = new TreeMap<>();
        for (Event event : events) {
            String id = (event.getCity() + event.getLatitude() + event.getLongitude());
            if (eventMap.containsKey(id)) {
                Event conflictingEvent = eventMap.get(id);
                if (conflictingEvent.getYear() < event.getYear()) {
                    conflictingEvent = event;
                }
            } else {
                eventMap.put(id, event);
            }
        }
        for (Map.Entry<String, Event> entry : eventMap.entrySet()) {
            Event event = entry.getValue();
            if (!event.equals(this.event)) {
                addMarker(event,
                        new LatLng(event.getLatitude(), event.getLongitude()),
                        getColor(event.getEventType()));
            }
        }
    }

    private void addMarker(Event event, LatLng latLng, int colorInt) {
        MarkerOptions options =
                new MarkerOptions().position(latLng).title(event.getCity())
                        .icon(generateBitmapDescriptorMarker(colorInt));

        Marker marker = map.addMarker(options);
        marker.setTag(event);
    }

    /*
    work on getting more varied colors, perhaps by incrementing instead of generating a random color
    */
    private int getColor(String eventType) {
        int out;
        Map<String, Integer> eventColors = dataStash.getEventColors();
        if (!eventColors.containsKey(eventType.toLowerCase()) ||
                eventColors.get(eventType.toLowerCase()) == null) {
            do {
                out = getRandColor();
            } while (eventColors.containsValue(out));
            eventColors.put(eventType.toLowerCase(), out);

        } else {
            out = eventColors.get(eventType.toLowerCase());
        }
        return out;
    }

    void setBounds() {
        Event[] events = dataStash.getEventWrapper().getEvents();
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Event event : events) {
            builder.include(new LatLng(event.getLatitude(), event.getLongitude()));
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate update =
                CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 5);
        map.moveCamera(update);
    }

    void drawFamilyTreeLines(Event[] events, Event event) {
        SortedSet<Event> eventSet = new TreeSet<>();
        Person person = new FamilyFinder().findPerson(event.getPersonID());
        Collections.addAll(eventSet, events);
        int color = getRandColor();
        drawParentLine(event, eventSet, WIDTH, color);
    }

    private void drawParentLine(Event event, SortedSet<Event> eventSet, float width, int color) {
        FamilyFinder familyFinder = new FamilyFinder();
        EventFinder eventFinder = new EventFinder();
        Person person = familyFinder.findPerson(event.getPersonID());
        Person father = familyFinder.findPerson(person.getFatherID());
        Person mother = familyFinder.findPerson(person.getMotherID());
        if (father != null) {
            Event[] fEvents = eventFinder.getPersonEvents(father.getPersonID());
            if (fEvents.length > 0) {
                Event fEvent = fEvents[0];
                if (eventSet.contains(fEvent)) {
                    drawRelationshipLine(event, father, width, color);
                    drawParentLine(fEvent, eventSet, width / 2, color);
                }
            }
        }
        if (mother != null) {
            Event[] mEvents = eventFinder.getPersonEvents(mother.getPersonID());
            if (mEvents.length > 0) {
                Event mEvent = mEvents[0];
                if (eventSet.contains(mEvent)) {
                    drawRelationshipLine(event, mother, width, color);
                    drawParentLine(mEvent, eventSet, width - 3, color);
                }
            }
        }
    }

    private void drawLifeLines(Event[] events, Event event) {
        SortedSet<String> ids = new TreeSet<>();
        drawLifeLine(event.getPersonID());
    }

    private void drawLifeLine(String personID) {
        LatLng lastCity = null;
        Event[] events = new EventFinder().getPersonEvents(personID);
        int color = dataStash.getEventColor("birth");
        for (Event event : events) {
            LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
            if (lastCity != null)
                drawLine(lastCity, latLng, WIDTH, color);
            lastCity = latLng;
        }
    }

    private void drawSpouseLines(Event[] events, Event event) {
        Person person = new FamilyFinder().findPerson(event.getPersonID());
        if (!"".equals(person.getSpouseID()) && person.getSpouseID() != null) {
            Person spouse = new FamilyFinder().findPerson(person.getSpouseID());
            boolean passes = new Search().personPassesFilters(spouse);
            if (spouse != null && passes) {
                drawRelationshipLine(event, spouse, WIDTH,
                        dataStash.getEventColor("marriage"));
            }
        }
    }

    private void drawRelationshipLine(Event hEvent, Person wife, float width, int color) {
        //Event hEvent = new EventFinder().getPersonEvents(husband.getPersonID())[0];
        Event wEvent = new EventFinder().getPersonEvents(wife.getPersonID())[0];
        if (hEvent != null && wEvent != null) {
            LatLng latLng1 = new LatLng(hEvent.getLatitude(), hEvent.getLongitude());
            LatLng latLng2 = new LatLng(wEvent.getLatitude(), wEvent.getLongitude());
            drawLine(latLng1, latLng2, width, color);
        }
    }

    private void drawLine(LatLng point1, LatLng point2, float width, int color) {
        PolylineOptions options =
                new PolylineOptions().add(point1, point2)
                        .color(color).width(width);

        polylines.add(map.addPolyline(options));
    }

    private void clearPolylines() {
        for (Polyline p : polylines) {
            p.remove();
        }
        polylines.clear();

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

    private String getMarkerData(Person person, Event event) {
        if (person != null && event != null) {
            return person.getFirstName() + " " +
                    person.getLastName() + "\n" +
                    event.getEventType().toUpperCase() + ": " +
                    event.getCity() + ", " +
                    event.getCountry() +
                    " (" + event.getYear() + ")";
        } else {
            return "";
        }
    }

    private void assessLineSettings(Event[] events, Event event) {
        clearPolylines();
        if (settingsManager.isSpouseLinesOn() &&
                settingsManager.isMaleEventsOn() &&
                settingsManager.isFemaleEventsOn()) {
            drawSpouseLines(events, event);
        }
        if (settingsManager.isLifeStoryLinesOn()) {
            drawLifeLines(events, event);
        }
        if (settingsManager.isFamilyTreeLinesOn()) {
            drawFamilyTreeLines(events, event);
        }
    }

    public Event[] filter(Event[] events) {
        Filter filter = new Filter();

        if (settingsManager.isFathersSideOn() && !settingsManager.isMothersSideOn()) {
            events = filter.filterFathersSide(events);
        } else if (settingsManager.isMothersSideOn() && !settingsManager.isFathersSideOn()) {
            events = filter.filterMothersSide(events);
        } else if (!settingsManager.isFathersSideOn() && !settingsManager.isMothersSideOn()) {
            Person person = new FamilyFinder().findPerson(dataStash.getActivePerson().getPersonID());
            Event[] events1 = new EventFinder().getPersonEvents(person.getPersonID());
            Event[] events2 = new EventFinder().getPersonEvents(person.getSpouseID());
            events = combineArrays(events1, events2);
        }

        if (settingsManager.isMaleEventsOn() && !settingsManager.isFemaleEventsOn()) {
            events = filter.filterMales(events);
        } else if (settingsManager.isFemaleEventsOn() && !settingsManager.isMaleEventsOn()) {
            events = filter.filterFemales(events);
        } else if (!settingsManager.isMaleEventsOn() && !settingsManager.isFemaleEventsOn()) {
            events = new Event[]{};
        }

        return events;
    }

    private Event[] combineArrays(Event[] events1, Event[] events2) {
        Event[] outEvents = new Event[events1.length + events2.length];
        int i = 0;
        for (Event item : events1) {
            outEvents[i] = item;
            i++;
        }
        for (Event value : events2) {
            outEvents[i] = value;
        }
        return outEvents;
    }

    private int getRandColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return Color.rgb(r, g, b);
    }
}
