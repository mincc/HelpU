package com.example.chungmin.helpu.serverrequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.User;
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
        target.setIsAdmin(json.getInt("isAdmin"));
        target.setGcmRegId(json.getString("gcmRegId"));
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

    public void insert(User user, Callback.GetUserCallback userCallBack) {
        progressDialog.show();
        new Insert(user, userCallBack).execute();
    }

    public void login(User user, Callback.GetUserCallback userCallBack) {
        progressDialog.show();
        new Login(user, userCallBack).execute();
    }

//    public void getUserByID(int userId, String url, GetUserCallback userCallBack) {
//        new UserByID(userId, userCallBack).execute(url);
//    }

    public void update(User user, Callback.GetUserCallback userCallBack) {
        new Update(user, userCallBack).execute();
    }

    public void isUsernameExists(String username, Callback.GetBooleanCallback booleanCallback) {
        new IsUsernameExists(username, booleanCallback).execute();
    }

    public void isCurrentPasswordValid(int userId, String currentPassword, Callback.GetBooleanCallback booleanCallback) {
        new IsCurrentPasswordValid(userId, currentPassword, booleanCallback).execute();
    }

    public void updatePassword(int userId, String currentPassword, String newPassword, Callback.GetBooleanCallback booleanCallback) {
        new UpdatePassword(userId, currentPassword, newPassword, booleanCallback).execute();
    }

    public void updatePasswordByEmail(String email, String newPassword, Callback.GetUserCallback userCallBack) {
        new UpdatePasswordByEmail(email, newPassword, userCallBack).execute();
    }

    public void logout(int userId, Callback.GetUserCallback userCallback) {
        new Logout(userId, userCallback).execute();
    }

    /**
     * ******************************************************************************************
     * *****************************************************************************************
     */

    public class Insert extends AsyncTask<String, Void, Void> {
        User user;
        Callback.GetUserCallback userCallBack;

        public Insert(User user, Callback.GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null) return null;
            String url = AppLink.getUserInsertUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", user.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("name", user.getUserName()));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userContact", user.getUserContact()));
            dataToSend.add(new BasicNameValuePair("userEmail", user.getUserEmail()));
            dataToSend.add(new BasicNameValuePair("gcmRegId", user.getGcmRegId()));

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
            progressDialog.dismiss();
            if (userCallBack != null) {
                if (mMsg.equals("")) {
                    userCallBack.complete(null);
                } else {
                    userCallBack.failure(mMsg);
                }
            }
        }

    }

    public class Login extends AsyncTask<String, Void, User> {
        User user;
        Callback.GetUserCallback userCallBack;

        public Login(User user, Callback.GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(String... params) {
            mMsg = "";
            if (params == null) return null;

            // get url from params
            String url = AppLink.getUserGetByUsernameAndPasswordUrl();

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
                    userCallBack.complete(returnedUser);
                } else {
                    userCallBack.failure(mMsg);
                }
            }
        }
    }

//    public class UserByID extends AsyncTask<String, Void, User> {
//        int userId;
//        Callback.GetUserCallback userCallBack;
//
//        public UserByID(int userId, Callback.GetUserCallback userCallBack) {
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
//            if (userCallBack != null) {
//                if (mMsg.equals("")) {
//                    userCallBack.complete(returnedUser);
//                } else {
//                    userCallBack.failure(mMsg);
//                }
//            }
//        }
//    }

    public class Update extends AsyncTask<String, Void, Void> {
        User user;
        Callback.GetUserCallback userCallBack;

        public Update(User user, Callback.GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;
            String url = AppLink.getUserUpdateUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", user.getUserId() + ""));
            dataToSend.add(new BasicNameValuePair("name", user.getUserName()));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("userContact", user.getUserContact()));
            dataToSend.add(new BasicNameValuePair("userEmail", user.getUserEmail()));
            dataToSend.add(new BasicNameValuePair("gcmRegId", user.getGcmRegId()));
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
                    userCallBack.complete(null);
                } else {
                    userCallBack.failure(mMsg);
                }
            }
        }
    }

    public class IsUsernameExists extends AsyncTask<String, Void, Boolean> {
        String username;
        Callback.GetBooleanCallback booleanCallback;

        public IsUsernameExists(String username, Callback.GetBooleanCallback booleanCallback) {
            this.username = username;
            this.booleanCallback = booleanCallback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mMsg = "";
            boolean isUsernameExists = false;
            if (params == null)
                return false;
            String url = AppLink.getIsUsernameAlreadyExistsUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", this.username));

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
                Log.v("happened", "Function IsUsernameExists Call");
                isUsernameExists = (boolean) ServerUtils.buildRecord(Boolean.class, result);

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

            return isUsernameExists;
        }

        @Override
        protected void onPostExecute(Boolean isUsernameExists) {
            super.onPostExecute(isUsernameExists);
            if (booleanCallback != null) {
                if (mMsg.equals("")) {
                    booleanCallback.complete(isUsernameExists);
                } else {
                    booleanCallback.failure(mMsg);
                }
            }
        }
    }

    private class IsCurrentPasswordValid extends AsyncTask<String, Void, Boolean> {
        private final int userId;
        private final String currentPassword;
        private final Callback.GetBooleanCallback booleanCallback;

        public IsCurrentPasswordValid(int userId, String currentPassword, Callback.GetBooleanCallback booleanCallback) {
            this.userId = userId;
            this.currentPassword = currentPassword;
            this.booleanCallback = booleanCallback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mMsg = "";
            boolean isPasswordExists = false;

            if (params == null) return null;
            String url = AppLink.getUserIsCurrentPasswordValidUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "Function IsCurrentPasswordValid Call");
                isPasswordExists = (boolean) ServerUtils.buildRecord(Boolean.class, result);
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

            return isPasswordExists;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (booleanCallback != null) {
                if (mMsg.equals("")) {
                    booleanCallback.complete(result);
                } else {
                    booleanCallback.failure(mMsg);
                }
            }
        }
    }

    private class UpdatePassword extends AsyncTask<String, Void, Boolean> {
        private final int userId;
        private final String currentPassword;
        private final String newPassword;
        private final Callback.GetBooleanCallback booleanCallback;

        public UpdatePassword(int userId, String currentPassword, String newPassword, Callback.GetBooleanCallback booleanCallback) {
            this.userId = userId;
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
            this.booleanCallback = booleanCallback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mMsg = "";
            boolean isSucessfulUpdate = false;

            if (params == null) return null;
            String url = AppLink.getUserPasswordUpdateUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);

                Log.v("happened", "Function UpdatePassword Call");
                isSucessfulUpdate = (boolean) ServerUtils.buildRecord(Boolean.class, result);

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

            return isSucessfulUpdate;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (booleanCallback != null) {
                if (mMsg.equals("")) {
                    booleanCallback.complete(result);
                } else {
                    booleanCallback.failure(mMsg);
                }
            }
        }
    }

    private class UpdatePasswordByEmail extends AsyncTask<String, Void, User> {
        private final String email;
        private final String newPassword;
        private final Callback.GetUserCallback userCallback;

        public UpdatePasswordByEmail(String email, String newPassword, Callback.GetUserCallback userCallback) {
            this.email = email;
            this.newPassword = newPassword;
            this.userCallback = userCallback;
        }


        @Override
        protected User doInBackground(String... params) {
            mMsg = "";
            if (params == null) return null;

            // get url from params
            String url = AppLink.getUserUpdatePasswordByEmailUrl();

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
                    mMsg = "No Response From Server";
                    return null;
                }
                String result = EntityUtils.toString(entity);
                Log.v("happened", "UpdatePasswordByEmail");
                returnedUser = buildRecord(result);

            } catch (JSONException e) {
                mMsg = "Email Address Not Exist";
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
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (userCallback != null) {
                if (mMsg.equals("")) {
                    userCallback.complete(user);
                } else {
                    userCallback.failure(mMsg);
                }
            }
        }
    }

    private class Logout extends AsyncTask<Void, Void, User> {
        private final int userId;
        private final Callback.GetUserCallback userCallback;

        public Logout(int userId, Callback.GetUserCallback userCallback) {
            this.userId = userId;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            mMsg = "";
            if (params == null) return null;

            // get url from params
            String url = AppLink.getUserLogoutUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", userId + ""));

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
                Log.v("happened", "Logout");

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
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (userCallback != null) {
                if (mMsg.equals("")) {
                    userCallback.complete(user);
                } else {
                    userCallback.failure(mMsg);
                }
            }
        }
    }
}
