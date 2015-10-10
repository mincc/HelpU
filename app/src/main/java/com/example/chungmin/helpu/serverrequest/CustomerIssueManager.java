package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.CustomerIssueStatus;
import com.example.chungmin.helpu.enumeration.CustomerIssueTypes;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.CustomerIssue;
import com.example.chungmin.helpu.serverrequest.common.ServerUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class CustomerIssueManager {
    private static String mMsg = "";

    public CustomerIssueManager() {

    }

    private static CustomerIssue parseJson(JSONObject json) throws JSONException {
        CustomerIssue target = new CustomerIssue();
        target.setCustomerIssueId(json.getInt("customerIssueId"));
        target.setUserId(json.getInt("userId"));
        target.setCustomerIssueStatus(CustomerIssueStatus.values()[json.getInt("customerIssueStatusId")]);
        target.setCustomerIssueTypes(CustomerIssueTypes.values()[json.getInt("customerIssueTypeId")]);
        target.setSubject(json.getString("subject"));
        target.setDescription(json.getString("description"));
        return target;
    }

    public static CustomerIssue buildRecord(String result) throws JSONException {

        CustomerIssue target = new CustomerIssue();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static List<CustomerIssue> buildList(String result) throws JSONException {

        List<CustomerIssue> list = new ArrayList<CustomerIssue>();
        JSONArray aJson = new JSONArray(result);
        for (int i = 0; i < aJson.length(); i++) {
            JSONObject json = aJson.getJSONObject(i);
            CustomerIssue target = parseJson(json);
            list.add(target);
        }
        return list;
    }

    public static void insert(CustomerIssue customerIssue, Callback.GetCustomerIssueCallback customerIssueCallBack) {
        new Insert(customerIssue, customerIssueCallBack).execute();
    }

    public static void getByID(int customerIssueId, Callback.GetCustomerIssueCallback customerIssueCallBack) {
        new GetByID(customerIssueId, customerIssueCallBack).execute();
    }

    public static void update(CustomerIssue customerIssue, Callback.GetCustomerIssueCallback customerIssueCallBack) {
        new Update(customerIssue, customerIssueCallBack).execute();
    }

    public static void delete(int customerIssueId, int isLogicalDelete, Callback.GetCustomerIssueCallback customerIssueCallback) {
        new Delete(customerIssueId, isLogicalDelete, customerIssueCallback).execute();
    }


    /**
     * ******************************************************************************************
     * *****************************************************************************************
     */

    public static class Insert extends AsyncTask<String, Void, CustomerIssue> {
        CustomerIssue customerIssue;
        Callback.GetCustomerIssueCallback customerIssueCallBack;

        public Insert(CustomerIssue customerIssue, Callback.GetCustomerIssueCallback customerIssueCallBack) {
            this.customerIssue = customerIssue;
            this.customerIssueCallBack = customerIssueCallBack;
        }

        @Override
        protected CustomerIssue doInBackground(String... params) {
            mMsg = "";
            if (params == null) return null;
            String url = AppLink.getCustomerIssueInsertUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", customerIssue.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("customerIssueTypeId", customerIssue.getCustomerIssueTypeId() + ""));
            dataToSend.add(new BasicNameValuePair("customerIssueStatusId", customerIssue.getCustomerIssueStatusId() + ""));
            dataToSend.add(new BasicNameValuePair("subject", customerIssue.getSubject()));
            dataToSend.add(new BasicNameValuePair("description", customerIssue.getDescription()));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerIssue returnedCustomerIssue = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.v("happened", "Get Customer Request By ID");
                returnedCustomerIssue = buildRecord(result);
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return returnedCustomerIssue;
        }

        @Override
        protected void onPostExecute(CustomerIssue result) {
            super.onPostExecute(result);
            if (customerIssueCallBack != null) {
                if (mMsg.equals("")) {
                    customerIssueCallBack.complete(result);
                } else {
                    customerIssueCallBack.failure(mMsg);
                }
            }
        }

    }

    public static class GetByID extends AsyncTask<String, Void, CustomerIssue> {
        int customerIssueId;
        Callback.GetCustomerIssueCallback customerIssueCallBack;

        public GetByID(int customerIssueId, Callback.GetCustomerIssueCallback customerIssueCallBack) {
            this.customerIssueId = customerIssueId;
            this.customerIssueCallBack = customerIssueCallBack;
        }

        @Override
        protected CustomerIssue doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getCustomerIssueGetByIDUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("customerIssueId", this.customerIssueId + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            CustomerIssue returnedCustomerIssue = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.v("happened", "CustomerIssueManager - GetByID");
                returnedCustomerIssue = buildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCustomerIssue;
        }

        @Override
        protected void onPostExecute(CustomerIssue returnedCustomerIssue) {
            super.onPostExecute(returnedCustomerIssue);
            if (customerIssueCallBack != null) {
                if (mMsg.equals("")) {
                    customerIssueCallBack.complete(returnedCustomerIssue);
                } else {
                    customerIssueCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class Update extends AsyncTask<String, Void, Void> {
        CustomerIssue customerIssue;
        Callback.GetCustomerIssueCallback customerIssueCallBack;

        public Update(CustomerIssue customerIssue, Callback.GetCustomerIssueCallback customerIssueCallBack) {
            this.customerIssue = customerIssue;
            this.customerIssueCallBack = customerIssueCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getCustomerIssueUpdateUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("customerIssueId", customerIssue.getCustomerIssueId() + ""));
            dataToSend.add(new BasicNameValuePair("userId", customerIssue.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("customerIssueTypeId", customerIssue.getCustomerIssueTypeId() + ""));
            dataToSend.add(new BasicNameValuePair("customerIssueStatusId", customerIssue.getCustomerIssueStatusId() + ""));
            dataToSend.add(new BasicNameValuePair("subject", customerIssue.getSubject()));
            dataToSend.add(new BasicNameValuePair("description", customerIssue.getDescription()));
            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (customerIssueCallBack != null) {
                if (mMsg.equals("")) {
                    customerIssueCallBack.complete(null);
                } else {
                    customerIssueCallBack.failure(mMsg);
                }
            }
        }
    }

    private static class Delete extends AsyncTask<String, Void, Void> {
        int customerIssueId;
        Callback.GetCustomerIssueCallback customerIssueCallBack;
        int isLogicalDelete;

        public Delete(int customerIssueId, int isLogicalDelete, Callback.GetCustomerIssueCallback customerIssueCallBack) {
            this.customerIssueId = customerIssueId;
            this.customerIssueCallBack = customerIssueCallBack;
            this.isLogicalDelete = isLogicalDelete;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getCustomerIssueDeleteUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("customerIssueId", customerIssueId + ""));
            dataToSend.add(new BasicNameValuePair("isLogicalDelete", isLogicalDelete + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (customerIssueCallBack != null) {
                if (mMsg.equals("")) {
                    customerIssueCallBack.complete(null);
                } else {
                    customerIssueCallBack.failure(mMsg);
                }
            }
        }
    }

}
