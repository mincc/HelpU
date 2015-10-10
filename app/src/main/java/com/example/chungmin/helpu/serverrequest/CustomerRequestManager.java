package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.CustomerRequest;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import HelpUGenericUtilities.StringUtils;

/**
 * Created by Chung Min on 7/19/2015.
 * 09 Sep 2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */
public class CustomerRequestManager {

    private static String mMsg = "";

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

    public static void insert(CustomerRequest customerRequest, Callback.GetCustomerRequestCallback customerRequestCallback) {
        new Insert(customerRequest, customerRequestCallback).execute();
    }

    public static void getByID(int customerRequestId, Callback.GetCustomerRequestCallback customerRequestCallBack) {
        new GetByID(customerRequestId, customerRequestCallBack).execute();
    }

    public static void update(CustomerRequest customerRequest, Callback.GetCustomerRequestCallback customerRequestCallBack) {
        new Update(customerRequest, customerRequestCallBack).execute();
    }

    public static void getCustomerRequestNotificationTrigger(int userId, Callback.GetCustomerRequestListCallback customerRequestListCallBack) {
        new CustomerRequestNotificationTrigger(userId, customerRequestListCallBack).execute();
    }

    public static void getCustomerRequestJobOffer(int userId, String url, Callback.GetCustomerRequestCallback customerRequestCallback) {
        new CustomerRequestJobOffer(userId, customerRequestCallback).execute(url);
    }

    public static void getListByUserId(int userId, String type, Callback.GetCustomerRequestListCallback customerRequestListCallBack) {
        new GetListByUserId(userId, type, customerRequestListCallBack).execute();
    }

    /**
     * ===========================================================================================
     * ===========================================================================================
     */

    public static class Insert extends AsyncTask<String, Void, CustomerRequest> {
        CustomerRequest customerRequest;
        Callback.GetCustomerRequestCallback customerRequestCallBack;

        public Insert(CustomerRequest customerRequest, Callback.GetCustomerRequestCallback customerRequestCallBack) {
            this.customerRequest = customerRequest;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            mMsg = "";
            if(params == null)
                return null;
            String url = AppLink.getCustomerRequestInsertUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "Insert");
                returnedCustomerRequest = buildRecord(result);

            } catch (JSONException e) {
                mMsg = "Invalid Response";
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            if (customerRequestCallBack != null) {
                if (mMsg.equals("")) {
                    customerRequestCallBack.complete(returnedCustomerRequest);
                } else {
                    customerRequestCallBack.failure(mMsg);
                }
            }
        }

    }

    public static class GetByID extends AsyncTask<String, Void, CustomerRequest> {
        int customerRequestId;
        Callback.GetCustomerRequestCallback customerRequestCallBack;

        public GetByID(int customerRequestId, Callback.GetCustomerRequestCallback customerRequestCallBack) {
            this.customerRequestId = customerRequestId;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            mMsg = "";
            if(params == null)
                return null;
            String url = AppLink.getCustomerRequestGetByIDUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                if (result.equals("null")) {
                    mMsg = "No Item Found";
                    return null;
                }

                Log.v("happened", "GetByID");
                returnedCustomerRequest = buildRecord(result);

            } catch (JSONException e) {
                mMsg = "Invalid Response";
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            if (customerRequestCallBack != null) {
                if (mMsg.equals("")) {
                    customerRequestCallBack.complete(returnedCustomerRequest);
                } else {
                    customerRequestCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class Update extends AsyncTask<String, Void, Void> {
        CustomerRequest customerRequest;
        Callback.GetCustomerRequestCallback customerRequestCallBack;

        public Update(CustomerRequest customerRequest, Callback.GetCustomerRequestCallback customerRequestCallBack) {
            this.customerRequest=customerRequest;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if(params == null)
                return null;
            String url = AppLink.getCustomerRequestUpdateUrl();

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
            if (customerRequestCallBack != null) {
                if (mMsg.equals("")) {
                    customerRequestCallBack.complete(null);
                } else {
                    customerRequestCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class CustomerRequestNotificationTrigger extends AsyncTask<String, Void, List<CustomerRequest>> {
        int userId;
        Callback.GetCustomerRequestListCallback customerRequestListCallBack;

        public CustomerRequestNotificationTrigger(int userId, Callback.GetCustomerRequestListCallback customerRequestListCallBack) {
            this.userId = userId;
            this.customerRequestListCallBack = customerRequestListCallBack;
        }

        @Override
        protected List<CustomerRequest> doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getCustomerRequestNotificationTriggerUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                customerRequestList = buildList(result);

            } catch (JSONException e) {
                mMsg = "Invalid Response";
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return customerRequestList;
        }

        @Override
        protected void onPostExecute(List<CustomerRequest> returnedCustomerListRequest) {
            super.onPostExecute(returnedCustomerListRequest);
            if (customerRequestListCallBack != null) {
                if (mMsg.equals("")) {
                    customerRequestListCallBack.complete(returnedCustomerListRequest);
                } else {
                    customerRequestListCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class CustomerRequestJobOffer extends AsyncTask<String, Void, CustomerRequest> {
        int userId;
        Callback.GetCustomerRequestCallback customerRequestCallBack;

        public CustomerRequestJobOffer(int userId, Callback.GetCustomerRequestCallback customerRequestCallBack) {
            this.userId = userId;
            this.customerRequestCallBack = customerRequestCallBack;
        }

        @Override
        protected CustomerRequest doInBackground(String... params) {
            mMsg = "";
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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "CustomerRequestJobOffer");
                returnedCustomerRequest = buildRecord(result);

            } catch (JSONException e) {
                mMsg = "Invalid Response";
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return returnedCustomerRequest;
        }

        @Override
        protected void onPostExecute(CustomerRequest returnedCustomerRequest) {
            super.onPostExecute(returnedCustomerRequest);
            if (customerRequestCallBack != null) {
                if (mMsg.equals("")) {
                    customerRequestCallBack.complete(returnedCustomerRequest);
                } else {
                    customerRequestCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class GetListByUserId extends AsyncTask<String, Void, List<CustomerRequest>> {
        private final Callback.GetCustomerRequestListCallback listener;
        private final int userId;
        private final String type;

        public GetListByUserId(int userId, String type, Callback.GetCustomerRequestListCallback listener) {
            this.userId = userId;
            this.type = type;
            this.listener = listener;
        }

        @Override
        protected List<CustomerRequest> doInBackground(String... params) {
            mMsg = "";
            List<CustomerRequest> customerRequestList = new ArrayList<CustomerRequest>();
            ;

            if (params == null) return null;
            String url = "";
            if (type.equals("JobOfferList")) {
                url = AppLink.getCustomerRequestJobListGetByUserIDUrl();
            } else if (type.equals("JobDoneList")) {
                url = AppLink.getCustomerRequestJobDoneListGetByUserIDUrl();
            } else if (type.equals("HireList")) {
                url = AppLink.getCustomerRequestHireListGetByUserIDUrl();
            }

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", userId + ""));

            try {
                HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(url);

                // connect
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = client.execute(post);

                // get response
                HttpEntity entity = response.getEntity();

                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                customerRequestList = buildList(result);

            } catch (JSONException e) {
                mMsg = "Invalid Response";
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return customerRequestList;
        }

        @Override
        protected void onPostExecute(List<CustomerRequest> returnedCustomerListRequest) {
            super.onPostExecute(returnedCustomerListRequest);
            if (listener != null) {
                if (mMsg.equals("")) {
                    listener.complete(returnedCustomerListRequest);
                } else {
                    listener.failure(mMsg);
                }
            }
        }

    }
}
