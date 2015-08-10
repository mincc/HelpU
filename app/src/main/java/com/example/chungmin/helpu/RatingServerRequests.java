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
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public RatingServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void setRatingInsert(Rating rating, String url, GetRatingCallback ratingCallback) {
        progressDialog.show();
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
            progressDialog.dismiss();
            ratingCallBack.done(null);
        }

//    }
//
//    public class RatingByID extends AsyncTask<String, Void, Rating> {
//        int ratingId;
//        GetRatingCallback ratingCallBack;
//
//        public RatingByID(int ratingId, GetRatingCallback ratingCallBack) {
//            this.ratingId = ratingId;
//            this.ratingCallBack = ratingCallBack;
//        }
//
//        @Override
//        protected Rating doInBackground(String... params) {
//            if(params == null)
//                return null;
//
//            // get url from params
//            String url = params[0];
//
//            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
//            dataToSend.add(new BasicNameValuePair("ratingId", this.ratingId + ""));
//
//            HttpParams httpRequestParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
//                    CONNECTION_TIMEOUT);
//            HttpConnectionParams.setSoTimeout(httpRequestParams,
//                    CONNECTION_TIMEOUT);
//
//            HttpClient client = new DefaultHttpClient(httpRequestParams);
//            HttpPost post = new HttpPost(url);
//
//            Rating returnedRating = null;
//
//            try {
//                post.setEntity(new UrlEncodedFormEntity(dataToSend));
//                HttpResponse httpResponse = client.execute(post);
//
//                HttpEntity entity = httpResponse.getEntity();
//                String result = EntityUtils.toString(entity);
//                JSONObject jObject = new JSONObject(result);
//
//                if (jObject.length() != 0){
//                    Log.v("happened", "Get Customer Request By ID");
//
//                    int ratingId = jObject.getInt("ratingId");
//                    int serviceId = jObject.getInt("serviceId");
//                    int userId = jObject.getInt("userId");
//                    String description = jObject.getString("description");
//                    int projectStatusId = jObject.getInt("projectStatusId");
//                    String userName = jObject.getString("userName");
//                    String serviceName = jObject.getString("serviceName");
//                    int serviceProviderId = jObject.getInt("serviceProviderId");
//                    double quotation = jObject.getDouble("quotation");
//
//                    returnedRating = new Rating(
//                            ratingId,
//                            serviceId,
//                            serviceName,
//                            userId,
//                            description,
//                            ProjectStatus.values()[projectStatusId],
//                            userName,
//                            serviceName,
//                            serviceProviderId,
//                            quotation);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return returnedRating;
//        }
//
//        @Override
//        protected void onPostExecute(Rating returnedRating) {
//            super.onPostExecute(returnedRating);
//            progressDialog.dismiss();
//            ratingCallBack.done(returnedRating);
//        }
//    }
//
//    public class RatingUpdate extends AsyncTask<String, Void, Void> {
//        Rating rating;
//        GetRatingCallback ratingCallBack;
//
//        public RatingUpdate(Rating rating, GetRatingCallback ratingCallBack) {
//            this.rating=rating;
//            this.ratingCallBack = ratingCallBack;
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            if(params == null)
//                return null;
//
//            // get url from params
//            String url = params[0];
//
//            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
//            dataToSend.add(new BasicNameValuePair("ratingId", this.rating.getRatingId() + ""));
//            dataToSend.add(new BasicNameValuePair("serviceId", this.rating.getServiceId() + ""));
//            dataToSend.add(new BasicNameValuePair("userId", this.rating.getUserId() + ""));
//            dataToSend.add(new BasicNameValuePair("description", this.rating.getDescription()));
//            dataToSend.add(new BasicNameValuePair("projectStatusId", this.rating.getProjectStatusId() + ""));
//            dataToSend.add(new BasicNameValuePair("serviceProviderId", this.rating.getServiceProviderId() + ""));
//            dataToSend.add(new BasicNameValuePair("quotation", this.rating.getQuotation() + ""));
//
//            HttpParams httpRequestParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
//                    CONNECTION_TIMEOUT);
//            HttpConnectionParams.setSoTimeout(httpRequestParams,
//                    CONNECTION_TIMEOUT);
//
//            HttpClient client = new DefaultHttpClient(httpRequestParams);
//            HttpPost post = new HttpPost(url);
//
//            Rating returnedRating = null;
//
//            try {
//                post.setEntity(new UrlEncodedFormEntity(dataToSend));
//                HttpResponse httpResponse = client.execute(post);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            progressDialog.dismiss();
//            ratingCallBack.done(null);
//        }
    }
}
