package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;

import com.example.chungmin.helpu.callback.GetRatingCallback;
import com.example.chungmin.helpu.models.Rating;
import com.example.chungmin.helpu.serverrequest.common.ServerUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class RatingManager {

    public RatingManager() {
    }

    public void insert(Rating rating, String url, GetRatingCallback ratingCallback) {
        new Insert(rating, ratingCallback).execute(url);
    }

    /**
     * ===========================================================================================
     * ===========================================================================================
     */

    public class Insert extends AsyncTask<String, Void, Void> {
        Rating rating;
        GetRatingCallback ratingCallBack;

        public Insert(Rating rating, GetRatingCallback ratingCallBack) {
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
            ratingCallBack.done(null);
        }
    }
}
