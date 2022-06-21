package com.example.apipractice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // DATA
    JSONObject geoJSON;
    JSONObject weatherJSON;

    JSONArray hourlyArray;
    JSONArray dailyArray;

    String key;
    String zip;
    String countryCode;

    String lat;
    String lon;
    String town;

    ArrayList<String> timeList;
    ArrayList<String> tempList;
    ArrayList<String> descList;
    ArrayList<String> lightList;

    // LAYOUT COMPONENTS
    EditText zipcodeEditText;

    TextView latText;
    TextView lonText;
    TextView townText;

    TextView temp1;
    TextView temp2;
    TextView temp3;
    TextView temp4;

    TextView time1;
    TextView time2;
    TextView time3;
    TextView time4;

    TextView desc1;
    TextView desc2;
    TextView desc3;
    TextView desc4;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;

    ArrayList<TextView> tempTextList;
    ArrayList<TextView> timeTextList;
    ArrayList<TextView> descTextList;
    ArrayList<ImageView> imgList;

    Button weatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key = "f8bbe0cb2b3227fcbaf560bad01471fb";
        countryCode = "us";

        timeList= new ArrayList<String>();
        tempList = new ArrayList<String>();
        descList = new ArrayList<String>();
        lightList = new ArrayList<String>();

        zipcodeEditText = findViewById(R.id.id_edit_zipcode);

        latText = findViewById(R.id.id_text_lat);
        lonText = findViewById(R.id.id_text_lon);
        townText = findViewById(R.id.id_text_town);

        temp1 = findViewById(R.id.id_text_temp_1);
        temp2 = findViewById(R.id.id_text_temp_2);
        temp3 = findViewById(R.id.id_text_temp_3);
        temp4 = findViewById(R.id.id_text_temp_4);

        time1 = findViewById(R.id.id_text_time_1);
        time2 = findViewById(R.id.id_text_time_2);
        time3 = findViewById(R.id.id_text_time_3);
        time4 = findViewById(R.id.id_text_time_4);

        desc1 = findViewById(R.id.id_text_desc_1);
        desc2 = findViewById(R.id.id_text_desc_2);
        desc3 = findViewById(R.id.id_text_desc_3);
        desc4 = findViewById(R.id.id_text_desc_4);

        img1 = findViewById(R.id.id_image_1);
        img2 = findViewById(R.id.id_image_2);
        img3 = findViewById(R.id.id_image_3);
        img4 = findViewById(R.id.id_image_4);

        tempTextList = new ArrayList<TextView>();
        timeTextList = new ArrayList<TextView>();
        descTextList = new ArrayList<TextView>();
        imgList = new ArrayList<ImageView>();

        tempTextList.add(temp1);
        tempTextList.add(temp2);
        tempTextList.add(temp3);
        tempTextList.add(temp4);

        timeTextList.add(time1);
        timeTextList.add(time2);
        timeTextList.add(time3);
        timeTextList.add(time4);

        descTextList.add(desc1);
        descTextList.add(desc2);
        descTextList.add(desc3);
        descTextList.add(desc4);

        imgList.add(img1);
        imgList.add(img2);
        imgList.add(img3);
        imgList.add(img4);

        weatherButton = findViewById(R.id.id_button_weather);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zip = zipcodeEditText.getText().toString();
                timeList.clear();
                tempList.clear();
                descList.clear();
                lightList.clear();

                GeoCallTask geoTask = new GeoCallTask(key, zip, countryCode);
                geoTask.execute();
            }
        });

    }

    public class GeoCallTask extends AsyncTask<String, String, String> {
        String data;
        String geoCall;

        public GeoCallTask(String key, String zip, String countryCode) {
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

                latText.setText("Latitude: Invalid Zipcode");
                lonText.setText("Longitude Invalid Zipcode");
                townText.setText("Town: Invalid Zipcode");
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                geoJSON = new JSONObject(s);
                lat = geoJSON.getString("lat");
                lon = geoJSON.getString("lon");
                town = geoJSON.getString("name");

                latText.setText("Latitude: " + lat);
                lonText.setText("Longitude " + lon);
                townText.setText("Town: " + town);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            WeatherTask weatherTask = new WeatherTask(key, lat, lon);
            weatherTask.execute();
        }
    }

    public class WeatherTask extends AsyncTask<String, String, String> {

        String data;
        String weatherCall;

        public WeatherTask(String key, String lat, String lon) {
            this.data = "";
            this.weatherCall = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude={part}&appid=" + key;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL weatherURL = new URL(weatherCall);
                HttpURLConnection weatherConn = (HttpURLConnection) weatherURL.openConnection();

                InputStream weatherStream = weatherConn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weatherStream));

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
            try {
                weatherJSON = new JSONObject(s);
                hourlyArray = weatherJSON.getJSONArray("hourly");
                dailyArray = weatherJSON.getJSONArray("daily");

                // RETRIEVE DATA
                for(int i = 0; i < 4; i++) {
                    timeList.add(hourlyArray.getJSONObject(i).getString("dt"));
                    tempList.add(hourlyArray.getJSONObject(i).getString("temp"));
                    descList.add(hourlyArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));

                    if(Integer.valueOf(timeList.get(i)) > Integer.valueOf(dailyArray.getJSONObject(0).getString("sunset")) || Integer.valueOf(timeList.get(i)) < Integer.valueOf(dailyArray.getJSONObject(0).getString("sunrise")))
                        lightList.add("dark");
                    else
                        lightList.add("light");

                }

                // FORMAT DATA
                for(int i = 0; i < 4; i++) {

                    // FORMAT UNIT TIME
                    Date date = new java.util.Date(Integer.parseInt(timeList.get(i))*1000L);
                    SimpleDateFormat hourFormat = new java.text.SimpleDateFormat("HH");
                    hourFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT-5"));

                    int hour = Integer.parseInt(hourFormat.format(date));

                    if(hour >= 12) {
                        hour -= 12;

                        if(hour == 0)
                            timeList.set(i, "12 PM");
                        else
                            timeList.set(i, hour + " PM");
                    } else {
                        if(hour == 0)
                            timeList.set(i, "12 AM");
                        else
                            timeList.set(i, hour + " AM");
                    }

                    // FORMAT TEMPERATURE
                    int formattedTemp = (int) ((Double.valueOf(tempList.get(i)) - 273.15) * (9.0/5.0) + 32.0);
                    tempList.set(i, formattedTemp + "°F");
                }

                // DISPLAY DATA

                for(int i = 0; i < 4; i++) {
                    timeTextList.get(i).setText(timeList.get(i));
                    tempTextList.get(i).setText(tempList.get(i));
                    descTextList.get(i).setText(descList.get(i));

                    String desc = descList.get(i);
                    String light = lightList.get(i);

                    if(desc.equals("Thunderstorm")) {
                        imgList.get(i).setImageResource(R.drawable.thunder);
                    } else if(desc.equals("Drizzle")) {
                        if(light.equals("light"))
                            imgList.get(i).setImageResource(R.drawable.drizzle);
                        else
                            imgList.get(i).setImageResource(R.drawable.drizzlenight);
                    } else if(desc.equals("Rain")) {
                        imgList.get(i).setImageResource(R.drawable.rain);
                    } else if(desc.equals("Snow")) {
                        imgList.get(i).setImageResource(R.drawable.snow);
                    } else if(desc.equals("Clear")) {
                        if(light.equals("light"))
                            imgList.get(i).setImageResource(R.drawable.sunny);
                        else
                            imgList.get(i).setImageResource(R.drawable.clearnight);
                    } else if(desc.equals("Clouds")) {
                        if(light.equals("light"))
                            imgList.get(i).setImageResource(R.drawable.clouds);
                        else
                            imgList.get(i).setImageResource(R.drawable.cloudsnight);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}