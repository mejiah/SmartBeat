package com.example.hmejia.smartbeat;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;

import com.amazonaws.Request;
import com.amazonaws.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class BackgroundWork extends AsyncTask<String, String, String> {

    Context context;
    AlertDialog.Builder alertDialog;
    BackgroundWork (Context ctx) {
        context = ctx;
    }
    Bundle bundle = new Bundle();
    String username;
    String password;

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];

        String login_url = "https://hmejiauw.000webhostapp.com/smartBeat/login.php";
        String register_url = "https://hmejiauw.000webhostapp.com/smartBeat/registration.php";
        String submitData_url = "https://hmejiauw.000webhostapp.com/smartBeat/submitData.php";

        if (type.equals("login")) {
            try {
                username = params[1];
                password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"
                        +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("register")) {
            try {
                String fName = params[1];
                String lName = params[2];
                String uName = params[3];
                String pWord = params[4];
                String bDate = params[5];

                URL url = new URL(register_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("fName", "UTF-8")+"="+URLEncoder.encode(fName, "UTF-8")+"&"
                        + URLEncoder.encode("lName", "UTF-8")+"="+URLEncoder.encode(lName, "UTF-8")+"&"
                        + URLEncoder.encode("uName", "UTF-8")+"="+URLEncoder.encode(uName, "UTF-8")+"&"
                        + URLEncoder.encode("pWord", "UTF-8")+"="+URLEncoder.encode(pWord, "UTF-8")+"&"
                        + URLEncoder.encode("bDate", "UTF-8")+"="+URLEncoder.encode(bDate, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("submitData")) {
            try {

                String userName = params[1];
                String password = params[2];
                String smile = params[3];
                String joy  = params[4];
                String anger = params[5];
                String disgust = params[6];
                String contempt = params[7];
                String fear = params[8];
                String sadness = params[9];
                String surprise = params[10];
                String submitDate = params[11];
                String submitTime = params[12];
                String label = params[13];

                URL url = new URL(submitData_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("userName", "UTF-8")+"="+URLEncoder.encode(userName, "UTF-8")+"&"
                        + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                        + URLEncoder.encode("smile", "UTF-8")+"="+URLEncoder.encode(smile, "UTF-8")+"&"
                        + URLEncoder.encode("joy", "UTF-8")+"="+URLEncoder.encode(joy, "UTF-8")+"&"
                        + URLEncoder.encode("anger", "UTF-8")+"="+URLEncoder.encode(anger, "UTF-8")+"&"
                        + URLEncoder.encode("disgust", "UTF-8")+"="+URLEncoder.encode(disgust, "UTF-8")+"&"
                        + URLEncoder.encode("contempt", "UTF-8")+"="+URLEncoder.encode(contempt, "UTF-8")+"&"
                        + URLEncoder.encode("fear", "UTF-8")+"="+URLEncoder.encode(fear, "UTF-8")+"&"
                        + URLEncoder.encode("sadness", "UTF-8")+"="+URLEncoder.encode(sadness, "UTF-8")+"&"
                        + URLEncoder.encode("surprise", "UTF-8")+"="+URLEncoder.encode(surprise, "UTF-8")+"&"
                        + URLEncoder.encode("submitDate", "UTF-8")+"="+URLEncoder.encode(submitDate, "UTF-8")+"&"
                        + URLEncoder.encode("submitTime", "UTF-8")+"="+URLEncoder.encode(submitTime, "UTF-8")+"&"
                        + URLEncoder.encode("label", "UTF-8")+"="+URLEncoder.encode(label, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onPostExecute(final String result) {
        //alertDialog.setMessage(result);
        //alertDialog.show();
        delegate.processFinish(result);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}

