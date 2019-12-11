package com.example.familymap.ui.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymap.R;
import com.example.familymap.model.AuthToken;
import com.example.familymap.data.DataStash;
import com.example.familymap.model.EventWrapper;
import com.example.familymap.proxy.LoginRequest;
import com.example.familymap.model.PersonWrapper;
import com.example.familymap.model.Person;
import com.example.familymap.proxy.RegisterRequest;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static com.example.familymap.ui.main.MainActivity.readString;

public class RegisterFragment extends Fragment {

    private EditText hostEditText;
    private EditText portEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private RadioGroup genderRadioGroup;
    private String gender;

    private Button regButton;
    private Button logButton;
    private DataStash dataStash;
    private String message;
    private Context regFragContext;


    public RegisterFragment() {
        // Required empty public constructor
    }

    static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gender = "m";
        dataStash = DataStash.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        regFragContext = getContext();
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        initViews(view);
        setTextListeners();
        setClickListeners();

        setLoginFields(); // auto fill fields for debugging
        return view;
    }

    private void initViews(View view) {
        hostEditText = view.findViewById(R.id.serverHost_enter);
        portEditText = view.findViewById(R.id.serverPort_enter);
        usernameEditText = view.findViewById(R.id.username_enter);
        passwordEditText = view.findViewById(R.id.password_enter);
        firstNameEditText = view.findViewById(R.id.firstName_enter);
        lastNameEditText = view.findViewById(R.id.lastName_enter);
        emailEditText = view.findViewById(R.id.email_enter);
        genderRadioGroup = view.findViewById(R.id.genderRadio);

        regButton = view.findViewById(R.id.registerButton);
        regButton.setEnabled(false);
        logButton = view.findViewById(R.id.loginButton);
        logButton.setEnabled(false);
    }

    private void setTextListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!hostEditText.getText().toString().equals("") &&
                        !portEditText.getText().toString().equals("") &&
                        !usernameEditText.getText().toString().equals("") &&
                        !passwordEditText.getText().toString().equals("")) {
                    logButton.setEnabled(true);
                    if (!firstNameEditText.getText().toString().equals("") &&
                            !lastNameEditText.getText().toString().equals("") &&
                            !emailEditText.getText().toString().equals("")) {
                        regButton.setEnabled(true);
                    } else {
                        regButton.setEnabled(false);
                    }
                } else {
                    logButton.setEnabled(false);
                    regButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        hostEditText.addTextChangedListener(textWatcher);
        portEditText.addTextChangedListener(textWatcher);
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        firstNameEditText.addTextChangedListener(textWatcher);
        lastNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
    }

    private void setClickListeners() {
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleRadio) {
                    gender = "m";
                } else if (checkedId == R.id.femaleRadio) {
                    gender = "f";
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regButton.setEnabled(false);
                logButton.setEnabled(false);
                onRegButtonClicked();
            }
        });
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regButton.setEnabled(false);
                logButton.setEnabled(false);
                onLogButtonClicked();
            }
        });
    }

    private RegisterRequest getRegRequest() {
        return new RegisterRequest(usernameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                emailEditText.getText().toString(),
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                gender,
                null,
                null,
                null);
    }

    private void onLogButtonClicked() {

        final LoginRequest loginRequest = getLogRequest();
        dataStash.setHost(hostEditText.getText().toString());
        dataStash.setPort(portEditText.getText().toString());

        String link = "http://" + dataStash.getHost() + ":" +
                dataStash.getPort() + "/user/login";
        String reqData = new Gson().toJson(loginRequest);

        message = "Successfully logged in ";

        Task task = new Task();
        task.execute(link, reqData);

        dataStash = DataStash.getInstance(); // delete later
    }

    private void onRegButtonClicked() {

        final RegisterRequest registerRequest = getRegRequest();

        dataStash.setHost(hostEditText.getText().toString());
        dataStash.setPort(portEditText.getText().toString());

        String link = "http://" + dataStash.getHost() + ":" +
                dataStash.getPort() + "/user/register";
        String reqData = new Gson().toJson(registerRequest);

        Task task = new Task();
        task.execute(link, reqData);

        message = "Successfully registered ";

        dataStash = DataStash.getInstance(); // delete later
    }

    protected class Task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] strings) {
            String response = "";
            try {
                String link = strings[0];
                String reqData = strings[1];

                URL url = new URL(link);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.connect();

                OutputStream reqBody = http.getOutputStream();
                MainActivity.writeString(reqData, reqBody);
                reqBody.close();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    Scanner in = new Scanner(http.getInputStream());
                    while (in.hasNextLine())
                        response += in.nextLine() + "\n";
                    in.close();

                } else {
                    System.out.println("ERROR: " + http.getResponseMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.contains("Error")) {
                dataStash.setAuthToken(new Gson().fromJson(result, AuthToken.class));

                PersonTask personTask = new PersonTask();
                personTask.execute();
            } else {
                showToast(new Gson().fromJson(result, MainActivity.Message.class).getMessage());
            }
        }
    }

    protected class PersonTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                String link = "http://" + dataStash.getHost() + ":" +
                        dataStash.getPort() + "/person/" +
                        dataStash.getAuthToken().getPersonID();

                URL url = new URL(link);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.setRequestProperty("Authorization", dataStash.getAuthToken()
                        .getAuthTokenID());
                http.connect();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    response = readString(http.getInputStream());

                } else {
                    System.out.println("ERROR: " + http.getResponseMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.contains("Error")) {
                dataStash.setActivePerson(new Gson().fromJson(result, Person.class));
                FamilyTask familyTask = new FamilyTask();
                familyTask.execute();
            } else {
                showToast(new Gson().fromJson(result, MainActivity.Message.class).getMessage());
            }
        }
    }

    protected class FamilyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                String link = "http://" + dataStash.getHost() + ":" +
                        dataStash.getPort() + "/person";

                URL url = new URL(link);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.setRequestProperty("Authorization", dataStash.getAuthToken()
                        .getAuthTokenID());
                http.connect();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    response = readString(http.getInputStream());

                } else {
                    System.out.println("ERROR: " + http.getResponseMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.contains("Error")) {
                dataStash.setPersonWrapper(new Gson().fromJson(result, PersonWrapper.class));
                EventsTask eventsTask = new EventsTask();
                eventsTask.execute();
            } else {
                showToast(new Gson().fromJson(result, MainActivity.Message.class).getMessage());
            }
        }
    }

    protected class EventsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                String link = "http://" + dataStash.getHost() + ":" +
                        dataStash.getPort() + "/event";
                URL url = new URL(link);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.setRequestProperty("Authorization", dataStash.getAuthToken().getAuthTokenID());
                http.connect();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    response = readString(http.getInputStream());
                } else {
                    System.out.println("ERROR: " + http.getResponseMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.contains("Error")) {
                dataStash.setEventWrapper(new Gson().fromJson(result, EventWrapper.class));
                Person person = dataStash.getActivePerson();
                showToast(message + person.getFirstName() + " " + person.getLastName());
                dataStash.setLoggedIn(true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_frame_layout, MapFragment.newInstance(), "Map").commit();
            } else {
                showToast(new Gson().fromJson(result, MainActivity.Message.class).getMessage());
            }
        }
    }

    private LoginRequest getLogRequest() {
        return new LoginRequest(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    private void showToast(String message) {
        Toast.makeText(regFragContext, message, Toast.LENGTH_SHORT).show();
    }

    // auto fill fields for debugging
    private void setLoginFields() {
        hostEditText.setText("10.0.2.2");
        portEditText.setText("8080");
        usernameEditText.setText("sheila");
        passwordEditText.setText("parker");
    }
}
