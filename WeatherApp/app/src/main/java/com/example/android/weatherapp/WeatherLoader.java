package com.example.android.weatherapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Manuel on 16/02/2017.
 */

public class WeatherLoader extends AsyncTaskLoader<List<Weather>> {

    /** Tag for log messages */
    private static final String LOG_TAG = WeatherLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link WeatherLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public WeatherLoader(Context context, String url) {
        super(context);
        // TODO: Finish implementing this constructor
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, " This is onStartLoading Method.");
        forceLoad();
    }

    @Override
    public List<Weather> loadInBackground() {
        // TODO: Implement this method
        Log.v(LOG_TAG, " This is loadInBackground Method.");
        if (mUrl == null) {
            return null;
        }
        //Perform the network request, parse the response, and extract a list of earthquakes.
        List<Weather> weathers = QueryUtils.fetchWeatherData(mUrl);
        return weathers;
    }
}