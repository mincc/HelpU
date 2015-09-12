package com.example.chungmin.helpu;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class ServiceProviderServerRequests {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public ServiceProviderServerRequests() {
    }

    public static ServiceProvider BuildRecord(String result) {

        ServiceProvider target = new ServiceProvider();
        try {
            JSONObject jObject = new JSONObject(result);
            if (jObject.length() != 0) {
                target.setServiceProviderId(jObject.getInt("serviceProviderId"));
                target.setUserId(jObject.getInt("userId"));
                target.setUserName(jObject.getString("userName"));
                target.setServiceId(jObject.getInt("serviceId"));
                target.setServiceName(jObject.getString("serviceName"));
                target.setPhone(jObject.getString("phone"));
                target.setEmail(jObject.getString("email"));
                target.setAvgRatedValue(jObject.getDouble("avgRatedValue"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return target;
    }

    public static List<ServiceProvider> BuildList(String result) throws JSONException {

        List<ServiceProvider> list = new ArrayList<ServiceProvider>();

        try {
            JSONArray aJson = new JSONArray(result);

            for (int i = 0; i < aJson.length(); i++) {
                JSONObject json = aJson.getJSONObject(i);
                ServiceProvider target = new ServiceProvider();
                target.setServiceProviderId(json.getInt("serviceProviderId"));
                target.setUserId(json.getInt("userId"));
                target.setServiceId(json.getInt("serviceId"));
                target.setPhone(json.getString("phone"));
                target.setEmail(json.getString("email"));
                target.setAvgRatedValue(json.getDouble("avgRatedValue"));
                target.setUserName(json.getString("userName"));
                target.setServiceName(json.getString("serviceName"));

                list.add(target);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("CustomerRequest - BuildList");
        }

        return list;
    }

    public void storeServiceProviderDataInBackground(ServiceProvider serviceProvider, String url, GetServiceProviderCallback serviceProviderCallback) {
        new StoreServiceProviderDataAsyncTask(serviceProvider, serviceProviderCallback).execute(url);
    }

    public void getServiceProviderByID(int serviceProviderId, String url, GetServiceProviderCallback serviceProviderCallBack) {
        new ServiceProviderByID(serviceProviderId, serviceProviderCallBack).execute(url);
    }

    public void getServiceProviderByServiceID(int serviceId, int userId, String url, GetServiceProviderListCallback serviceProviderListCallBack) {
        new ServiceProviderByServiceID(serviceId, userId, serviceProviderListCallBack).execute(url);
    }

    public void serviceProviderDelete(int serviceProviderId, String url, GetServiceProviderCallback serviceProviderCallback) {
        new ServiceProviderDelete(serviceProviderId, serviceProviderCallback).execute(url);
    }

    public void isServiceProviderAlreadyExists(int userId, int serviceId, String url, GetBooleanCallback booleanCallBack) {
        new IsServiceProviderAlreadyExists(userId, serviceId, booleanCallBack).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class StoreServiceProviderDataAsyncTask extends AsyncTask<String, Void, Void> {
        ServiceProvider serviceProvider;
        GetServiceProviderCallback serviceProviderCallBack;

        public StoreServiceProviderDataAsyncTask(ServiceProvider serviceProvider, GetServiceProviderCallback serviceProviderCallBack) {
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
            dataToSend.add(new BasicNameValuePair("userId", serviceProvider.userId+ ""));
            dataToSend.add(new BasicNameValuePair("serviceId", serviceProvider.serviceId+ ""));
            dataToSend.add(new BasicNameValuePair("email", serviceProvider.email));
            dataToSend.add(new BasicNameValuePair("phone", serviceProvider.phone));

            HttpParams httpRequestParams = getHttpRequestParams();

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

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            serviceProviderCallBack.done(null);
        }

    }

    public class ServiceProviderByID extends AsyncTask<String, Void, ServiceProvider> {
        int serviceProviderId;
        GetServiceProviderCallback serviceProviderCallBack;

        public ServiceProviderByID(int serviceProviderId, GetServiceProviderCallback serviceProviderCallBack) {
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

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            ServiceProvider returnedServiceProvider = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "ServiceProviderByID");
                returnedServiceProvider = BuildRecord(result);

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

    public class ServiceProviderByServiceID extends AsyncTask<String, Void, List<ServiceProvider>> {
        int serviceId;
        int userId;
        GetServiceProviderListCallback serviceProviderListCallBack;

        public ServiceProviderByServiceID(int serviceId, int userId, GetServiceProviderListCallback serviceProviderListCallBack) {
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

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                serviceProviderList = BuildList(result);

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

    public class ServiceProviderDelete extends AsyncTask<String, Void, Void> {
        int serviceProviderId;
        GetServiceProviderCallback serviceProviderCallBack;

        public ServiceProviderDelete(int serviceProviderId, GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProviderId = serviceProviderId;
            this.serviceProviderCallBack = serviceProviderCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("serviceProviderId", serviceProviderId + ""));

            HttpParams httpRequestParams = getHttpRequestParams();

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

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            return httpRequestParams;
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

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

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
