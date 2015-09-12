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
public class RatingServerRequests {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public RatingServerRequests() {
    }

    public void setRatingInsert(Rating rating, String url, GetRatingCallback ratingCallback) {
        new RatingInsert(rating, ratingCallback).execute(url);
    }
//
//    public void getRatingByID(int ratingId, String url, GetRatingCallback ratingCallBack) {
//        progressDialog.show();
//        new RatingByID(ratingId, ratingCallBack).execute(url);
//    }
//
//    public void getRatingUpdate(Rating rating, String url, GetRatingCallback ratingCallBack) {
//        //progressDialog.show();
//        new RatingUpdate(rating, ratingCallBack).execute(url);
//    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation activity_notification_job_offer of the background computation
     */

    public class RatingInsert extends AsyncTask<String, Void, Void> {
        Rating rating;
        GetRatingCallback ratingCallBack;

        public RatingInsert(Rating rating, GetRatingCallback ratingCallBack) {
            this.rating = rating;
            this.ratingCallBack = ratingCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("voterId", rating.getVoterId() + ""));
            dataToSend.add(new BasicNameValuePair("targetUserId", rating.getTargetUserId() + ""));
            dataToSend.add(new BasicNameValuePair("ratingValue", rating.getRatingValue() + ""));
            dataToSend.add(new BasicNameValuePair("ratingType", rating.getRatingType()));
            dataToSend.add(new BasicNameValuePair("customerRequestId", rating.getCustomerRequestId() + ""));

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
            ratingCallBack.done(null);
        }
    }
}
