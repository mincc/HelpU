package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.GetUserStatsCallback;
import com.example.chungmin.helpu.models.UserStats;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class UserStatsManager {

    public UserStatsManager() {
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

    public void getByUserId(int userId, String url, GetUserStatsCallback userStatsCallBack) {
        new GetByUserId(userId, userStatsCallBack).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class GetByUserId extends AsyncTask<String, Void, UserStats> {
        int userId;
        GetUserStatsCallback userStatsCallBack;

        public GetByUserId(int userId, GetUserStatsCallback userStatsCallBack) {
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

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            UserStats returnedUserStats = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("happened", "GetByUserId");
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
