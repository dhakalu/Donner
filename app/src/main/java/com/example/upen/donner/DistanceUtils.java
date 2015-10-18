package com.example.upen.donner;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class DistanceUtils {

    public static String[] getDistance(String origin, String destination){
        String[] params = {origin, destination};
        JSONObject result;
        String distance = "";
        String distance_text = "";
        String duration = "";
        try {
            result = new CalculateDistanceTask().execute(params).get();
            JSONObject object = result.getJSONArray("rows").getJSONObject(0);
            JSONObject object1 = object.getJSONArray("elements").getJSONObject(0);
            distance = "" + object1.getJSONObject("distance").getInt("value");
            distance_text = "" + object1.getJSONObject("distance").getString("text");
            duration = object1.getJSONObject("duration").getString("text");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[]{distance, distance_text, duration};
    }

    static class CalculateDistanceTask extends AsyncTask<String[], Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String[]... params) {

            JSONObject returnJSON = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String[] passedParams = params[0];
            String from = passedParams[0];
            String to = passedParams[1];

            final String ORIGIN_PARAM = "origins";
            final String DEST_PARAM = "destinations";
            final String API_PARAM = "key";
            final String MODE_PARAM = "mode";
            final String UNIT_PARAM = "units";

            Uri builtUri = Uri.parse(Constants.BASE_DISTANCE_URL)
                    .buildUpon()
                    .appendQueryParameter(ORIGIN_PARAM, from)
                    .appendQueryParameter(DEST_PARAM, to)
                    .appendQueryParameter(API_PARAM, Constants.API_KEY)
                    .appendQueryParameter(MODE_PARAM, Constants.DEFAULT_TRANSPORT_MODE)
                    .appendQueryParameter(UNIT_PARAM, "imperial")
                    .build();
            try {
                URL url = new URL(builtUri.toString());
                Log.e("Upen", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                returnJSON = new JSONObject(buffer.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnJSON;
        }
    }
}
