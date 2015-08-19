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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import HelpUGenericUtilities.StringUtils;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class ServiceProviderServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

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

    public void getServiceProviderByServiceID(int serviceId, int userId, String url, GetServiceProviderListCallback serviceProviderListCallBack) {
        progressDialog.show();
        new ServiceProviderByServiceID(serviceId, userId, serviceProviderListCallBack).execute(url);
    }

    public void getServiceProviderIsNotificationTrigger(int userId, String url, GetBooleanCallback booleanCallBack) {
        //progressDialog.show();
        new ServiceProviderIsNotificationTrigger(userId, booleanCallBack).execute(url);
    }

    public void getServiceProviderJobOffer(int userId, String url, GetCustomerRequestCallback customerRequestCallback) {
        //progressDialog.show();
        new ServiceProviderJobOffer(userId, customerRequestCallback).execute(url);
    }

    public void getServiceProviderWinAward(int userId, String url, GetCustomerRequestCallback customerRequestCallback) {
        //progressDialog.show();
        new ServiceProviderWinAward(userId, customerRequestCallback).execute(url);
    }

    public void getServiceProviderTotalJobOffer(int userId, String url,  CountListener listener) {
        //progressDialog.show();
        new ServiceProviderTotalJobOffer(userId, listener).execute(url);
    }

    public void serviceProviderDelete(int serviceProviderId, String url, GetServiceProviderCallback serviceProviderCallback) {
        progressDialog.show();
        new ServiceProviderDelete(serviceProviderId, serviceProviderCallback).execute(url);
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
                    double avgRatedValue = jObject.getDouble("avgRatedValue");

                    returnedServiceProvider = new ServiceProvider(
                            serviceProviderId,
                            userId,
                            userName,
                            serviceId,
                            serviceName,
                            phone,
                            email,
                            avgRatedValue);
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
            List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();

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
                    serviceProvider.setAvgRatedValue(json.getDouble("avgRatedValue"));
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

    public class ServiceProviderIsNotificationTrigger extends AsyncTask<String, Void, Boolean> {
        int userId;
        GetBooleanCallback booleanCallBack;

        public ServiceProviderIsNotificationTrigger(int userId, GetBooleanCallback booleanCallBack) {
            this.userId = userId;
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
                    Log.v("happened", "Service Provider Is Notification Job Offer Trigger");

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
            progressDialog.dismiss();
            booleanCallBack.done(isTrigger);
        }
    }

    public class ServiceProviderJobOffer extends AsyncTask<String, Void, CustomerRequest> {
        int userId;
        GetCustomerRequestCallback consumerRequestCallBack;

        public ServiceProviderJobOffer(int userId, GetCustomerRequestCallback consumerRequestCallBack) {
            this.userId = userId;
            this.consumerRequestCallBack = consumerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            boolean isTrigger = false;
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", this.userId + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerRequest returnedCustomerRequest = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Service Provider Job Offer");

                    int customerRequestId = jObject.getInt("customerRequestId");
                    int serviceId = jObject.getInt("serviceId");
                    int userId = jObject.getInt("userId");
                    String description = jObject.getString("description");
                    int projectStatusId = jObject.getInt("projectStatusId");
                    String userName = jObject.getString("userName");
                    String serviceName = jObject.getString("serviceName");
                    int serviceProviderId = jObject.getInt("serviceProviderId");
                    double quotation = jObject.getDouble("quotation");

                    returnedCustomerRequest = new CustomerRequest(
                            customerRequestId,
                            serviceId,
                            serviceName,
                            userId,
                            description,
                            ProjectStatus.values()[projectStatusId],
                            userName,
                            serviceName,
                            serviceProviderId,
                            quotation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            progressDialog.dismiss();
            consumerRequestCallBack.done(returnedCustomerRequest);
        }
    }

    public class ServiceProviderWinAward extends AsyncTask<String, Void, CustomerRequest> {
        int userId;
        GetCustomerRequestCallback consumerRequestCallBack;

        public ServiceProviderWinAward(int userId, GetCustomerRequestCallback consumerRequestCallBack) {
            this.userId = userId;
            this.consumerRequestCallBack = consumerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            boolean isTrigger = false;
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", this.userId + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerRequest returnedCustomerRequest = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Service Provider Job Offer");

                    int customerRequestId = jObject.getInt("customerRequestId");
                    int serviceId = jObject.getInt("serviceId");
                    int userId = jObject.getInt("userId");
                    String description = jObject.getString("description");
                    int projectStatusId = jObject.getInt("projectStatusId");
                    String userName = jObject.getString("userName");
                    String serviceName = jObject.getString("serviceName");
                    int serviceProviderId = jObject.getInt("serviceProviderId");
                    double quotation = jObject.getDouble("quotation");

                    returnedCustomerRequest = new CustomerRequest(
                            customerRequestId,
                            serviceId,
                            serviceName,
                            userId,
                            description,
                            ProjectStatus.values()[projectStatusId],
                            userName,
                            serviceName,
                            serviceProviderId,
                            quotation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            progressDialog.dismiss();
            consumerRequestCallBack.done(returnedCustomerRequest);
        }
    }

    public class ServiceProviderTotalJobOffer extends AsyncTask<String, Void, String> {
        private CountListener listener;
        int userId;
        private String msg;
        public static final int CONNECTION_TIMEOUT = 1000 * 15;

        public ServiceProviderTotalJobOffer(int userId, CountListener listener) {
            this.userId = userId;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            if(params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", Integer.toString(userId)));

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
                if(listener != null) listener.CountFailure((msg));
                return;
            }

            try {
                // convert json string to json array
                JSONObject jsonObj = new JSONObject(sJson);
                int count = 0;

                count = Integer.parseInt(jsonObj.get("Total").toString());

                //notify the activity that fetch data has been complete
                if(listener != null)
                    listener.CountComplete(count);

            } catch (JSONException e) {
                msg = "Invalid response";
                if(listener != null) listener.CountFailure(msg);
                return;
            }
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
            progressDialog.dismiss();
            serviceProviderCallBack.done(null);
        }

    }

}
