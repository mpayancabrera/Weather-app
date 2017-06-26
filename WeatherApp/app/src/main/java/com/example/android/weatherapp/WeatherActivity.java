package com.example.android.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Weather>>, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG = WeatherActivity.class.getName();

    private final String WEATHER_REQUEST = "http://api.openweathermap.org/data/2.5/forecast/daily?";

    /** Adapter for the list of earthquakes */
    private WeatherAdapter mAdapter;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int WEATHER_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Find a reference to the {@link ListView} in the layout
        ListView weatherListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        weatherListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of weather items as input
        mAdapter = new WeatherAdapter(this, new ArrayList<Weather>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        weatherListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(WEATHER_LOADER_ID, null, this);
        }
        else
        {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Weather>> onCreateLoader(int i, Bundle bundle) {
        // TODO: Create a new loader for the given URL
        Log.v(LOG_TAG, " This is onCreateLoader Method.");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String days = sharedPrefs.getString(getString(R.string.settings_number_of_days_key), getString(R.string.settings_number_of_days_default));

        String location = sharedPrefs.getString(getString(R.string.settings_location_key), null);

        Uri baseUri = Uri.parse(WEATHER_REQUEST);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", location);
        Log.v(LOG_TAG, "Location: " + location);
        uriBuilder.appendQueryParameter("units", "metric");
        uriBuilder.appendQueryParameter("cnt", days);
        uriBuilder.appendQueryParameter("appid", "7df3bafd70a63bbe32e2d10072eb4b84");

        String lang = Locale.getDefault().getDisplayLanguage();
        if(lang.equals("español")) {
            uriBuilder.appendQueryParameter("lang", "es");
        }

        // Create a new loader for the given URL
        return new WeatherLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Weather>> loader, List<Weather> WeatherItems) {
        // TODO: Update the UI with the result
        Log.v(LOG_TAG, " This is onLoadFinished Method.");

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_weather_Data);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (WeatherItems != null && !WeatherItems.isEmpty()) {
            mAdapter.addAll(WeatherItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Weather>> loader) {
        // TODO: Loader reset, so we can clear out our existing data.
        Log.v(LOG_TAG, " This is onLoaderReset Method.");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_number_of_days_key)) || key.equals(getString(R.string.settings_location_key))){
            // Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(WEATHER_LOADER_ID, null, this);
        }
    }
}
