package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.AppStats;
import com.example.chungmin.helpu.serverrequest.common.ServerUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class AppStatsManager {

    private static String mMsg = "";

    public AppStatsManager() {
    }

    private static AppStats parseJson(JSONObject json) throws JSONException {
        AppStats target = new AppStats();
        target.setTotalCustomerRequestInOneDay(json.getInt("totalCustomerRequestInOneDay"));
        target.setTotalCustomerRequestInOneWeek(json.getInt("totalCustomerRequestInOneWeek"));
        target.setTotalCustomerRequestInOneMonth(json.getInt("totalCustomerRequestInOneMonth"));
        target.setTotalServiceProviderInOneDay(json.getInt("totalServiceProviderInOneDay"));
        target.setTotalServiceProviderInOneWeek(json.getInt("totalServiceProviderInOneWeek"));
        target.setTotalServiceProviderInOneMonth(json.getInt("totalServiceProviderInOneMonth"));
        target.setTotalCustomerRequestDoneInOneDay(json.getInt("totalCustomerRequestDoneInOneDay"));
        target.setTotalCustomerRequestDoneInOneWeek(json.getInt("totalCustomerRequestDoneInOneWeek"));
        target.setTotalCustomerRequestDoneInOneMonth(json.getInt("totalCustomerRequestDoneInOneMonth"));
        target.setTotalQuotationInOneDay(json.getDouble("totalQuotationInOneDay"));
        target.setTotalQuotationInOneWeek(json.getDouble("totalQuotationInOneWeek"));
        target.setTotalQuotationInOneMonth(json.getDouble("totalQuotationInOneMonth"));
        target.setTotalUserInOneDay(json.getInt("totalUserInOneDay"));
        target.setTotalUserInOneWeek(json.getInt("totalUserInOneWeek"));
        target.setTotalUserInOneMonth(json.getInt("totalUserInOneMonth"));
        return target;
    }

    public static AppStats buildRecord(String result) throws JSONException {

        AppStats target = new AppStats();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static void getAppStats(Callback.GetAppStatsCallback appStatsCallBack) {
        new AppStatistic(appStatsCallBack).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public static class AppStatistic extends AsyncTask<String, Void, AppStats> {

        Callback.GetAppStatsCallback appStatsCallBack;

        public AppStatistic(Callback.GetAppStatsCallback appStatsCallBack) {
            this.appStatsCallBack = appStatsCallBack;
        }

        @Override
        protected AppStats doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.geAppStatsInfoUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            AppStats returnedAppStats = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "AppStats");
                returnedAppStats = buildRecord(result);

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

            return returnedAppStats;
        }

        @Override
        protected void onPostExecute(AppStats returnedAppStats) {
            super.onPostExecute(returnedAppStats);

            if (appStatsCallBack != null) {
                if (mMsg.equals("")) {
                    appStatsCallBack.complete(returnedAppStats);
                } else {
                    appStatsCallBack.failure(mMsg);
                }
            }
        }
    }


}
