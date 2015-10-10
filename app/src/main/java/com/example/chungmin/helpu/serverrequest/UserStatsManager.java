package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.UserStats;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class UserStatsManager {

    private static String mMsg = "";

    public UserStatsManager() {
    }

    public static UserStats BuildRecord(String result) throws JSONException {

        UserStats target = new UserStats();
        JSONObject jObject = new JSONObject(result);
        if (jObject.length() != 0) {
            target.setTotalCustomerRequest(jObject.getInt("totalCustomerRequest"));
            target.setTotalServiceProvider(jObject.getInt("totalServiceProvider"));
            target.setTotalJobOffer(jObject.getInt("totalJobOffer"));
            target.setTotalJobDone(jObject.getInt("totalJobDone"));
            target.setTotalWorkNotification(jObject.getInt("totalWorkNotification"));
            target.setTotalHireNotification(jObject.getInt("totalHireNotification"));
        }
        return target;
    }

    public static void getByUserId(int userId, Callback.GetUserStatsCallback userStatsCallBack) {
        new GetByUserId(userId, userStatsCallBack).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public static class GetByUserId extends AsyncTask<String, Void, UserStats> {
        int userId;
        Callback.GetUserStatsCallback userStatsCallBack;

        public GetByUserId(int userId, Callback.GetUserStatsCallback userStatsCallBack) {
            this.userId = userId;
            this.userStatsCallBack = userStatsCallBack;
        }

        @Override
        protected UserStats doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getUserStatsInfoUrl();

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
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "GetByUserId");
                returnedUserStats = BuildRecord(result);

            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return returnedUserStats;
        }

        @Override
        protected void onPostExecute(UserStats returnedUserStats) {
            super.onPostExecute(returnedUserStats);
            if (userStatsCallBack != null) {
                if (mMsg.equals("")) {
                    userStatsCallBack.complete(returnedUserStats);
                } else {
                    userStatsCallBack.failure(mMsg);
                }
            }
        }
    }


}
