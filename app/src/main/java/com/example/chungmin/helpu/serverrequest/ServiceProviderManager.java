package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.GetBooleanCallback;
import com.example.chungmin.helpu.callback.GetServiceProviderCallback;
import com.example.chungmin.helpu.callback.GetServiceProviderListCallback;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Chung Min on 7/19/2015.
 * 09 Sep 2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */
public class ServiceProviderManager {

    public ServiceProviderManager() {
    }

    private static ServiceProvider parseJson(JSONObject json) throws JSONException {
        ServiceProvider target = new ServiceProvider();
        target.setServiceProviderId(json.getInt("serviceProviderId"));
        target.setUserId(json.getInt("userId"));
        target.setUserName(json.getString("userName"));
        target.setServiceId(json.getInt("serviceId"));
        target.setServiceName(json.getString("serviceName"));
        target.setPhone(json.getString("phone"));
        target.setEmail(json.getString("email"));
        target.setAvgRatedValue(json.getDouble("avgRatedValue"));
        return target;
    }

    public static ServiceProvider buildRecord(String result) throws JSONException {

        ServiceProvider target = new ServiceProvider();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static List<ServiceProvider> buildList(String result) throws JSONException {
        List<ServiceProvider> list = new ArrayList<ServiceProvider>();
        JSONArray aJson = new JSONArray(result);
        for (int i = 0; i < aJson.length(); i++) {
            JSONObject json = aJson.getJSONObject(i);
            ServiceProvider target = parseJson(json);
            list.add(target);
        }
        return list;
    }

    public void insert(ServiceProvider serviceProvider, String url, GetServiceProviderCallback serviceProviderCallback) {
        new Insert(serviceProvider, serviceProviderCallback).execute(url);
    }

    public void getByID(int serviceProviderId, String url, GetServiceProviderCallback serviceProviderCallBack) {
        new GetByID(serviceProviderId, serviceProviderCallBack).execute(url);
    }

    public void getByServiceID(int serviceId, int userId, String url, GetServiceProviderListCallback serviceProviderListCallBack) {
        new GetByServiceID(serviceId, userId, serviceProviderListCallBack).execute(url);
    }

    public void delete(int serviceProviderId, int isLogicalDelete, String url, GetServiceProviderCallback serviceProviderCallback) {
        new Delete(serviceProviderId, isLogicalDelete, serviceProviderCallback).execute(url);
    }

    public void isServiceProviderAlreadyExists(int userId, int serviceId, String url, GetBooleanCallback booleanCallBack) {
        new IsServiceProviderAlreadyExists(userId, serviceId, booleanCallBack).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class Insert extends AsyncTask<String, Void, Void> {
        ServiceProvider serviceProvider;
        GetServiceProviderCallback serviceProviderCallBack;

        public Insert(ServiceProvider serviceProvider, GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProvider = serviceProvider;
            this.serviceProviderCallBack = serviceProviderCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", serviceProvider.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("serviceId", serviceProvider.getServiceId() + ""));
            dataToSend.add(new BasicNameValuePair("email", serviceProvider.getEmail()));
            dataToSend.add(new BasicNameValuePair("phone", serviceProvider.getPhone()));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            serviceProviderCallBack.done(null);
        }

    }

    public class GetByID extends AsyncTask<String, Void, ServiceProvider> {
        int serviceProviderId;
        GetServiceProviderCallback serviceProviderCallBack;

        public GetByID(int serviceProviderId, GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProviderId = serviceProviderId;
            this.serviceProviderCallBack = serviceProviderCallBack;
        }

        @Override
        protected ServiceProvider doInBackground(String... params) {
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("serviceProviderId", this.serviceProviderId + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            ServiceProvider returnedServiceProvider = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "GetByID");
                returnedServiceProvider = buildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedServiceProvider;
        }

        @Override
        protected void onPostExecute(ServiceProvider returnedServiceProvider) {
            super.onPostExecute(returnedServiceProvider);
            serviceProviderCallBack.done(returnedServiceProvider);
        }
    }

    public class GetByServiceID extends AsyncTask<String, Void, List<ServiceProvider>> {
        int serviceId;
        int userId;
        GetServiceProviderListCallback serviceProviderListCallBack;

        public GetByServiceID(int serviceId, int userId, GetServiceProviderListCallback serviceProviderListCallBack) {
            this.serviceId = serviceId;
            this.userId = userId;
            this.serviceProviderListCallBack = serviceProviderListCallBack;
        }

        @Override
        protected List<ServiceProvider> doInBackground(String... params) {
            // create serviceProviders list
            List<ServiceProvider> serviceProviderList = new ArrayList<ServiceProvider>();

            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("serviceId", this.serviceId + ""));
            dataToSend.add(new BasicNameValuePair("userId", this.userId + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                serviceProviderList = buildList(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return serviceProviderList;
        }

        @Override
        protected void onPostExecute(List<ServiceProvider> returnedServiceProviderList) {
            super.onPostExecute(returnedServiceProviderList);
            serviceProviderListCallBack.done(returnedServiceProviderList);
        }
    }

    public class Delete extends AsyncTask<String, Void, Void> {
        int serviceProviderId;
        GetServiceProviderCallback serviceProviderCallBack;
        int isLogicalDelete;

        public Delete(int serviceProviderId, int isLogicalDelete, GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProviderId = serviceProviderId;
            this.serviceProviderCallBack = serviceProviderCallBack;
            this.isLogicalDelete = isLogicalDelete;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("serviceProviderId", serviceProviderId + ""));
            dataToSend.add(new BasicNameValuePair("isLogicalDelete", isLogicalDelete + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            serviceProviderCallBack.done(null);
        }

    }

    public class IsServiceProviderAlreadyExists extends AsyncTask<String, Void, Boolean> {
        int userId;
        int serviceId;
        GetBooleanCallback booleanCallBack;

        public IsServiceProviderAlreadyExists(int userId, int serviceId, GetBooleanCallback booleanCallBack) {
            this.userId = userId;
            this.serviceId = serviceId;
            this.booleanCallBack = booleanCallBack;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean isTrigger = false;
            if(params == null)
                return false;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", this.userId + ""));
            dataToSend.add(new BasicNameValuePair("serviceId", this.serviceId + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Function IsServiceProviderAlreadyExists Call");

                    return isTrigger = jObject.getBoolean("result");

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return isTrigger;
        }

        @Override
        protected void onPostExecute(Boolean isTrigger) {
            super.onPostExecute(isTrigger);
            booleanCallBack.done(isTrigger);
        }
    }


}
