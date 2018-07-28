package com.example.hmejia.smartbeat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.FaceDetector;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.util.AWSRequestMetrics;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    EditText UsernameEt, PasswordEt;

    AlertDialog alertDialog;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsernameEt = (EditText)findViewById(R.id.userNameET);
        PasswordEt = (EditText)findViewById(R.id.passwordET);


        Button button = (Button) findViewById(R.id.loginBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = UsernameEt.getText().toString();
                password = PasswordEt.getText().toString();

                String type = "login";

                BackgroundWork backgroundWorker = new BackgroundWork(MainActivity.this);
                backgroundWorker.delegate = MainActivity.this;
                backgroundWorker.execute(type, username, password);

                /*
                Intent i = new Intent(MainActivity.this, FaceRecognition.class);
                bundle.putString("user_name", username);
                bundle.putString("user_password", password);
                i.putExtras(bundle);
                startActivity(i);
                */
            }
        });



        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

    }

    @Override
    public void processFinish(String output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        final String[] ss = output.split(",");

        if (ss[0].length() > 0) {
            builder.setTitle("SmartBeat Login")
                    .setMessage("Login Succesful!\n\nWelcome: " + ss[0])
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            Intent i = new Intent(getBaseContext(), FaceRecognition.class);
                            bundle.putString("user_name", ss[1]);
                            bundle.putString("user_password", ss[2]);
                            i.putExtras(bundle);
                            startActivity(i);
                            //startActivity(new Intent(getBaseContext(),FaceRecognition.class));
                        }
                    })
                    .show();

        } else {
            builder.setTitle("SmartBeat Login")
                    .setMessage("Login Unsuccesful!\n\nIncorrcet Username or Password")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
