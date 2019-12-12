package com.example.familymap.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymap.R;
import com.example.familymap.data.DataStash;
import com.example.familymap.model.Event;
import com.example.familymap.model.Person;
import com.example.familymap.support.FamilyFinder;
import com.example.familymap.support.Search;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import static com.example.familymap.data.Values.REQ_CODE_ORDER_INFO;

public class SearchActivity extends AppCompatActivity {

    //private SearchView searchView;
    private EditText editText;
    private RecyclerView recyclerView;
    private Context searchContext;
    private DataStash dataStash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchContext = this;
        dataStash = DataStash.getInstance();
        initViews();
        setListeners();
    }

    private void initViews() {
        editText = findViewById(R.id.edit_search);
        recyclerView = findViewById(R.id.recycler_view_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initUI(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initUI(String input) {
        Object[] searchItems;
        if ("".equals(input)) {
            searchItems = new Object[]{};
        } else {
            Search search = new Search();
            searchItems = search.search(input);
        }
        Adapter adapter = new Adapter(this, searchItems);
        recyclerView.setAdapter(adapter);
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, SearchActivity.class);
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        private Object[] searchItems;
        private LayoutInflater inflater;

        Adapter(Context context, Object[] searchItems) {
            this.searchItems = searchItems;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item,
                    parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(searchItems[position]);
        }

        @Override
        public int getItemCount() {
            return searchItems.length;
        }
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView info;
        private TextView description;
        private Object object;
        private FrameLayout frameLayout;

        Holder(@NonNull View view) {
            super(view);
            frameLayout = view.findViewById(R.id.list_item_icon);
            info = view.findViewById(R.id.list_item_info);
            description = view.findViewById(R.id.list_item_name);

            LinearLayout listItem = view.findViewById(R.id.list_item_layout);
            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (object.getClass() == Event.class) {
                        intent = EventActivity.newIntent(searchContext);
                        intent.putExtra("eventID",
                                ((Event) object).getEventID());
                        startActivityForResult(intent, REQ_CODE_ORDER_INFO);
                    } else if (object.getClass() == Person.class) {
                        intent = PersonActivity.newIntent(searchContext);
                        intent.putExtra("personID",
                                ((Person) object).getPersonID());
                        startActivityForResult(intent, REQ_CODE_ORDER_INFO);
                    }
                }
            });


        }

        void bind(Object object) {
            this.object = object;
            String infoText;
            ImageView imageView;

            if (object.getClass() == Person.class) {
                Person person = (Person) object;
                infoText = getNameFirstLast(person);

                imageView = new ImageView(getApplicationContext());
                if ("m".equals(person.getGender())) {
                    imageView.setImageResource(R.drawable.icon_male1);
                } else {
                    imageView.setImageResource(R.drawable.icon_female1);
                }
                imageView.setPadding(10, 10, 10, 10);

                frameLayout.addView(imageView);
                this.info.setText(infoText);

            } else if (object.getClass() == Event.class) {
                Event event = (Event) object;

                infoText = event.getEventType().toUpperCase() + ": " +
                        event.getCity() + ", " + event.getCountry() +
                        " (" + event.getYear() + ")";

                Person eventPerson = new FamilyFinder().findPerson(event.getPersonID());
                String descriptionText = getNameFirstLast(eventPerson);

                Drawable drawable = new IconDrawable(getApplicationContext(),
                        FontAwesomeIcons.fa_map_marker)
                        .color(dataStash.getEventColor(event.getEventType().toLowerCase()))
                        .sizeDp(40);
                imageView = new ImageView(searchContext);
                imageView.setImageDrawable(drawable);

                frameLayout.addView(imageView);
                this.info.setText(infoText);
                this.description.setText(descriptionText);


            }
        }

        @Override
        public void onClick(View v) {

        }
    }

    private String getNameFirstLast(Person person) {
        return person.getFirstName() + " " + person.getLastName();
    }
}
