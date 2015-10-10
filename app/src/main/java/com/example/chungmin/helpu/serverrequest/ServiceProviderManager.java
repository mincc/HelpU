package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.ServiceProvider;
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

import static HelpUGenericUtilities.DateTimeUtils.parseTo;


/**
 * Created by Chung Min on 7/19/2015.
 * 09 Sep 2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */
public class ServiceProviderManager {

    private static String mMsg = "";

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
        target.setLastOnline(parseTo(json.getString("lastOnline")));
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

    public static void insert(ServiceProvider serviceProvider, Callback.GetServiceProviderCallback serviceProviderCallback) {
        new Insert(serviceProvider, serviceProviderCallback).execute();
    }

    public static void getByID(int serviceProviderId, Callback.GetServiceProviderCallback serviceProviderCallBack) {
        new GetByID(serviceProviderId, serviceProviderCallBack).execute();
    }

    public static void getByServiceID(int serviceId, int userId, Callback.GetServiceProviderListCallback serviceProviderListCallBack) {
        new GetByServiceID(serviceId, userId, serviceProviderListCallBack).execute();
    }

    public static void delete(int serviceProviderId, int isLogicalDelete, Callback.GetServiceProviderCallback serviceProviderCallback) {
        new Delete(serviceProviderId, isLogicalDelete, serviceProviderCallback).execute();
    }

    public static void isServiceProviderAlreadyExists(int userId, int serviceId, Callback.GetBooleanCallback booleanCallBack) {
        new IsServiceProviderAlreadyExists(userId, serviceId, booleanCallBack).execute();
    }

    public static void getListByUserId(int userId, Callback.GetServiceProviderListCallback serviceProviderListCallBack) {
        new GetListByUserId(userId, serviceProviderListCallBack).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public static class Insert extends AsyncTask<String, Void, Void> {
        ServiceProvider serviceProvider;
        Callback.GetServiceProviderCallback serviceProviderCallBack;

        public Insert(ServiceProvider serviceProvider, Callback.GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProvider = serviceProvider;
            this.serviceProviderCallBack = serviceProviderCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if(params == null)
                return null;
            String url = AppLink.getServiceProviderInsertUrl();

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
            if (serviceProviderCallBack != null) {
                if (mMsg.equals("")) {
                    serviceProviderCallBack.complete(null);
                } else {
                    serviceProviderCallBack.failure(mMsg);
                }
            }
        }

    }

    public static class GetByID extends AsyncTask<String, Void, ServiceProvider> {
        int serviceProviderId;
        Callback.GetServiceProviderCallback serviceProviderCallBack;

        public GetByID(int serviceProviderId, Callback.GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProviderId = serviceProviderId;
            this.serviceProviderCallBack = serviceProviderCallBack;
        }

        @Override
        protected ServiceProvider doInBackground(String... params) {
            mMsg = "";
            if(params == null)
                return null;
            String url = AppLink.getServiceProviderGetByIDUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "GetByID");
                returnedServiceProvider = buildRecord(result);

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

            return returnedServiceProvider;
        }

        @Override
        protected void onPostExecute(ServiceProvider returnedServiceProvider) {
            super.onPostExecute(returnedServiceProvider);
            if (serviceProviderCallBack != null) {
                if (mMsg.equals("")) {
                    serviceProviderCallBack.complete(returnedServiceProvider);
                } else {
                    serviceProviderCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class GetByServiceID extends AsyncTask<String, Void, List<ServiceProvider>> {
        int serviceId;
        int userId;
        Callback.GetServiceProviderListCallback serviceProviderListCallBack;

        public GetByServiceID(int serviceId, int userId, Callback.GetServiceProviderListCallback serviceProviderListCallBack) {
            this.serviceId = serviceId;
            this.userId = userId;
            this.serviceProviderListCallBack = serviceProviderListCallBack;
        }

        @Override
        protected List<ServiceProvider> doInBackground(String... params) {
            mMsg = "";
            // create serviceProviders list
            List<ServiceProvider> serviceProviderList = new ArrayList<ServiceProvider>();

            if(params == null)
                return null;
            String url = AppLink.getServiceProviderGetByServiceIDUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                serviceProviderList = buildList(result);

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

            return serviceProviderList;
        }

        @Override
        protected void onPostExecute(List<ServiceProvider> returnedServiceProviderList) {
            super.onPostExecute(returnedServiceProviderList);
            if (serviceProviderListCallBack != null) {
                if (mMsg.equals("")) {
                    serviceProviderListCallBack.complete(returnedServiceProviderList);
                } else {
                    serviceProviderListCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class Delete extends AsyncTask<String, Void, Void> {
        int serviceProviderId;
        Callback.GetServiceProviderCallback serviceProviderCallBack;
        int isLogicalDelete;

        public Delete(int serviceProviderId, int isLogicalDelete, Callback.GetServiceProviderCallback serviceProviderCallBack) {
            this.serviceProviderId = serviceProviderId;
            this.serviceProviderCallBack = serviceProviderCallBack;
            this.isLogicalDelete = isLogicalDelete;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.serviceProviderDeleteUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("serviceProviderId", serviceProviderId + ""));
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
            if (serviceProviderCallBack != null) {
                if (mMsg.equals("")) {
                    serviceProviderCallBack.complete(null);
                } else {
                    serviceProviderCallBack.failure(mMsg);
                }
            }
        }

    }

    public static class IsServiceProviderAlreadyExists extends AsyncTask<String, Void, Boolean> {
        int userId;
        int serviceId;
        Callback.GetBooleanCallback booleanCallBack;

        public IsServiceProviderAlreadyExists(int userId, int serviceId, Callback.GetBooleanCallback booleanCallBack) {
            this.userId = userId;
            this.serviceId = serviceId;
            this.booleanCallBack = booleanCallBack;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mMsg = "";
            boolean isTrigger = false;
            if(params == null)
                return false;

            // get url from params
            String url = AppLink.serviceProviderIsServiceAlreadyExistsUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);

                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Function IsServiceProviderAlreadyExists Call");
                    return isTrigger = jObject.getBoolean("result");

                }
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
            return isTrigger;
        }

        @Override
        protected void onPostExecute(Boolean isTrigger) {
            super.onPostExecute(isTrigger);
            if (booleanCallBack != null) {
                if (mMsg.equals("")) {
                    booleanCallBack.complete(isTrigger);
                } else {
                    booleanCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class GetListByUserId extends AsyncTask<String, Void, List<ServiceProvider>> {
        private final Callback.GetServiceProviderListCallback listener;
        private final int userId;
        private String msg;

        public GetListByUserId(int userId, Callback.GetServiceProviderListCallback serviceProviderListCallBack) {
            this.userId = userId;
            this.listener = serviceProviderListCallBack;
        }

        @Override
        protected List<ServiceProvider> doInBackground(String... params) {
            mMsg = "";
            List<ServiceProvider> serviceProviderList = new ArrayList<ServiceProvider>();

            if (params == null) return null;

            // get url from params
            String url = AppLink.getServiceProviderGetByUserIDUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", userId + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                // connect
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = client.execute(post);

                // get response
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    msg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                serviceProviderList = buildList(result);

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

            return serviceProviderList;
        }

        @Override
        protected void onPostExecute(List<ServiceProvider> returnedServiceProviderList) {
            super.onPostExecute(returnedServiceProviderList);
            if (listener != null) {
                if (mMsg.equals("")) {
                    listener.complete(returnedServiceProviderList);
                } else {
                    listener.failure(mMsg);
                }
            }
        }


    }
}
