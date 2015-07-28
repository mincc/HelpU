package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CustomerRequestDataTask extends AsyncTask<String, Void, String>{
    private final CustomerRequestDataListener listener;
    private String msg;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public CustomerRequestDataTask(CustomerRequestDataListener listener) {
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
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            // create http connection
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
            return streamToString(is);
        }
        catch(IOException e){
            msg = "No Network Connection";
        }

        return null;
    }

    @Override
    protected void onPostExecute(String sJson) {
        if(sJson == null) {
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }

        try {
            // convert json string to json array
            JSONArray aJson = new JSONArray(sJson);
            // create customerRequests list
            List<CustomerRequest> customerRequests = new ArrayList<CustomerRequest>();

            for(int i=0; i<aJson.length(); i++) {
                JSONObject json = aJson.getJSONObject(i);
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.setCustomerRequestId(Integer.parseInt(json.getString("customerRequestId")));
                customerRequest.setServiceId(Integer.parseInt(json.getString("serviceId")));
                customerRequest.setUserId(Integer.parseInt(json.getString("userId")));
                customerRequest.setDescription(json.getString("description"));
                customerRequest.setProjectStatusId(json.getInt("projectStatusId"));

                customerRequest.setUserName(json.getString("userName"));
                customerRequest.setServiceName(json.getString("serviceName"));
                customerRequest.setProjectStatusName(json.getString("projectStatusName"));

                // add the customerRequest to customerRequests list
                customerRequests.add(customerRequest);
            }

            //notify the activity that fetch data has been complete
            if(listener != null) listener.onFetchComplete(customerRequests);
        } catch (JSONException e) {
            msg = "Invalid response";
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }
    }

    /**
     * This function will convert response stream into json string
     * @param is respons string
     * @return json string
     * @throws IOException
     */
    public String streamToString(final InputStream is) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                throw e;
            }
        }

        return sb.toString();
    }
}