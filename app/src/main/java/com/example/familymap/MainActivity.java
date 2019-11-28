package com.example.familymap;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familymap.model.AuthToken;
import com.example.familymap.model.DataStash;
import com.example.familymap.model.Events;
import com.example.familymap.model.LoginRequest;
import com.example.familymap.model.People;
import com.example.familymap.model.RegisterRequest;
import com.example.familymap.model.Person;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends FragmentActivity {

    private RegisterFragment registerFragment;
    private EditText hostEditText;
    private EditText portEditText;
    private DataStash dataStash;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dataStash = DataStash.getInstance();
        setContentView(R.layout.main_activity);
        Button logButton = findViewById(R.id.loginButton);
        Button regButton = findViewById(R.id.registerButton);
        hostEditText = findViewById(R.id.serverHost_enter);
        portEditText = findViewById(R.id.serverPort_enter);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegButtonClicked();
            }
        });
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogButtonClicked();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        registerFragment =
                (RegisterFragment) fragmentManager.findFragmentById(R.id.fragment_reg);

        if (registerFragment == null) {
            registerFragment = RegisterFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.fragment_reg, registerFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onLogButtonClicked() {

        final LoginRequest loginRequest = registerFragment.getLogRequest();
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

        final RegisterRequest registerRequest = registerFragment.getRegRequest();

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
                writeString(reqData, reqBody);
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
                showToast(new Gson().fromJson(result, Message.class).getMessage());
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
                dataStash.setPerson(new Gson().fromJson(result, Person.class));
                FamilyTask familyTask = new FamilyTask();
                familyTask.execute();
            } else {
                showToast(new Gson().fromJson(result, Message.class).getMessage());
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
                dataStash.setPeople(new Gson().fromJson(result, People.class));
                EventsTask eventsTask = new EventsTask();
                eventsTask.execute();
            } else {
                showToast(new Gson().fromJson(result, Message.class).getMessage());
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
                dataStash.setEvents(new Gson().fromJson(result, Events.class));
                Person person = dataStash.getPerson();
                showToast(message + person.getFirstName() + " " + person.getLastName());
            } else {
                showToast(new Gson().fromJson(result, Message.class).getMessage());
            }
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private class Message {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}




