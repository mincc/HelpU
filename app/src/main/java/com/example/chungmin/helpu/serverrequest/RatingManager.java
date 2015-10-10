package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.Rating;
import com.example.chungmin.helpu.serverrequest.common.ServerUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class RatingManager {

    private static String mMsg = "";

    public RatingManager() {
    }

    public static void insert(Rating rating, Callback.GetRatingCallback ratingCallback) {
        new Insert(rating, ratingCallback).execute();
    }

    /**
     * ===========================================================================================
     * ===========================================================================================
     */

    public static class Insert extends AsyncTask<String, Void, Void> {
        Rating rating;
        Callback.GetRatingCallback ratingCallBack;

        public Insert(Rating rating, Callback.GetRatingCallback ratingCallBack) {
            this.rating = rating;
            this.ratingCallBack = ratingCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if(params == null)
                return null;

            // get url from params
            String url = AppLink.getRatingInsertUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("voterId", rating.getVoterId() + ""));
            dataToSend.add(new BasicNameValuePair("targetUserId", rating.getTargetUserId() + ""));
            dataToSend.add(new BasicNameValuePair("ratingValue", rating.getRatingValue() + ""));
            dataToSend.add(new BasicNameValuePair("ratingType", rating.getRatingType()));
            dataToSend.add(new BasicNameValuePair("customerRequestId", rating.getCustomerRequestId() + ""));

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
            if (ratingCallBack != null) {
                if (mMsg.equals("")) {
                    ratingCallBack.complete(null);
                } else {
                    ratingCallBack.failure(mMsg);
                }
            }
        }
    }
}
