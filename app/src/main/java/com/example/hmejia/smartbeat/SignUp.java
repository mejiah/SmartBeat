package com.example.hmejia.smartbeat;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity implements AsyncResponse {

    EditText firstName, lastName, userName, password, birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstName = (EditText)findViewById(R.id.firstNameET);
        lastName = (EditText)findViewById(R.id.lastNameET);
        userName = (EditText)findViewById(R.id.newUserNameET);
        password = (EditText)findViewById(R.id.newPasswordET);
        birthDate = (EditText)findViewById(R.id.birthDateET);

        Button signUpBtn = (Button) findViewById(R.id.newSignUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String uName = userName.getText().toString();
                String pWord = password.getText().toString();
                String bDate = birthDate.getText().toString();
                String type = "register";

                BackgroundWork backgroundWorker = new BackgroundWork(SignUp.this);
                backgroundWorker.delegate = SignUp.this;
                backgroundWorker.execute(type, fName, lName, uName, pWord, bDate);
                startActivity(new Intent(SignUp.this, MainActivity.class));

            }
        });

    }

    @Override
    public void processFinish(String output) {

    }
}

