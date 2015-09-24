package com.example.chungmin.helpu.serverrequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.callback.GetBooleanCallback;
import com.example.chungmin.helpu.callback.GetUserCallback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.serverrequest.common.ServerUtils;
import com.example.chungmin.helpu.service.BootComplete;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class UserManager {
    ProgressDialog progressDialog;
    private String mMsg = "";

    public UserManager(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(context.getString(R.string.strProcessing));
        progressDialog.setMessage(context.getString(R.string.strPlsWait));
    }

    private static User parseJson(JSONObject json) throws JSONException {
        User target = new User();
        target.setUserName(json.getString("name"));
        target.setUserId(json.getInt("userId"));
        target.setUserContact(json.getString("userContact"));
        target.setUserEmail(json.getString("userEmail"));
        target.setUsername(json.getString("username"));
        return target;
    }

    public static User buildRecord(String result) throws JSONException {

        User target = new User();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static List<User> buildList(String result) throws JSONException {

        List<User> list = new ArrayList<User>();
        JSONArray aJson = new JSONArray(result);
        for (int i = 0; i < aJson.length(); i++) {
            JSONObject json = aJson.getJSONObject(i);
            User target = parseJson(json);
            list.add(target);
        }
        return list;
    }

    public void insert(User user, String url, GetUserCallback userCallBack) {
        progressDialog.show();
        new Insert(user, userCallBack).execute(url);
    }

    public void login(User user, String url, GetUserCallback userCallBack) {
        progressDialog.show();
        new Login(user, userCallBack).execute(url);
    }

//    public void getUserByID(int userId, String url, GetUserCallback userCallBack) {
//        new UserByID(userId, userCallBack).execute(url);
//    }

    public void update(User user, String url, GetUserCallback userCallBack) {
        new Update(user, userCallBack).execute(url);
    }

    public void isUsernameExists(String username, String url, GetBooleanCallback booleanCallback) {
        new IsUsernameExists(username, booleanCallback).execute(url);
    }

    public void isCurrentPasswordValid(int userId, String currentPassword, String url, GetBooleanCallback booleanCallback) {
        new IsCurrentPasswordValid(userId, currentPassword, booleanCallback).execute(url);
    }

    public void updatePassword(int userId, String currentPassword, String newPassword, String url, GetBooleanCallback booleanCallback) {
        new UpdatePassword(userId, currentPassword, newPassword, booleanCallback).execute(url);
    }

    public void updatePasswordByEmail(String email, String newPassword, String url, GetUserCallback userCallBack) {
        new UpdatePasswordByEmail(email, newPassword, userCallBack).execute(url);
    }

    /**
     * ******************************************************************************************
     * *****************************************************************************************
     */

    public class Insert extends AsyncTask<String, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public Insert(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", user.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("name", user.getUserName()));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userContact", user.getUserContact()));
            dataToSend.add(new BasicNameValuePair("userEmail", user.getUserEmail()));

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
            progressDialog.dismiss();
            userCallBack.done(null);
        }

    }

    public class Login extends AsyncTask<String, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public Login(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(String... params) {
            if (params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userId", Integer.toString(user.getUserId())));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            User returnedUser = null;

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
                    mMsg = "Incorrect User Details";
                    return null;
                }

                Log.v("happened", "login");
                returnedUser = buildRecord(result);


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

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();

            if (userCallBack != null) {
                if (mMsg.equals("")) {
                    userCallBack.done(returnedUser);
                } else {
                    userCallBack.fail(mMsg);
                }
            }
        }
    }

//    public class UserByID extends AsyncTask<String, Void, User> {
//        int userId;
//        GetUserCallback userCallBack;
//
//        public UserByID(int userId, GetUserCallback userCallBack) {
//            this.userId = userId;
//            this.userCallBack = userCallBack;
//        }
//
//        @Override
//        protected User doInBackground(String... params) {
//            if(params == null)
//                return null;
//
//            // get url from params
//            String url = params[0];
//
//            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
//            dataToSend.add(new BasicNameValuePair("userId", this.userId + ""));
//
//            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
//            HttpClient client = new DefaultHttpClient(httpRequestParams);
//            HttpPost post = new HttpPost(url);
//
//            User returnedUser = null;
//
//            try {
//                post.setEntity(new UrlEncodedFormEntity(dataToSend));
//                HttpResponse httpResponse = client.execute(post);
//
//                HttpEntity entity = httpResponse.getEntity();
//                String result = EntityUtils.toString(entity);
//
//                Log.v("happened", "Get Customer Request By ID");
//                returnedUser = buildRecord(result);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return returnedUser;
//        }
//
//        @Override
//        protected void onPostExecute(User returnedUser) {
//            super.onPostExecute(returnedUser);
//            progressDialog.dismiss();
//            userCallBack.done(returnedUser);
//        }
//    }

    public class Update extends AsyncTask<String, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public Update(User user, GetUserCallback userCallBack) {
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
            dataToSend.add(new BasicNameValuePair("userId", user.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("name", user.getUserName()));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userContact", user.getUserContact()));
            dataToSend.add(new BasicNameValuePair("userEmail", user.getUserEmail()));
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
            progressDialog.dismiss();

            if (userCallBack != null) {
                if (mMsg.equals("")) {
                    userCallBack.done(null);
                } else {
                    userCallBack.fail(mMsg);
                }
            }
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

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "Function IsUsernameExists Call");
                isUsernameExists = (boolean) ServerUtils.buildRecord(Boolean.class, result);

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

    private class IsCurrentPasswordValid extends AsyncTask<String, Void, Boolean> {
        private final int userId;
        private final String currentPassword;
        private final GetBooleanCallback booleanCallback;

        public IsCurrentPasswordValid(int userId, String currentPassword, GetBooleanCallback booleanCallback) {
            this.userId = userId;
            this.currentPassword = currentPassword;
            this.booleanCallback = booleanCallback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean isPasswordExists = false;

            if (params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", userId + ""));
            dataToSend.add(new BasicNameValuePair("currentPassword", currentPassword));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "Function IsCurrentPasswordValid Call");
                isPasswordExists = (boolean) ServerUtils.buildRecord(Boolean.class, result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return isPasswordExists;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            booleanCallback.done(result);
        }
    }

    private class UpdatePassword extends AsyncTask<String, Void, Boolean> {
        private final int userId;
        private final String currentPassword;
        private final String newPassword;
        private final GetBooleanCallback booleanCallback;

        public UpdatePassword(int userId, String currentPassword, String newPassword, GetBooleanCallback booleanCallback) {
            this.userId = userId;
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
            this.booleanCallback = booleanCallback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean isSucessfulUpdate = false;

            if (params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", userId + ""));
            dataToSend.add(new BasicNameValuePair("currentPassword", currentPassword));
            dataToSend.add(new BasicNameValuePair("newPassword", newPassword));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.v("happened", "Function UpdatePassword Call");
                isSucessfulUpdate = (boolean) ServerUtils.buildRecord(Boolean.class, result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return isSucessfulUpdate;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            booleanCallback.done(result);
        }
    }

    private class UpdatePasswordByEmail extends AsyncTask<String, Void, User> {
        private String msg = "";
        private final String email;
        private final String newPassword;
        private final GetUserCallback userCallback;

        public UpdatePasswordByEmail(String email, String newPassword, GetUserCallback userCallback) {
            this.email = email;
            this.newPassword = newPassword;
            this.userCallback = userCallback;
        }


        @Override
        protected User doInBackground(String... params) {
            if (params == null) return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", email));
            dataToSend.add(new BasicNameValuePair("newPassword", newPassword));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            User returnedUser = new User();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                if (entity == null) {
                    msg = "No Response From Server";
                }
                String result = EntityUtils.toString(entity);
                Log.v("happened", "UpdatePasswordByEmail");
                returnedUser = buildRecord(result);

            } catch (JSONException e) {
                msg = "Email Address Not Exist";
            } catch (ConnectTimeoutException cte) {
                msg = "Connection Timeout";
            } catch (IOException e) {
                msg = "No Network Connection";
            } catch (Exception e) {
                msg = e.toString();
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (userCallback != null) {
                if (msg.equals("")) {
                    userCallback.done(user);
                } else {
                    userCallback.fail(msg);
                }
            }
        }
    }
}
