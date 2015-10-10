package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.Transaction;
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
 * Created by Chung Min on 8/14/2015.
 */
public class TransactionManager {

    private static String mMsg = "";

    public TransactionManager() {
    }


    private static Transaction parseJson(JSONObject json) throws JSONException {
        Transaction target = new Transaction();
        target.setId(json.getInt("id"));
        target.setTransactionId(json.getString("transactionId"));
        target.setRefNo(json.getString("refNo"));
        target.setAmount(json.getString("amount"));
        target.setRemark(json.getString("remark"));
        target.setAuthCode(json.getString("authCode"));
        target.setErrDesc(json.getString("errDesc"));
        return target;
    }

    public static Transaction buildRecord(String result) throws JSONException {

        Transaction target = new Transaction();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static void insert(Transaction transaction, Callback.GetTransactionCallback transactionCallback) {
        new Insert(transaction, transactionCallback).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public static class Insert extends AsyncTask<String, Void, Transaction> {
        Transaction transaction;
        Callback.GetTransactionCallback transactionCallBack;

        public Insert(Transaction transaction, Callback.GetTransactionCallback transactionCallBack) {
            this.transaction = transaction;
            this.transactionCallBack = transactionCallBack;
        }

        @Override
        protected Transaction doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;
            String url = AppLink.getTransactionInsertUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("transactionId", transaction.getTransactionId()));
            dataToSend.add(new BasicNameValuePair("refNo", transaction.getRefNo()));
            dataToSend.add(new BasicNameValuePair("amount", transaction.getAmount()));
            dataToSend.add(new BasicNameValuePair("remark", transaction.getRemark()));
            dataToSend.add(new BasicNameValuePair("authCode", transaction.getAuthCode()));
            dataToSend.add(new BasicNameValuePair("errDesc", transaction.getErrDesc()));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            Transaction returnedTransaction = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                Log.v("happened", "Transaction Insert");
                returnedTransaction = buildRecord(result);
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

            return returnedTransaction;
        }

        @Override
        protected void onPostExecute(Transaction returnedTransaction) {
            super.onPostExecute(returnedTransaction);
            if (transactionCallBack != null) {
                if (mMsg.equals("")) {
                    transactionCallBack.complete(returnedTransaction);
                } else {
                    transactionCallBack.failure(mMsg);
                }
            }
        }

    }

}


