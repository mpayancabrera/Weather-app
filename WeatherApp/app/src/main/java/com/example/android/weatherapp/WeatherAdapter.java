package com.example.android.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.android.weatherapp.R.id.country;
import static com.example.android.weatherapp.R.id.date;
import static com.example.android.weatherapp.R.id.humidity;
import static com.example.android.weatherapp.R.id.icon;
import static com.example.android.weatherapp.R.id.location;
import static com.example.android.weatherapp.R.id.my_weather_icon;
import static com.example.android.weatherapp.R.id.precipitation;
import static com.example.android.weatherapp.R.id.temperature;
import static com.example.android.weatherapp.R.id.wind_speed;

/**
 * Created by Manuel on 14/02/2017.
 */

/*
* {@link com.example.android.weatherapp.WeatherAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link Weather} objects.
* */
public class WeatherAdapter extends ArrayAdapter<Weather> {

    public WeatherAdapter(Activity context, ArrayList<Weather> weather_items) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, weather_items);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.weather_list_item, parent, false);
        }

        // Get the {@link Weather} object located at this position in the list
        Weather currentWeatherItem = getItem(position);

        // Find the TextView in the weather_list_item.xml layout with the date
        TextView dateTextView = (TextView) listItemView.findViewById(date);
        // Find the TextView in the weather_list_item.xml layout with the location
        TextView locationeTextView = (TextView) listItemView.findViewById(location);
        // Find the TextView in the weather_list_item.xml layout with the country
        TextView countryTextView = (TextView) listItemView.findViewById(country);
        // Find the TextView in the weather_list_item.xml layout with the temperature
        TextView tempTextView = (TextView) listItemView.findViewById(temperature);
        // Find the TextView in the weather_list_item.xml layout with the precipitations
        TextView prepTextView = (TextView) listItemView.findViewById(precipitation);
        // Find the TextView in the weather_list_item.xml layout with the humidity
        TextView humTextView = (TextView) listItemView.findViewById(humidity);
        // Find the TextView in the weather_list_item.xml layout with the wind speed
        TextView speedTextView = (TextView) listItemView.findViewById(wind_speed);
        // Find the main weather information to include a weather icon
        WeatherIconView weatherIconView = (WeatherIconView) listItemView.findViewById(R.id.my_weather_icon);

        // Get the date of the current weather item and
        // set the text to TextView
        dateTextView.setText(currentWeatherItem.getDate());
        // Get the location of the current weather item and
        // set the text to TextView
        locationeTextView.setText(currentWeatherItem.getLocation());
        // Get the country of the current weather item and
        // set the text to TextView
        countryTextView.setText(currentWeatherItem.getCountry());
        // Get the temperature of the current weather item and
        // set the text to TextView
        DecimalFormat temperatureFormat = new DecimalFormat("0");
        tempTextView.setText(temperatureFormat.format(currentWeatherItem.getTemperature()));
        // Get the precipitations of the current weather item and
        // set the text to TextView
        prepTextView.setText(currentWeatherItem.getWeatherDescr());
        // Get the humidity of the current weather item and
        // set the text to TextView
        DecimalFormat humFormat = new DecimalFormat("0");
        humTextView.setText(humFormat.format(currentWeatherItem.getHumidity()));

        // Get the temperature of the current weather item and
        // set the text to TextView
        DecimalFormat speedFormat = new DecimalFormat("0.0");
        speedTextView.setText(speedFormat.format(currentWeatherItem.getSpeed()));

        if(currentWeatherItem.getMain().equals("Clear")) {
            weatherIconView.setIconResource(getContext().getString(R.string.wi_day_sunny));
            weatherIconView.setIconSize(50);
            weatherIconView.setIconColor(Color.rgb(239, 108, 0));
        }
        else if (currentWeatherItem.getMain().equals("Rain")){
            weatherIconView.setIconResource(getContext().getString(R.string.wi_day_rain));
            weatherIconView.setIconSize(50);
            weatherIconView.setIconColor(Color.BLUE);
        }
        else if (currentWeatherItem.getMain().equals("Snow")){
            weatherIconView.setIconResource(getContext().getString(R.string.wi_day_snow));
            weatherIconView.setIconSize(50);
            weatherIconView.setIconColor(Color.BLACK);
        }
        else if (currentWeatherItem.getMain().equals("Clouds")){
            weatherIconView.setIconResource(getContext().getString(R.string.wi_day_cloudy));
            weatherIconView.setIconSize(50);
            weatherIconView.setIconColor(Color.GRAY);
        }

        return listItemView;
    }
}
