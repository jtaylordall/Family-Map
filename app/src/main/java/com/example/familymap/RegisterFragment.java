package com.example.familymap;

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

import com.example.familymap.model.LoginRequest;
import com.example.familymap.model.RegisterRequest;

public class RegisterFragment extends Fragment {

    private EditText hostEditText;
    private EditText portEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private String gender;

    private Button regButton;
    private Button logButton;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        hostEditText = view.findViewById(R.id.serverHost_enter);
        portEditText = view.findViewById(R.id.serverPort_enter);
        usernameEditText = view.findViewById(R.id.username_enter);
        passwordEditText = view.findViewById(R.id.password_enter);
        firstNameEditText = view.findViewById(R.id.firstName_enter);
        lastNameEditText = view.findViewById(R.id.lastName_enter);
        emailEditText = view.findViewById(R.id.email_enter);
        RadioGroup genderRadioGroup = view.findViewById(R.id.genderRadio);

        regButton = view.findViewById(R.id.registerButton);
        regButton.setEnabled(false);
        logButton = view.findViewById(R.id.loginButton);
        logButton.setEnabled(false);

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
        return view;
    }


    RegisterRequest getRegRequest() {
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

    LoginRequest getLogRequest() {
        return new LoginRequest(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }
}
