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
public class UserServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public UserServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(context.getString(R.string.strProcessing));
        progressDialog.setMessage(context.getString(R.string.strPlsWait));
    }

    public void storeUserDataInBackground(User user, String url, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute(url);
    }

    public void fetchUserDataAsyncTask(User user, String url, GetUserCallback userCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute(url);
    }

    public void getUserByID(int userId, String url, GetUserCallback userCallBack) {
        new UserByID(userId, userCallBack).execute(url);
    }

    public void getUserUpdate(User user, String url, GetUserCallback userCallBack) {
        new UserUpdate(user, userCallBack).execute(url);
    }

    public void isUsernameExists(String username, String url, GetBooleanCallback booleanCallback) {
        new IsUsernameExists(username, booleanCallback).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation activity_notification_job_offer of the background computation
     */

    public class StoreUserDataAsyncTask extends AsyncTask<String, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", user.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("name", user.getUserName()));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userContact", user.getUserContact()));
            dataToSend.add(new BasicNameValuePair("userEmail", user.getUserEmail()));

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
            userCallBack.done(null);
        }

    }

    public class fetchUserDataAsyncTask extends AsyncTask<String, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(String... params) {
            if(params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userId", Integer.toString(user.getUserId())));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "fetchUserDataAsyncTask");
                    String name = jObject.getString("name");
                    int userId = jObject.getInt("userId");
                    String userContact = jObject.getString("userContact");
                    String userEmail = jObject.getString("userEmail");

                    returnedUser = new User(userId, name, user.getUsername(), "", userContact, userEmail);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    public class UserByID extends AsyncTask<String, Void, User> {
        int userId;
        GetUserCallback userCallBack;

        public UserByID(int userId, GetUserCallback userCallBack) {
            this.userId = userId;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(String... params) {
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

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "Get Customer Request By ID");

                    int userId = jObject.getInt("userId");
                    String name = jObject.getString("name");

                    returnedUser = new User(
                            userId,
                            name);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    public class UserUpdate extends AsyncTask<String, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public UserUpdate(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", this.user.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("name", this.user.getUserName()));
            dataToSend.add(new BasicNameValuePair("username", this.user.getUsername()));
            dataToSend.add(new BasicNameValuePair("userContact", this.user.getUserContact()));
            dataToSend.add(new BasicNameValuePair("userEmail", this.user.getUserEmail()));

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
            userCallBack.done(null);
        }
    }

    public class IsUsernameExists extends AsyncTask<String, Void, Boolean> {
        String username;
        GetBooleanCallback booleanCallBack;

        public IsUsernameExists(String username, GetBooleanCallback booleanCallBack) {
            this.username = username;
            this.booleanCallBack = booleanCallBack;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean isUsernameExists = false;
            if (params == null)
                return false;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", this.username));

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

                if (jObject.length() != 0) {
                    Log.v("happened", "Function IsUsernameExists Call");

                    return isUsernameExists = jObject.getBoolean("result");

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return isUsernameExists;
        }

        @Override
        protected void onPostExecute(Boolean isUsernameExists) {
            super.onPostExecute(isUsernameExists);
            booleanCallBack.done(isUsernameExists);
        }
    }

}
