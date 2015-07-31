package com.example.chungmin.helpu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class ServiceProviderServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    //public static final String SERVER_ADDRESS = "http://helpu.hostei.com/";

    public ServiceProviderServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeServiceProviderDataInBackground(ServiceProvider serviceProvider, String url, GetServiceProviderCallback serviceProviderCallback) {
        progressDialog.show();
        new StoreServiceProviderDataAsyncTask(serviceProvider, serviceProviderCallback).execute(url);
    }

    public void getServiceProviderByID(int serviceProviderId, String url, GetServiceProviderCallback serviceProviderCallBack) {
        progressDialog.show();
        new ServiceProviderByID(serviceProviderId, serviceProviderCallBack).execute(url);
    }

    public void getServiceProviderByServiceID(int serviceId, String url, GetServiceProviderListCallback serviceProviderListCallBack) {
        progressDialog.show();
        new ServiceProviderByServiceID(serviceId, serviceProviderListCallBack).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
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
            progressDialog.dismiss();
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
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Get Service Provider By ID");

                    int serviceProviderId = jObject.getInt("serviceProviderId");
                    int userId = jObject.getInt("userId");
                    String userName = jObject.getString("userName");
                    int serviceId = jObject.getInt("serviceId");
                    String serviceName = jObject.getString("serviceName");
                    String phone = jObject.getString("phone");
                    String email = jObject.getString("email");

                    returnedServiceProvider = new ServiceProvider(
                            serviceProviderId,
                            userId,
                            userName,
                            serviceId,
                            serviceName,
                            phone,
                            email);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedServiceProvider;
        }

        @Override
        protected void onPostExecute(ServiceProvider returnedServiceProvider) {
            super.onPostExecute(returnedServiceProvider);
            progressDialog.dismiss();
            serviceProviderCallBack.done(returnedServiceProvider);
        }
    }

    public class ServiceProviderByServiceID extends AsyncTask<String, Void, List<ServiceProvider>> {
        int serviceId;
        GetServiceProviderListCallback serviceProviderListCallBack;

        public ServiceProviderByServiceID(int serviceId, GetServiceProviderListCallback serviceProviderListCallBack) {
            this.serviceId = serviceId;
            this.serviceProviderListCallBack = serviceProviderListCallBack;
        }

        @Override
        protected List<ServiceProvider> doInBackground(String... params) {
            // create serviceProviders list
            List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();

            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
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

                // convert json string to json array
                JSONArray aJson = new JSONArray(result);

                for(int i=0; i<aJson.length(); i++) {
                    JSONObject json = aJson.getJSONObject(i);
                    ServiceProvider serviceProvider = new ServiceProvider();
                    serviceProvider.setServiceProviderId(Integer.parseInt(json.getString("serviceProviderId")));
                    serviceProvider.setUserId(Integer.parseInt(json.getString("userId")));
                    serviceProvider.setServiceId(Integer.parseInt(json.getString("serviceId")));
                    serviceProvider.setPhone(json.getString("phone"));
                    serviceProvider.setEmail(json.getString("email"));
                    serviceProvider.setUserName(json.getString("userName"));
                    serviceProvider.setServiceName(json.getString("serviceName"));

                    // add the serviceProvider to serviceProviders list
                    serviceProviders.add(serviceProvider);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return serviceProviders;
        }

        @Override
        protected void onPostExecute(List<ServiceProvider> returnedServiceProviderList) {
            super.onPostExecute(returnedServiceProviderList);
            progressDialog.dismiss();
            serviceProviderListCallBack.done(returnedServiceProviderList);
        }
    }
}
