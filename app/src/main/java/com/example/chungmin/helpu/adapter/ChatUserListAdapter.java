package com.example.chungmin.helpu.adapter;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.ChatActivity;
import com.example.chungmin.helpu.activities.ChatUserListActivity;
import com.example.chungmin.helpu.activities.CustomerRequestList;
import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ProjectMessages;
import com.example.chungmin.helpu.activities.ServiceProviderListByServiceID;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.enumeration.ServiceType;
import com.example.chungmin.helpu.models.ChatTopic;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;

public class ChatUserListAdapter extends ArrayAdapter<ChatTopic> {
    TextView tvCustomerRequestId, tvUserName, tvServiceProvider, tvProjectDescription, tvSpdrUserId, tvCustUserId;
    Button btnChat;
    private List<ChatTopic> items;
    private Context mContext;
    private Context mActivity;
    private int mUserId = 0;
    private BadgeView badge;
    private ChatTopic mChatTopic;


    public ChatUserListAdapter(Context context, List<ChatTopic> items) {
        super(context, R.layout.user_custom_list, items);
        this.items = items;
        mContext = context;
        mUserId = ((Globals) context.getApplicationContext()).getUserId();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.user_custom_list, null);
        }

        mChatTopic = items.get(position);

        if (mChatTopic != null) {
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvServiceProvider = (TextView) v.findViewById(R.id.tvServiceProvider);
            tvCustomerRequestId = (TextView) v.findViewById(R.id.tvCustomerRequestId);
            tvProjectDescription = (TextView) v.findViewById(R.id.tvProjectDescription);
            tvSpdrUserId = (TextView) v.findViewById(R.id.tvSpdrUserId);
            tvCustUserId = (TextView) v.findViewById(R.id.tvCustUserId);

            btnChat = (Button) v.findViewById(R.id.btnChat);

            tvUserName.setText(mChatTopic.getUserNameCust());
            tvCustUserId.setText(mChatTopic.getUserIdCust() + "");
            tvServiceProvider.setText(mChatTopic.getUserNameSPdr());
            tvSpdrUserId.setText(mChatTopic.getUserIdSPdr() + "");
            tvCustomerRequestId.setText("Customer Request ID : " + Integer.toString((mChatTopic.getCustomerRequestId())));
            tvProjectDescription.setText(mChatTopic.getDescription());

            btnChat.setOnClickListener(new Button.OnClickListener() {
                public void onClick(final View v) {
                    final View superView = (View) v.getParent();
                    tvCustUserId = (TextView) superView.findViewById(R.id.tvCustUserId);
                    tvSpdrUserId = (TextView) superView.findViewById(R.id.tvSpdrUserId);
                    tvUserName = (TextView) superView.findViewById(R.id.tvUserName);
                    tvServiceProvider = (TextView) superView.findViewById(R.id.tvServiceProvider);

                    int custUserId = Integer.parseInt(tvCustUserId.getText().toString());
                    int spdrUserId = Integer.parseInt(tvSpdrUserId.getText().toString());
                    String userNameCust = tvUserName.getText().toString();
                    String userNameSPdr = tvServiceProvider.getText().toString();

                    Intent redirect = new Intent(v.getContext(), ChatActivity.class);
                    Bundle b = new Bundle();
                    if (mUserId == custUserId) {
                        b.putInt("userIdFrom", custUserId);
                        b.putInt("userIdTo", spdrUserId);
                        b.putString("userNameFrom", userNameCust);
                        b.putString("userNameTo", userNameSPdr);
                    } else {
                        b.putInt("userIdFrom", spdrUserId);
                        b.putInt("userIdTo", custUserId);
                        b.putString("userNameFrom", userNameSPdr);
                        b.putString("userNameTo", userNameCust);
                    }

                    redirect.putExtras(b);
                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(redirect);
                    ((Activity) mContext).finish();
                }
            });
        }

        return v;
    }


}
