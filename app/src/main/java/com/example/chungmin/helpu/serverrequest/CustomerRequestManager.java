package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.GetCustomerRequestCallback;
import com.example.chungmin.helpu.callback.GetCustomerRequestListCallback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.CustomerRequest;
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
public class CustomerRequestManager {

    public CustomerRequestManager() {
    }

    private static CustomerRequest parseJson(JSONObject json) throws JSONException {
        CustomerRequest target = new CustomerRequest();
        target.setCustomerRequestId(json.getInt("customerRequestId"));
        target.setServiceId(json.getInt("serviceId"));
        target.setUserId(json.getInt("userId"));
        target.setDescription(json.getString("description"));
        target.setProjectStatus(ProjectStatus.values()[json.getInt("projectStatusId")]);
        target.setProjectStatusId(json.getInt("projectStatusId"));
        target.setUserName(json.getString("userName"));
        target.setUserEmail(json.getString("userEmail"));
        target.setUserContact(json.getString("userContact"));
        target.setServiceName(json.getString("serviceName"));
        target.setServiceProviderId(json.getInt("serviceProviderId"));
        target.setQuotation(json.getDouble("quotation"));
        target.setProjectStatusName(json.getString("projectStatusName"));
        target.setCustomerRatingValue(json.getDouble("customerRatingValue"));
        target.setServiceProviderRatingValue(json.getDouble("serviceProviderRatingValue"));
        target.setAlreadyReadNotification(json.getInt("alreadyReadNotification"));
        return target;
    }

    public static CustomerRequest buildRecord(String result) throws JSONException {

        CustomerRequest target = new CustomerRequest();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static List<CustomerRequest> buildList(String result) throws JSONException {

        List<CustomerRequest> list = new ArrayList<CustomerRequest>();
        JSONArray aJson = new JSONArray(result);
        for (int i = 0; i < aJson.length(); i++) {
            JSONObject json = aJson.getJSONObject(i);
            CustomerRequest target = parseJson(json);
            list.add(target);
        }
        return list;
    }

    public void insert(CustomerRequest customerRequest, String url, GetCustomerRequestCallback customerRequestCallback) {
        new Insert(customerRequest, customerRequestCallback).execute(url);
    }

    public void getByID(int customerRequestId, String url, GetCustomerRequestCallback customerRequestCallBack) {
        new GetByID(customerRequestId, customerRequestCallBack).execute(url);
    }

    public void update(CustomerRequest customerRequest, String url, GetCustomerRequestCallback customerRequestCallBack) {
        new Update(customerRequest, customerRequestCallBack).execute(url);
    }

    public void getCustomerRequestNotificationTrigger(int userId, String url, GetCustomerRequestListCallback customerRequestListCallBack) {
        new CustomerRequestNotificationTrigger(userId, customerRequestListCallBack).execute(url);
    }

    public void getCustomerRequestJobOffer(int userId, String url, GetCustomerRequestCallback customerRequestCallback) {
        new CustomerRequestJobOffer(userId, customerRequestCallback).execute(url);
    }
    /**
     * ===========================================================================================
     * ===========================================================================================
     */

    public class Insert extends AsyncTask<String, Void, CustomerRequest> {
        CustomerRequest customerRequest;
        GetCustomerRequestCallback customerRequestCallBack;

        public Insert(CustomerRequest customerRequest, GetCustomerRequestCallback customerRequestCallBack) {
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

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerRequest returnedCustomerRequest = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "Insert");
                returnedCustomerRequest = buildRecord(result);

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

    public class GetByID extends AsyncTask<String, Void, CustomerRequest> {
        int customerRequestId;
        GetCustomerRequestCallback customerRequestCallBack;

        public GetByID(int customerRequestId, GetCustomerRequestCallback customerRequestCallBack) {
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

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerRequest returnedCustomerRequest = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "GetByID");
                returnedCustomerRequest = buildRecord(result);

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

    public class Update extends AsyncTask<String, Void, Void> {
        CustomerRequest customerRequest;
        GetCustomerRequestCallback customerRequestCallBack;

        public Update(CustomerRequest customerRequest, GetCustomerRequestCallback customerRequestCallBack) {
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
            dataToSend.add(new BasicNameValuePair("customerRequestId", customerRequest.getCustomerRequestId() + ""));
            dataToSend.add(new BasicNameValuePair("serviceId", customerRequest.getServiceId() + ""));
            dataToSend.add(new BasicNameValuePair("userId", customerRequest.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("description", customerRequest.getDescription()));
            dataToSend.add(new BasicNameValuePair("projectStatusId", customerRequest.getProjectStatusId() + ""));
            dataToSend.add(new BasicNameValuePair("serviceProviderId", customerRequest.getServiceProviderId() + ""));
            dataToSend.add(new BasicNameValuePair("quotation", customerRequest.getQuotation() + ""));
            dataToSend.add(new BasicNameValuePair("customerRatingValue", customerRequest.getCustomerRatingValue() + ""));
            dataToSend.add(new BasicNameValuePair("serviceProviderRatingValue", customerRequest.getServiceProviderRatingValue() + ""));
            dataToSend.add(new BasicNameValuePair("alreadyReadNotification", customerRequest.isAlreadyReadNotification() + ""));

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

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            // create customerRequests list
            List<CustomerRequest> customerRequestList = new ArrayList<CustomerRequest>();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                customerRequestList = buildList(result);

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

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerRequest returnedCustomerRequest = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "CustomerRequestJobOffer");
                returnedCustomerRequest = buildRecord(result);

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
