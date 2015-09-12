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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import HelpUGenericUtilities.StringUtils;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class UserStatsServerRequests {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public UserStatsServerRequests() {
    }

    public static UserStats BuildRecord(String result) {

        UserStats target = new UserStats();
        try {
            JSONObject jObject = new JSONObject(result);
            if (jObject.length() != 0) {
                target.setTotalCustomerRequest(jObject.getInt("totalCustomerRequest"));
                target.setTotalServiceProvider(jObject.getInt("totalServiceProvider"));
                target.setTotalJobOffer(jObject.getInt("totalJobOffer"));
                target.setTotalJobDone(jObject.getInt("totalJobDone"));
                target.setTotalWorkNotification(jObject.getInt("totalWorkNotification"));
                target.setTotalHireNotification(jObject.getInt("totalHireNotification"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return target;
    }

    public void getUserStatsByUserId(int userId, String url, GetUserStatsCallback userStatsCallBack) {
        new UserStatsByUserId(userId, userStatsCallBack).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class UserStatsByUserId extends AsyncTask<String, Void, UserStats> {
        int userId;
        GetUserStatsCallback userStatsCallBack;

        public UserStatsByUserId(int userId, GetUserStatsCallback userStatsCallBack) {
            this.userId = userId;
            this.userStatsCallBack = userStatsCallBack;
        }

        @Override
        protected UserStats doInBackground(String... params) {
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

            UserStats returnedUserStats = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "UserStatsByUserId");
                returnedUserStats = BuildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUserStats;
        }

        @Override
        protected void onPostExecute(UserStats returnedUserStats) {
            super.onPostExecute(returnedUserStats);
            userStatsCallBack.done(returnedUserStats);
        }
    }


}
