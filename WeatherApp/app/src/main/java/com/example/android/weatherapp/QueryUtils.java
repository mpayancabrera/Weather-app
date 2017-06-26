package com.example.android.weatherapp;

/**
 * Created by Manuel on 16/02/2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.android.weatherapp.R.id.location;
import static com.example.android.weatherapp.R.id.temperature;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an {@link Weather} object to represent a single Weather item.
     */
    public static List<Weather> fetchWeatherData(String requestUrl) {

        Log.v(LOG_TAG, " This is fetchEarthquakeData Method.");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Weather> weather = extractWeatherItems(jsonResponse);

        // Return the {@link Event}
        return weather;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Return a list of {@link Weather} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Weather> extractWeatherItems(String JsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Weather> Weathers = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //Parse the response given by the SAMPLE_JSON_RESPONSE string and
            //build up a list of Earthquake objects with the corresponding data.
            //Creamos un objeto JSON a partir de la cadena
            JSONObject object = new JSONObject(JsonResponse);
            JSONObject cityObject = object.getJSONObject("city");
            String city = cityObject.getString("name");
            String country = cityObject.getString("country");

            JSONArray json_array = object.optJSONArray("list");
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject jsonObject = json_array.getJSONObject(i);

                long timeInMilliseconds = jsonObject.getLong("dt") * 1000;
                Date dateObject = new Date(timeInMilliseconds);

                SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
                String dateToDisplay = dateFormat.format(dateObject);

                JSONObject temperature = jsonObject.getJSONObject("temp");
                int temp_day = (int) temperature.getDouble("day");

                int humidity = jsonObject.getInt("humidity");

                Double speed = jsonObject.getDouble("speed");

                JSONArray weather_array = jsonObject.optJSONArray("weather");
                JSONObject weatherObject = weather_array.getJSONObject(0);
                String weather_descr = weatherObject.getString("description");
                String main = weatherObject.getString("main");

                Weathers.add(new Weather(dateToDisplay, city, country, temp_day, humidity, weather_descr, speed, main));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the weather JSON results", e);
        }

        // Return the list of Weathers
        return Weathers;
    }

}
