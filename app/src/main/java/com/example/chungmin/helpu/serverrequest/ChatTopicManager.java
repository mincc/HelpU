package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.ChatTopic;
import com.example.chungmin.helpu.models.ChatTopic;
import com.example.chungmin.helpu.models.CustomerRequest;
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

/**
 * Created by Chung Min on 7/19/2015.
 */
public class ChatTopicManager {

    private static String mMsg = "";

    public ChatTopicManager() {
    }

    private static ChatTopic parseJson(JSONObject json) throws JSONException {
        ChatTopic target = new ChatTopic();
        target.setUserIdCust(json.getInt("userIdCust"));
        target.setUserNameCust(json.getString("userNameCust"));
        target.setUserIdSPdr(json.getInt("userIdSPdr"));
        target.setUserNameSPdr(json.getString("userNameSPdr"));
        target.setCustomerRequestId(json.getInt("customerRequestId"));
        target.setDescription(json.getString("description"));
        return target;
    }

    public static ChatTopic buildRecord(String result) throws JSONException {

        ChatTopic target = new ChatTopic();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static List<ChatTopic> buildList(String result) throws JSONException {

        List<ChatTopic> list = new ArrayList<ChatTopic>();
        JSONArray aJson = new JSONArray(result);
        for (int i = 0; i < aJson.length(); i++) {
            JSONObject json = aJson.getJSONObject(i);
            ChatTopic target = parseJson(json);
            list.add(target);
        }
        return list;
    }

    public static void getListByUserId(int userId, Callback.GetChatTopicListCallback chatTopicCallback) {
        new GetListByUserId(userId, chatTopicCallback).execute();
    }

    /**
     * ===========================================================================================
     * ===========================================================================================
     */

    public static class GetListByUserId extends AsyncTask<String, Void, List<ChatTopic>> {
        int userId;
        Callback.GetChatTopicListCallback chatTopicCallBack;

        public GetListByUserId(int userId, Callback.GetChatTopicListCallback chatTopicCallBack) {
            this.userId = userId;
            this.chatTopicCallBack = chatTopicCallBack;
        }

        @Override
        protected List<ChatTopic> doInBackground(String... params) {
            mMsg = "";
            List<ChatTopic> chatTopicList = new ArrayList<ChatTopic>();

            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getChatTopicGetListByUserIdUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userId", userId + ""));

            try {
                HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(url);

                // connect
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = client.execute(post);

                // get response
                HttpEntity entity = response.getEntity();

                if (entity == null) {
                    mMsg = "No Response From Server";
                    return null;
                }

                String result = EntityUtils.toString(entity);
                chatTopicList = buildList(result);
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return chatTopicList;
        }

        @Override
        protected void onPostExecute(List<ChatTopic> result) {
            super.onPostExecute(result);
            if (chatTopicCallBack != null) {
                if (mMsg.equals("")) {
                    chatTopicCallBack.complete(result);
                } else {
                    chatTopicCallBack.failure(mMsg);
                }
            }
        }
    }
}
