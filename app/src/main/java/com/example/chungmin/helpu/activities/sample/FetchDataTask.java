package com.example.chungmin.helpu.activities.sample;

/**
 * Created by Chung Min on 7/23/2015.
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.chungmin.helpu.callback.FetchDataListener;
import com.example.chungmin.helpu.models.Application;

import HelpUGenericUtilities.StringUtils;

public class FetchDataTask extends AsyncTask<String, Void, String>{
    private final FetchDataListener listener;
    private String msg;

    public FetchDataTask(FetchDataListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if(params == null) return null;

        // get url from params
        String url = params[0];

        try {
            // create http connection
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);

            // connect
            HttpResponse response = client.execute(httpget);

            // get response
            HttpEntity entity = response.getEntity();

            if(entity == null) {
                msg = "No response from server";
                return null;
            }

            // get response content and convert it to json string
            InputStream is = entity.getContent();
            return StringUtils.streamToString(is);
        }
        catch(IOException e){
            msg = "No Network Connection";
        }

        return null;
    }

    @Override
    protected void onPostExecute(String sJson) {
        if(sJson == null) {
            if(listener != null) listener.Failure(msg);
            return;
        }

        try {
            // convert json string to json array
            JSONArray aJson = new JSONArray(sJson);
            // create apps list
            List<Application> apps = new ArrayList<Application>();

            for(int i=0; i<aJson.length(); i++) {
                JSONObject json = aJson.getJSONObject(i);
                Application app = new Application();
                app.setTitle(json.getString("app_title"));
                app.setTotalDl(Long.parseLong(json.getString("total_dl")));
                app.setRating(Integer.parseInt(json.getString("rating")));
                app.setIcon(json.getString("icon"));

                // add the app to apps list
                apps.add(app);
            }

            //notify the activity that fetch data has been complete
            if(listener != null) listener.Complete(apps);
        } catch (JSONException e) {
            msg = "Invalid response";
            if(listener != null) listener.Failure(msg);
            return;
        }
    }


}