package com.example.android.weatherapp;

/**
 * Created by Manuel on 14/02/2017.
 */

import static com.example.android.weatherapp.R.id.humidity;
import static com.example.android.weatherapp.R.id.temperature;

/**
 * An {@link Weather} object contains information related to a single weather item.
 */
public class Weather {

    /** Temperature */
    private int mTemperature;

    /** Precipitation */
    private String mWeatherDesc;

    /** Humidity */
    private int mHumidity;

    /** Location of the weather prediction */
    private String mLocation;

    /** Country of the weather prediction */
    private String mCountry;

    //Weather prediction date
    private String mDate;

    //Weather wind speed
    private Double mSpeed;

    //Weather main information
    private String mMain;

    /**
     * Create a new Earthquake object
     *
     * @param temperature is the temperature
     *
     * @param weather_descr is the weather description
     *
     * @param humidity is the humidity
     *
     * @param speed is the wind speed (m/s per default)
     *
     * @param location is the city location
     *
     * @param country is the country
     *
     * @param date is the weather time
     *
     * @param main is the main weather information
     */
    public Weather(String date, String location, String country, int temperature, int humidity, String weather_descr, Double speed, String main) {
        mTemperature = temperature;
        mWeatherDesc = weather_descr;
        mHumidity = humidity;
        mDate = date;
        mLocation = location;
        mCountry = country;
        mSpeed = speed;
        mMain = main;
    }

    /**
     * Gets the city
     *
     * @return current String in the mLocation.
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Gets the country
     *
     * @return current String in the mCountry.
     */
    public String getCountry() {
        return mCountry;
    }

    /**
     * Gets the date
     *
     * @return current String in the mDate.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Gets the temperature
     *
     * @return current value in the mTemperature.
     */
    public int getTemperature() {
        return mTemperature;
    }

    /**
     * Gets the precipitation
     *
     * @return current value in the mWeatherDesc.
     */
    public String getWeatherDescr() {
        return mWeatherDesc;
    }

    /**
     * Gets the humidity
     *
     * @return current value in the mHumidity.
     */
    public int getHumidity() {
        return mHumidity;
    }

    /**
     * Gets the wind speed
     *
     * @return current value in the mWindSpeed.
     */
    public Double getSpeed() {
        return mSpeed;
    }

    /**
     * Gets the main weather information
     *
     * @return current value in the mMain.
     */
    public String getMain() {
        return mMain;
    }


}
