package com.example.chungmin.helpu.serverrequest;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.os.AsyncTask;

import com.example.chungmin.helpu.callback.FetchServiceProviderDataListener;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.serverrequest.common.ServerUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import HelpUGenericUtilities.StringUtils;

public class ServiceProviderDataTask extends AsyncTask<String, Void, String> {
    private final FetchServiceProviderDataListener listener;
    private String msg;

    public ServiceProviderDataTask(FetchServiceProviderDataListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if(params == null) return null;

        // get url from params
        String url = params[0];

        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("userId", params[1]));

        try {
            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            // connect
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse response = client.execute(post);

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
            if (listener != null)
                listener.Failure(msg);
            return;
        }

        // create serviceProviders list
        List<ServiceProvider> serviceProviderList = null;
        try {
            serviceProviderList = ServiceProviderManager.buildList(sJson);

            //notify the activity that fetch data has been complete
            if (listener != null)
                listener.Complete(serviceProviderList);

        } catch (JSONException e) {
            msg = "Invalid response";
            if (listener != null)
                listener.Failure(msg);
            return;
        }


    }


}