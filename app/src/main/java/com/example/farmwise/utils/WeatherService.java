package com.example.farmwise.utils;

import com.example.farmwise.models.WeatherData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private static final String GEO_API_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";
    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,weather_code&daily=precipitation_sum&timezone=auto";

    public static WeatherData getWeather(String city) {
        try {
            String geoUrlStr = String.format(GEO_API_URL, city.replace(" ", "+"));
            String geoResponse = fetchUrl(geoUrlStr);

            if (geoResponse == null)
                return null;

            JsonObject geoJson = JsonParser.parseString(geoResponse).getAsJsonObject();
            if (!geoJson.has("results"))
                return null;

            JsonArray results = geoJson.getAsJsonArray("results");
            if (results.size() == 0)
                return null;

            JsonObject firstResult = results.get(0).getAsJsonObject();
            double lat = firstResult.get("latitude").getAsDouble();
            double lon = firstResult.get("longitude").getAsDouble();

            String weatherUrlStr = String.format(WEATHER_API_URL, lat, lon);
            String weatherResponse = fetchUrl(weatherUrlStr);

            if (weatherResponse == null)
                return null;

            JsonObject weatherJson = JsonParser.parseString(weatherResponse).getAsJsonObject();
            JsonObject current = weatherJson.getAsJsonObject("current");
            JsonObject daily = weatherJson.getAsJsonObject("daily");

            int temp = current.get("temperature_2m").getAsInt();
            int humidity = current.get("relative_humidity_2m").getAsInt();
            int code = current.get("weather_code").getAsInt();
            String desc = decodeWeatherCode(code);

            double rainSum = daily.getAsJsonArray("precipitation_sum").get(0).getAsDouble();
            boolean rainExpected = rainSum > 0.5;

            return new WeatherData(temp, desc, humidity, rainExpected);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String fetchUrl(String urlString) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private static String decodeWeatherCode(int code) {
        if (code == 0)
            return "Clear Sky";
        if (code >= 1 && code <= 3)
            return "Partly Cloudy";
        if (code >= 45 && code <= 48)
            return "Foggy";
        if (code >= 51 && code <= 55)
            return "Drizzle";
        if (code >= 61 && code <= 67)
            return "Rain";
        if (code >= 71 && code <= 77)
            return "Snow";
        if (code >= 80 && code <= 82)
            return "Showers";
        if (code >= 95 && code <= 99)
            return "Thunderstorm";
        return "Unknown";
    }
}
