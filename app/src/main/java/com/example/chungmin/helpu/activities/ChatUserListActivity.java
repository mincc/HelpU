package com.example.chungmin.helpu.activities;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.chungmin.helpu.adapter.ChatUserListAdapter;
import com.example.chungmin.helpu.adapter.CustomerRequestAdapter;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.ChatTopic;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.serverrequest.ChatTopicManager;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.chungmin.helpu.R;

public class ChatUserListActivity extends HelpUBaseActivity {

    private static final String TAG = "ChatUserListActivity";
    private static String mTypeList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_list);

        setTitle("Project Discussion");

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            GetProjectListFragment list = new GetProjectListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class GetProjectListFragment extends ListFragment {
        private Context mContext;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mContext = getActivity();
            initView();

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Getting list view from xml
            ListView lv = getListView();

            // Creating a button - Load More
            Button btnDone = new Button(mContext);
            btnDone.setText(R.string.strDone);
            btnDone.setBackgroundResource(R.drawable.custom_btn_beige);
            btnDone.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent redirect = new Intent(v.getContext(), ChatActivity.class);
                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(redirect);
                    getActivity().finish();
                }
            });

            // Adding button to list view at footer
            lv.addFooterView(btnDone);

        }

        private void initView() {

            UserLocalStore userLocalStore = new UserLocalStore(mContext);
            User user = userLocalStore.getLoggedInUser();
            int userId = user.getUserId();

            ChatTopicManager.getListByUserId(userId, new Callback.GetChatTopicListCallback() {

                @Override
                public void complete(List<ChatTopic> data) {
                    // create new adapter
                    ChatUserListAdapter adapter = new ChatUserListAdapter(mContext, data);
                    // set the adapter to list
                    setListAdapter(adapter);
                }

                @Override
                public void failure(String msg) {
                    msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                    ((HelpUBaseActivity) mContext).showAlert(msg);
                }
            });
        }
    }

//    private void updateUI(String userList) {
//        //get userlist from the intents and update the list
//
//        String[] userListArr = userList.split(":");
//
//        Log.d(TAG,"userListArr: "+userListArr.length+" tostr "+userListArr.toString());
//
//        //remove empty strings :-)
//        List<String> list = new ArrayList<String>();
//        for(String s : userListArr) {
//            if(s != null && s.length() > 0) {
//                list.add(s);
//            }
//        }
//        userListArr = list.toArray(new String[list.size()]);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, userListArr);
//        setListAdapter(adapter);
//
//    }
//
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//
//        super.onListItemClick(l, v, position, id);
//
//        // ListView Clicked item index
//        int itemPosition     = position;
//
//        // ListView Clicked item value
//        String  itemValue    = (String) l.getItemAtPosition(position);
//
//        content.setText("User selected: " +itemValue);
//
//
//        Intent i = new Intent(getApplicationContext(),
//                ChatActivity.class);
//        i.putExtra("TOUSER",itemValue);
//        startActivity(i);
//        finish();
//    }


}
