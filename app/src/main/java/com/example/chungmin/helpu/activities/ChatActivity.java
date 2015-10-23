package com.example.chungmin.helpu.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.adapter.ChatAdapter;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.ChatMessage;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.serverrequest.ChatMessageManager;
import com.example.chungmin.helpu.sqlite.DBMessageController;

import org.joda.time.DateTime;

public class ChatActivity extends HelpUBaseActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private static final String TAG = "ChatActivity";
    private DBMessageController db = new DBMessageController(this);
    private int mUserId = 0;
    private int mUserIdFrom = 0;
    private int mUserIdTo = 0;
    private static String mUsernameFrom = "";
    private static String mUsernameTo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mUserId = ((Globals) getApplication()).getUserId();

        //get from bundle
        Bundle b = getIntent().getExtras();
        mUserIdFrom = b.getInt("userIdFrom", 0);
        mUsernameFrom = (String) b.get("userNameFrom");
        mUserIdTo = b.getInt("userIdTo", 0);
        mUsernameTo = (String) b.get("userNameTo");

        initControls();
    }

    //register activity onResume()
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter("ChatActivityBroadcastReceiver"));
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String messageText = intent.getStringExtra("message");
            int senderId = intent.getIntExtra("senderId", 0);
            int receiverId = intent.getIntExtra("receiverId", 0);

            if (mUserIdFrom == receiverId && mUserIdTo == senderId) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageText);
                chatMessage.setUserIdFrom(senderId);
                chatMessage.setUserIdTo(receiverId);
                chatMessage.setCreatedDate(DateTime.now());

                //do other stuff here
                displayMessage(chatMessage);
            }
        }
    };

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        meLabel.setText(mUsernameFrom);
        companionLabel.setText(mUsernameTo);

//        DeleteAllData();
//        InsertData();

        loadHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageText);
                chatMessage.setUserIdFrom(mUserIdFrom);
                chatMessage.setUserIdTo(mUserIdTo);
                chatMessage.setCreatedDate(DateTime.now());

                Long rowId = db.insert(chatMessage);
                displayMessage(chatMessage);

                chatMessage.setId(rowId);
                updateServerAndSendPushNotification(chatMessage);

                messageET.setText("");
            }
        });
    }

    private void updateServerAndSendPushNotification(ChatMessage chatMessage) {
        ChatMessageManager serverRequest = new ChatMessageManager();
        serverRequest.insert(chatMessage, new Callback.GetChatMessageCallback() {
            @Override
            public void complete(ChatMessage returnedChatMessage) {
                //send email with new password
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadHistory() {
        List<ChatMessage> chatMessageList = db.getAllMessages(mUserIdFrom, mUserIdTo);

        for (ChatMessage chatMessage : chatMessageList) {
            String log = "Id : " + chatMessage.getId()
                    + " ,Message : " + chatMessage.getMessage()
                    + " ,User ID FROM : " + chatMessage.getUserIdFrom()
                    + " ,User ID TO : " + chatMessage.getUserIdTo()
                    + " ,Created Date : " + chatMessage.getCreatedDate();

            // Writing Contacts to log
            Log.d(TAG, log);
        }

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>(), mUserId);
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatMessageList.size(); i++) {
            ChatMessage message = chatMessageList.get(i);
            displayMessage(message);
        }
    }

    private void InsertData() {

        Log.d(TAG, "Inserting ..");
        db.insert(new ChatMessage(0, 0, "Hi", mUserIdFrom, mUserIdTo, DateTime.now(), 0));
        db.insert(new ChatMessage(0, 0, "How Are you?", mUserIdFrom, mUserIdTo, DateTime.now(), 0));
        db.insert(new ChatMessage(0, 0, "I am fine here", mUserIdTo, mUserIdFrom, DateTime.now(), 0));
        db.insert(new ChatMessage(0, 0, "How life going on", mUserIdTo, mUserIdFrom, DateTime.now(), 0));

    }

    private void DeleteAllData() {
        db.deleteAll();
    }
}
