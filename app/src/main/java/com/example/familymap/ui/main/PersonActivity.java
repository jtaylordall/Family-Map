package com.example.familymap.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.R;
import com.example.familymap.model.DataStash;
import com.example.familymap.model.Event;
import com.example.familymap.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Map;

public class PersonActivity extends AppCompatActivity {

    final private int ISNT_FAMILY = 0;
    final private int IS_FATHER = 1;
    final private int IS_MOTHER = 2;
    final private int IS_SPOUSE = 3;
    final private int IS_CHILD = 4;

    private Person activePerson;
    private DataStash dataStash;
    private RecyclerView recyclerView_lifeEvents;
    private RecyclerView recyclerView_family;
    private boolean lifeEventsOn;
    private boolean familyOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        lifeEventsOn = false;
        familyOn = false;

        dataStash = DataStash.getInstance();
        activePerson = dataStash.getActivePerson();

        TextView textViewFirstName = findViewById(R.id.person_fname);
        TextView textViewLastName = findViewById(R.id.person_lname);
        TextView textViewGender = findViewById(R.id.person_gender);
        TextView textViewLifeEvents = findViewById(R.id.text_view_life_events);
        TextView textViewFamily = findViewById(R.id.text_view_family);

        textViewFirstName.setText(activePerson.getFirstName().toUpperCase());
        textViewLastName.setText(activePerson.getLastName().toUpperCase());

        String gender;
        if ("m".equals(activePerson.getGender())) {
            gender = "MALE";
        } else {
            gender = "FEMALE";
        }
        textViewGender.setText(gender);

        recyclerView_lifeEvents = findViewById(R.id.recycler_view_events);
        recyclerView_lifeEvents.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_family = findViewById(R.id.recycler_view_family);
        recyclerView_family.setLayoutManager(new LinearLayoutManager(this));

        textViewLifeEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lifeEventsOn = !lifeEventsOn;
                initLifeEventsUI();
            }
        });

        textViewFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyOn = !familyOn;
                initFamilyUI();
            }
        });

    }

    private void initLifeEventsUI() {
        EventAdapter eventAdapter;
        if (lifeEventsOn) {
            eventAdapter = new EventAdapter(this,
                    dataStash.getPersonEvents(activePerson.getPersonID()));
        } else {
            eventAdapter = new EventAdapter(this,
                    dataStash.getPersonEvents(""));
        }
        recyclerView_lifeEvents.setAdapter(eventAdapter);
    }

    private void initFamilyUI() {

        FamAdapter famAdapter;
        if (familyOn) {
            famAdapter = new FamAdapter(this,
                    dataStash.getPersonFamily(activePerson.getPersonID()));
        } else {
            famAdapter = new FamAdapter(this,
                    dataStash.getPersonFamily(""));
        }
        recyclerView_family.setAdapter(famAdapter);
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, PersonActivity.class);
    }

    class EventAdapter extends RecyclerView.Adapter<EventHolder> {

        private Event[] events;
        private LayoutInflater inflater;

        EventAdapter(Context context, Event[] events) {
            this.events = events;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            holder.bind(events[position]);
        }

        @Override
        public int getItemCount() {
            return events.length;
        }

    }

    class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView eventIcon;
        private TextView eventInfo;
        private TextView eventPersonName;
        private Event event;
        private FrameLayout frameLayout;

        EventHolder(View view) {
            super(view);
            frameLayout = view.findViewById(R.id.list_item_icon);
            eventInfo = view.findViewById(R.id.list_item_info);
            eventPersonName = view.findViewById(R.id.list_item_name);
        }

        void bind(Event event) {
            this.event = event;

            Drawable drawable = new IconDrawable(getApplicationContext(),
                    FontAwesomeIcons.fa_map_marker)
                    .color(dataStash.getEventColor(event.getEventType()))
                    .sizeDp(40);
            eventIcon = new ImageView(getApplicationContext());
            eventIcon.setImageDrawable(drawable);
            eventIcon.setPadding(10, 10, 10, 10);

            String info = event.getEventType().toUpperCase() + ": " +
                    event.getCity() + ", " + event.getCountry() +
                    " (" + event.getYear() + ")";
            eventInfo.setText(info);

            frameLayout.addView(eventIcon);

            String name = activePerson.getFirstName() + " " + activePerson.getLastName();
            eventPersonName.setText(name);
        }

        @Override
        public void onClick(View view) {
            /*if (checkBox.isChecked())
                selected.add(item);
            else
                selected.remove(item);
            textView.setText(selected.toString());*/
        }

    }


    class FamAdapter extends RecyclerView.Adapter<FamHolder> {

        private Map<Integer, Person> family;
        private Person[] familyArray;
        private LayoutInflater inflater;

        FamAdapter(Context context, Map<Integer, Person> family) {
            this.family = family;
            familyArray = new Person[family.size()];
            int a = 0;
            for (Map.Entry<Integer, Person> e : family.entrySet()) {
                Person p = e.getValue();
                familyArray[a] = p;
                a++;
            }
            inflater = LayoutInflater.from(context);
        }

        @Override
        public FamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new FamHolder(view);
        }

        @Override
        public void onBindViewHolder(FamHolder holder, int position) {
            System.out.println(familyArray);
            Person person = familyArray[position];
            int relationship = 0;
            for (Map.Entry<Integer, Person> e : family.entrySet()) {
                Person p = e.getValue();
                if (p.getPersonID().equals(person.getPersonID())) {
                    relationship = e.getKey();
                }
            }
            holder.bind(relationship, person);
        }

        @Override
        public int getItemCount() {
            return family.size();
        }

    }

    class FamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView personIcon;
        private TextView personInfo;
        private TextView personName;
        private Person person;
        private FrameLayout frameLayout;

        FamHolder(View view) {
            super(view);
            frameLayout = view.findViewById(R.id.list_item_icon);
            personName = view.findViewById(R.id.list_item_info);
            personInfo = view.findViewById(R.id.list_item_name);
        }

        void bind(int relationship, Person person) {
            this.person = person;

            personIcon = new ImageView(getApplicationContext());
            if ("m".equals(person.getGender())) {
                personIcon.setImageResource(R.drawable.icon_male1);
            } else {
                personIcon.setImageResource(R.drawable.icon_female1);
            }
            personIcon.setPadding(10, 10, 10, 10);

            frameLayout.addView(personIcon);

            String name = person.getFirstName() + " " + person.getLastName();
            personName.setText(name);

            String info;

            switch (relationship) {
                case IS_FATHER:
                    info = "FATHER";
                    break;
                case IS_MOTHER:
                    info = "MOTHER";
                    break;
                case IS_SPOUSE:
                    info = "SPOUSE";
                    break;
                case IS_CHILD:
                    info = "CHILD";
                    break;
                default:
                    info = "SIBLING";
            }

            personInfo.setText(info);
        }

        @Override
        public void onClick(View view) {
            /*if (checkBox.isChecked())
                selected.add(item);
            else
                selected.remove(item);
            textView.setText(selected.toString());*/
        }

    }

}
