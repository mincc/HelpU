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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class CustomerRequestServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public CustomerRequestServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeCustomerRequestDataInBackground(CustomerRequest customerRequest, String url, GetCustomerRequestCallback customerRequestCallback) {
        progressDialog.show();
        new StoreCustomerRequestDataAsyncTask(customerRequest, customerRequestCallback).execute(url);
    }

    public void getCustomerRequestByID(int customerRequestId, String url, GetCustomerRequestCallback customerRequestCallBack) {
        progressDialog.show();
        new CustomerRequestByID(customerRequestId, customerRequestCallBack).execute(url);
    }

    public void getCustomerRequestUpdate(CustomerRequest customerRequest, String url, GetCustomerRequestCallback customerRequestCallBack) {
        //progressDialog.show();
        new CustomerRequestUpdate(customerRequest, customerRequestCallBack).execute(url);
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
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0) {
                    Log.v("happened", "Store Customer Request Data");

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
            progressDialog.dismiss();
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
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Get Customer Request By ID");

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

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            customerRequestCallBack.done(null);
        }
    }
}
