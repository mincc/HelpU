package com.example.chungmin.helpu;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chung Min on 7/19/2015.
 * 09 Sep 2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */
public class CustomerRequestServerRequests {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public CustomerRequestServerRequests() {
    }

    public static CustomerRequest BuildRecord(String result) {

        CustomerRequest target = new CustomerRequest();
        try {
            JSONObject jObject = new JSONObject(result);
            if (jObject.length() != 0) {
                target.setCustomerRequestId(jObject.getInt("customerRequestId"));
                target.setServiceId(jObject.getInt("serviceId"));
                target.setUserId(jObject.getInt("userId"));
                target.setDescription(jObject.getString("description"));
                target.setProjectStatus(ProjectStatus.values()[jObject.getInt("projectStatusId")]);
                target.setUserName(jObject.getString("userName"));
                target.setUserEmail(jObject.getString("userEmail"));
                target.setUserContact(jObject.getString("userContact"));
                target.setServiceName(jObject.getString("serviceName"));
                target.setServiceProviderId(jObject.getInt("serviceProviderId"));
                target.setQuotation(jObject.getDouble("quotation"));
                target.setCustomerRatingValue(jObject.getDouble("customerRatingValue"));
                target.setServiceProviderRatingValue(jObject.getDouble("serviceProviderRatingValue"));
                target.setAlreadyReadNotification(jObject.getInt("alreadyReadNotification"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return target;
    }

    public static List<CustomerRequest> BuildList(String result) throws Exception {

        List<CustomerRequest> list = new ArrayList<CustomerRequest>();

        try {
            JSONArray aJson = new JSONArray(result);

            for (int i = 0; i < aJson.length(); i++) {
                JSONObject json = aJson.getJSONObject(i);
                CustomerRequest target = new CustomerRequest();
                target.setCustomerRequestId(json.getInt("customerRequestId"));
                target.setServiceId(json.getInt("serviceId"));
                target.setDescription(json.getString("description"));
                target.setUserId(json.getInt("userId"));
                target.setProjectStatusId(json.getInt("projectStatusId"));
                target.setServiceProviderId(json.getInt("serviceProviderId"));
                target.setQuotation(json.getDouble("quotation"));
                target.setUserName(json.getString("userName"));
                target.setUserEmail(json.getString("userEmail"));
                target.setUserContact(json.getString("userContact"));
                target.setServiceName(json.getString("serviceName"));
                target.setProjectStatusName(json.getString("projectStatusName"));
                target.setCustomerRatingValue(json.getDouble("customerRatingValue"));
                target.setServiceProviderRatingValue(json.getDouble("serviceProviderRatingValue"));
                target.setAlreadyReadNotification(json.getInt("alreadyReadNotification"));

                list.add(target);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("CustomerRequest - BuildList");
        }

        return list;
    }

    public void storeCustomerRequestDataInBackground(CustomerRequest customerRequest, String url, GetCustomerRequestCallback customerRequestCallback) {
        new StoreCustomerRequestDataAsyncTask(customerRequest, customerRequestCallback).execute(url);
    }

    public void getCustomerRequestByID(int customerRequestId, String url, GetCustomerRequestCallback customerRequestCallBack) {
        new CustomerRequestByID(customerRequestId, customerRequestCallBack).execute(url);
    }

    public void getCustomerRequestUpdate(CustomerRequest customerRequest, String url, GetCustomerRequestCallback customerRequestCallBack) {
        new CustomerRequestUpdate(customerRequest, customerRequestCallBack).execute(url);
    }

    public void getCustomerRequestNotificationTrigger(int userId, String url, GetCustomerRequestListCallback customerRequestListCallBack) {
        new CustomerRequestNotificationTrigger(userId, customerRequestListCallBack).execute(url);
    }

    public void getCustomerRequestJobOffer(int userId, String url, GetCustomerRequestCallback customerRequestCallback) {
        new CustomerRequestJobOffer(userId, customerRequestCallback).execute(url);
    }
    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class StoreCustomerRequestDataAsyncTask extends AsyncTask<String, Void, CustomerRequest> {
        CustomerRequest customerRequest;
        GetCustomerRequestCallback customerRequestCallBack;

        public StoreCustomerRequestDataAsyncTask(CustomerRequest customerRequest, GetCustomerRequestCallback customerRequestCallBack) {
            this.customerRequest = customerRequest;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", customerRequest.getUserId()+ ""));
            dataToSend.add(new BasicNameValuePair("serviceId", customerRequest.getServiceId()+ ""));
            dataToSend.add(new BasicNameValuePair("description", customerRequest.getDescription()));
            dataToSend.add(new BasicNameValuePair("projectStatusId", customerRequest.getProjectStatus().getId()+""));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerRequest returnedCustomerRequest = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "StoreCustomerRequestDataAsyncTask");
                returnedCustomerRequest = BuildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCustomerRequest;
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
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            customerRequestCallBack.done(returnedCustomerRequest);
        }

    }

    public class CustomerRequestByID extends AsyncTask<String, Void, CustomerRequest> {
        int customerRequestId;
        GetCustomerRequestCallback customerRequestCallBack;

        public CustomerRequestByID(int customerRequestId, GetCustomerRequestCallback customerRequestCallBack) {
            this.customerRequestId = customerRequestId;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("customerRequestId", this.customerRequestId + ""));

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
                Log.v("happened", "CustomerRequestByID");
                returnedCustomerRequest = BuildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            customerRequestCallBack.done(returnedCustomerRequest);
        }
    }

    public class CustomerRequestUpdate extends AsyncTask<String, Void, Void> {
        CustomerRequest customerRequest;
        GetCustomerRequestCallback customerRequestCallBack;

        public CustomerRequestUpdate(CustomerRequest customerRequest, GetCustomerRequestCallback customerRequestCallBack) {
            this.customerRequest=customerRequest;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("customerRequestId", this.customerRequest.getCustomerRequestId() + ""));
            dataToSend.add(new BasicNameValuePair("serviceId", this.customerRequest.getServiceId() + ""));
            dataToSend.add(new BasicNameValuePair("userId", this.customerRequest.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("description", this.customerRequest.getDescription()));
            dataToSend.add(new BasicNameValuePair("projectStatusId", this.customerRequest.getProjectStatusId() + ""));
            dataToSend.add(new BasicNameValuePair("serviceProviderId", this.customerRequest.getServiceProviderId() + ""));
            dataToSend.add(new BasicNameValuePair("quotation", this.customerRequest.getQuotation() + ""));
            dataToSend.add(new BasicNameValuePair("customerRatingValue", this.customerRequest.getCustomerRatingValue() + ""));
            dataToSend.add(new BasicNameValuePair("serviceProviderRatingValue", this.customerRequest.getServiceProviderRatingValue() + ""));
            dataToSend.add(new BasicNameValuePair("alreadyReadNotification", this.customerRequest.isAlreadyReadNotification() + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
//                HttpResponse httpResponse = client.execute(post);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            customerRequestCallBack.done(null);
        }
    }

    public class CustomerRequestNotificationTrigger extends AsyncTask<String, Void, List<CustomerRequest>> {
        int userId;
        GetCustomerRequestListCallback customerRequestListCallBack;

        public CustomerRequestNotificationTrigger(int userId, GetCustomerRequestListCallback customerRequestListCallBack) {
            this.userId = userId;
            this.customerRequestListCallBack = customerRequestListCallBack;
        }

        @Override
        protected List<CustomerRequest> doInBackground(String... params) {
            if (params == null)
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

            // create customerRequests list
            List<CustomerRequest> customerRequestList = new ArrayList<CustomerRequest>();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                customerRequestList = BuildList(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return customerRequestList;
        }

        @Override
        protected void onPostExecute(List<CustomerRequest> returnedCustomerListRequest) {
            super.onPostExecute(returnedCustomerListRequest);
            customerRequestListCallBack.Complete(returnedCustomerListRequest);
        }
    }

    public class CustomerRequestJobOffer extends AsyncTask<String, Void, CustomerRequest> {
        int userId;
        GetCustomerRequestCallback consumerRequestCallBack;

        public CustomerRequestJobOffer(int userId, GetCustomerRequestCallback consumerRequestCallBack) {
            this.userId = userId;
            this.consumerRequestCallBack = consumerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            boolean isTrigger = false;
            if (params == null)
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
                Log.v("happened", "CustomerRequestJobOffer");
                returnedCustomerRequest = BuildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            consumerRequestCallBack.done(returnedCustomerRequest);
        }
    }

}
