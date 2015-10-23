package com.example.chungmin.helpu.serverrequest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.ChatMessage;
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
 * Created by Chung Min on 10/19/2015.
 */
public class ChatMessageManager {

    private static String mMsg = "";

    public ChatMessageManager() {
    }

    private static ChatMessage parseJson(JSONObject json) throws JSONException {
        ChatMessage target = new ChatMessage();
        target.setChatMessageId(json.getInt("chatMessageId"));
        target.setId(json.getInt("id"));
        target.setMessage(json.getString("message"));
        target.setUserIdFrom(json.getInt("userIdFrom"));
        target.setUserIdTo(json.getInt("userIdTo"));
        target.setCreatedDate(parseTo(json.getString("createdDate")));
        target.setIsDelete(json.getInt("isDelete"));
        return target;
    }

    public static ChatMessage buildRecord(String result) throws JSONException {

        ChatMessage target = new ChatMessage();
        JSONObject json = new JSONObject(result);
        if (json.length() != 0) {
            target = parseJson(json);
        }
        return target;
    }

    public static List<ChatMessage> buildList(String result) throws JSONException {

        List<ChatMessage> list = new ArrayList<ChatMessage>();
        JSONArray aJson = new JSONArray(result);
        for (int i = 0; i < aJson.length(); i++) {
            JSONObject json = aJson.getJSONObject(i);
            ChatMessage target = parseJson(json);
            list.add(target);
        }
        return list;
    }

    public static void insert(ChatMessage chatMessage, Callback.GetChatMessageCallback callback) {
        new Insert(chatMessage, callback).execute();
    }

    public static void getByID(int chatMessageId, Callback.GetChatMessageCallback chatMessageCallBack) {
        new GetByID(chatMessageId, chatMessageCallBack).execute();
    }

    public static void update(ChatMessage chatMessage, Callback.GetChatMessageCallback chatMessageCallBack) {
        new Update(chatMessage, chatMessageCallBack).execute();
    }

    public static void delete(int chatMessageId, int isLogicalDelete, Callback.GetChatMessageCallback chatMessageCallback) {
        new Delete(chatMessageId, isLogicalDelete, chatMessageCallback).execute();
    }

    /**
     * ===========================================================================================
     * ===========================================================================================
     */

    public static class Insert extends AsyncTask<String, Void, ChatMessage> {
        ChatMessage chatMessage;
        Callback.GetChatMessageCallback callback;

        public Insert(ChatMessage chatMessage, Callback.GetChatMessageCallback callback) {
            this.chatMessage = chatMessage;
            this.callback = callback;
        }

        @Override
        protected ChatMessage doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getChatMessageInsertUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", chatMessage.getId() + ""));
            dataToSend.add(new BasicNameValuePair("message", chatMessage.getMessage()));
            dataToSend.add(new BasicNameValuePair("userIdFrom", chatMessage.getUserIdFrom() + ""));
            dataToSend.add(new BasicNameValuePair("userIdTo", chatMessage.getUserIdTo() + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            ChatMessage returnedChatMessage = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.v("happened", "Get Chat Message");
                returnedChatMessage = buildRecord(result);
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return returnedChatMessage;
        }

        @Override
        protected void onPostExecute(ChatMessage result) {
            super.onPostExecute(result);
            if (callback != null) {
                if (mMsg.equals("")) {
                    callback.complete(result);
                } else {
                    callback.failure(mMsg);
                }
            }
        }
    }

    public static class GetByID extends AsyncTask<String, Void, ChatMessage> {
        int chatMessageId;
        Callback.GetChatMessageCallback chatMessageCallBack;

        public GetByID(int chatMessageId, Callback.GetChatMessageCallback chatMessageCallBack) {
            this.chatMessageId = chatMessageId;
            this.chatMessageCallBack = chatMessageCallBack;
        }

        @Override
        protected ChatMessage doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getChatMessageGetByIDUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("chatMessageId", this.chatMessageId + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            ChatMessage returnedChatMessage = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                Log.v("happened", "ChatMessageManager - GetByID");
                returnedChatMessage = buildRecord(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedChatMessage;
        }

        @Override
        protected void onPostExecute(ChatMessage returnedChatMessage) {
            super.onPostExecute(returnedChatMessage);
            if (chatMessageCallBack != null) {
                if (mMsg.equals("")) {
                    chatMessageCallBack.complete(returnedChatMessage);
                } else {
                    chatMessageCallBack.failure(mMsg);
                }
            }
        }
    }

    public static class Update extends AsyncTask<String, Void, Void> {
        ChatMessage chatMessage;
        Callback.GetChatMessageCallback chatMessageCallBack;

        public Update(ChatMessage chatMessage, Callback.GetChatMessageCallback chatMessageCallBack) {
            this.chatMessage = chatMessage;
            this.chatMessageCallBack = chatMessageCallBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getChatMessageUpdateUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("chatMessageId", chatMessage.getChatMessageId() + ""));
            dataToSend.add(new BasicNameValuePair("id", chatMessage.getId() + ""));
            dataToSend.add(new BasicNameValuePair("message", chatMessage.getMessage()));
            dataToSend.add(new BasicNameValuePair("userIdFrom", chatMessage.getUserIdFrom() + ""));
            dataToSend.add(new BasicNameValuePair("userIdTo", chatMessage.getUserIdTo() + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (chatMessageCallBack != null) {
                if (mMsg.equals("")) {
                    chatMessageCallBack.complete(null);
                } else {
                    chatMessageCallBack.failure(mMsg);
                }
            }
        }
    }

    private static class Delete extends AsyncTask<String, Void, Void> {
        int chatMessageId;
        Callback.GetChatMessageCallback chatMessageCallBack;
        int isLogicalDelete;

        public Delete(int chatMessageId, int isLogicalDelete, Callback.GetChatMessageCallback chatMessageCallBack) {
            this.chatMessageId = chatMessageId;
            this.chatMessageCallBack = chatMessageCallBack;
            this.isLogicalDelete = isLogicalDelete;
        }

        @Override
        protected Void doInBackground(String... params) {
            mMsg = "";
            if (params == null)
                return null;

            // get url from params
            String url = AppLink.getChatMessageDeleteUrl();

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("chatMessageId", chatMessageId + ""));
            dataToSend.add(new BasicNameValuePair("isLogicalDelete", isLogicalDelete + ""));

            HttpParams httpRequestParams = ServerUtils.getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(url);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (ConnectTimeoutException cte) {
                mMsg = "Connection Timeout";
            } catch (IOException e) {
                mMsg = "No Network Connection";
            } catch (Exception e) {
                mMsg = e.toString();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (chatMessageCallBack != null) {
                if (mMsg.equals("")) {
                    chatMessageCallBack.complete(null);
                } else {
                    chatMessageCallBack.failure(mMsg);
                }
            }
        }
    }

}
