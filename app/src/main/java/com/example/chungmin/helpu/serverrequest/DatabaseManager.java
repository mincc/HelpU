package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.ChatMessage;
import com.example.chungmin.helpu.models.Rating;
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

import static HelpUGenericUtilities.DateTimeUtils.parseTo;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class DatabaseManager {

    private static String mMsg = "";

    public DatabaseManager() {
    }

    private static Integer parseJson(JSONObject json) throws JSONException {
        return json.getInt("result");
    }

    public static Integer buildRecord(String result) throws JSONException {

        Integer target = 0;
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static void execSQL(String sql, Callback.GetIntCallback intCallback) {
        new ExecSQL(sql, intCallback).execute();
    }

    /**
     * ===========================================================================================
     * ===========================================================================================
     */


    private static class ExecSQL extends AsyncTask<String, Void, Integer> {
        String sql;
        Callback.GetIntCallback intCallBack;

        public ExecSQL(String sql, Callback.GetIntCallback intCallBack) {
            this.sql = sql;
            this.intCallBack = intCallBack;
        }

        @Override
        protected Integer doInBackground(String... params) {
            mMsg = "";
            Integer result = 0;
            if (params == null)
                return result;

            // get url from params
            String url = AppLink.getDatabaseSelectUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("sql", sql));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String returnResult = EntityUtils.toString(entity);

                Log.v("happened", "ExecSQL");
                result = buildRecord(returnResult);
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (intCallBack != null) {
                if (mMsg.equals("")) {
                    intCallBack.complete(result);
                } else {
                    intCallBack.failure(mMsg);
                }
            }
        }
    }
}
