package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.GetTransactionCallback;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.Transaction;
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
 * Created by Chung Min on 8/14/2015.
 */
public class TransactionManager {

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

    public void insert(Transaction transaction, String url, GetTransactionCallback transactionCallback) {
//        progressDialog.show();
        new Insert(transaction, transactionCallback).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class Insert extends AsyncTask<String, Void, Transaction> {
        Transaction transaction;
        GetTransactionCallback transactionCallBack;

        public Insert(Transaction transaction, GetTransactionCallback transactionCallBack) {
            this.transaction = transaction;
            this.transactionCallBack = transactionCallBack;
        }

        @Override
        protected Transaction doInBackground(String... params) {
            if (params == null)
                return null;

            // get url from params
            String url = params[0];

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
                String result = EntityUtils.toString(entity);
                Log.v("happened", "Transaction Insert");
                returnedTransaction = buildRecord(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedTransaction;
        }

        @Override
        protected void onPostExecute(Transaction returnedTransaction) {
            super.onPostExecute(returnedTransaction);
            transactionCallBack.done(returnedTransaction);
        }

    }

}


