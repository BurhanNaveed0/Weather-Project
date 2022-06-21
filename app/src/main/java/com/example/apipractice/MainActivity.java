package com.example.apipractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = "f8bbe0cb2b3227fcbaf560bad01471fb";
        String zip = "08824";
        String countryCode = "us";
    }

    public class geoCallTask extends AsyncTask<String, String, String> {

        String key;
        String zip;
        String countryCode;
        String data;

        String geoCall;

        public geoCallTask(String key, String zip, String countryCode) {
            this.key = key;
            this.zip = zip;
            this.countryCode = countryCode;
            this.data = "";

            this.geoCall = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zip + "," + countryCode + "&appid=" + key;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL geoURL = new URL(geoCall);
                HttpURLConnection geoConnection = (HttpURLConnection) geoURL.openConnection();

                InputStream geoStream = geoConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(geoStream));

                String line = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("data", s);
        }
    }
}