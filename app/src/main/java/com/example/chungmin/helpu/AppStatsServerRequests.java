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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class AppStatsServerRequests {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public AppStatsServerRequests() {
    }

    public void getAppStats(String url, GetAppStatsCallback appStatsCallBack) {
        new AppStatistic(appStatsCallBack).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class AppStatistic extends AsyncTask<String, Void, AppStats> {

        GetAppStatsCallback appStatsCallBack;

        public AppStatistic(GetAppStatsCallback appStatsCallBack) {
            this.appStatsCallBack = appStatsCallBack;
        }

        @Override
        protected AppStats doInBackground(String... params) {
            if (params == null)
                return null;

            // get url from params
            String url = params[0];

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            AppStats returnedAppStats = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0) {
                    Log.v("happened", "AppStats");

                    int totalCustomerRequestInOneDay = jObject.getInt("totalCustomerRequestInOneDay");
                    int totalCustomerRequestInOneWeek = jObject.getInt("totalCustomerRequestInOneWeek");
                    int totalCustomerRequestInOneMonth = jObject.getInt("totalCustomerRequestInOneMonth");
                    int totalServiceProviderInOneDay = jObject.getInt("totalServiceProviderInOneDay");
                    int totalServiceProviderInOneWeek = jObject.getInt("totalServiceProviderInOneWeek");
                    int totalServiceProviderInOneMonth = jObject.getInt("totalServiceProviderInOneMonth");
                    int totalCustomerRequestDoneInOneDay = jObject.getInt("totalCustomerRequestDoneInOneDay");
                    int totalCustomerRequestDoneInOneWeek = jObject.getInt("totalCustomerRequestDoneInOneWeek");
                    int totalCustomerRequestDoneInOneMonth = jObject.getInt("totalCustomerRequestDoneInOneMonth");
                    double totalQuotationInOneDay = jObject.getDouble("totalQuotationInOneDay");
                    double totalQuotationInOneWeek = jObject.getDouble("totalQuotationInOneWeek");
                    double totalQuotationInOneMonth = jObject.getDouble("totalQuotationInOneMonth");
                    int totalUserInOneDay = jObject.getInt("totalUserInOneDay");
                    int totalUserInOneWeek = jObject.getInt("totalUserInOneWeek");
                    int totalUserInOneMonth = jObject.getInt("totalUserInOneMonth");

                    returnedAppStats = new AppStats(
                            totalCustomerRequestInOneDay,
                            totalCustomerRequestInOneWeek,
                            totalCustomerRequestInOneMonth,
                            totalServiceProviderInOneDay,
                            totalServiceProviderInOneWeek,
                            totalServiceProviderInOneMonth,
                            totalCustomerRequestDoneInOneDay,
                            totalCustomerRequestDoneInOneWeek,
                            totalCustomerRequestDoneInOneMonth,
                            totalQuotationInOneDay,
                            totalQuotationInOneWeek,
                            totalQuotationInOneMonth,
                            totalUserInOneDay,
                            totalUserInOneWeek,
                            totalUserInOneMonth);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedAppStats;
        }

        @Override
        protected void onPostExecute(AppStats returnedAppStats) {
            super.onPostExecute(returnedAppStats);
            appStatsCallBack.done(returnedAppStats);
        }
    }


}
