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
 * Created by Chung Min on 8/14/2015.
 */
public class TransactionServerRequests {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public TransactionServerRequests() {
    }

    public void transactionInsert(Transaction transaction, String url, GetTransactionCallback transactionCallback) {
//        progressDialog.show();
        new TransactionInsert(transaction, transactionCallback).execute(url);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation
     */

    public class TransactionInsert extends AsyncTask<String, Void, Transaction> {
        Transaction transaction;
        GetTransactionCallback transactionCallBack;

        public TransactionInsert(Transaction transaction, GetTransactionCallback transactionCallBack) {
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

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            Transaction returnedTransaction = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0) {
                    Log.v("happened", "Transaction Insert");

                    int id = jObject.getInt("id");
                    String transactionId = jObject.getString("transactionId");
                    String refNo = jObject.getString("refNo");
                    String amount = jObject.getString("amount");
                    String remark = jObject.getString("remark");
                    String authCode = jObject.getString("authCode");
                    String errDesc = jObject.getString("errDesc");

                    returnedTransaction = new Transaction(
                            id,
                            transactionId,
                            refNo,
                            amount,
                            remark,
                            authCode,
                            errDesc);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedTransaction;
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
        protected void onPostExecute(Transaction returnedTransaction) {
            super.onPostExecute(returnedTransaction);
            transactionCallBack.done(returnedTransaction);
        }

    }

}


