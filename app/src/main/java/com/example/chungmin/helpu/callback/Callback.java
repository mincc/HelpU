package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.AppStats;
import com.example.chungmin.helpu.models.ChatMessage;
import com.example.chungmin.helpu.models.ChatTopic;
import com.example.chungmin.helpu.models.CustomerIssue;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Rating;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.Transaction;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserStats;

import java.util.List;

/**
 * Created by Chung Min on 9/24/2015.
 */
public class Callback {
    public interface CountCallback {
        void complete(int data);

        void failure(String msg);
    }

    public interface GetAppStatsCallback {
        void complete(AppStats returnedAppStats);

        void failure(String msg);
    }

    public interface GetBooleanCallback {
        void complete(Boolean isTrigger);

        void failure(String msg);
    }

    public interface GetCustomerRequestCallback {
        void complete(CustomerRequest returnedCustomerRequest);

        void failure(String msg);
    }

    public interface GetCustomerRequestListCallback {
        void complete(List<CustomerRequest> data);

        void failure(String msg);
    }

    public interface GetRatingCallback {
        void complete(Rating returnedRating);

        void failure(String msg);
    }

    public interface GetServiceProviderCallback {
        void complete(ServiceProvider returnedServiceProvider);

        void failure(String msg);
    }

    public interface GetServiceProviderListCallback {
        void complete(List<ServiceProvider> returnedServiceProviderList);

        void failure(String msg);
    }

    public interface GetTransactionCallback {
        void complete(Transaction returnedTransaction);

        void failure(String msg);
    }

    public interface GetUserCallback {
        void complete(User returnedUser);

        void failure(String msg);
    }

    public interface GetUserStatsCallback {
        void complete(UserStats returnedUserStats);

        void failure(String msg);
    }

    public interface GetCustomerIssueCallback {
        void complete(CustomerIssue returnedCustomerIssue);

        void failure(String msg);
    }

    public interface GetChatMessageCallback {
        void complete(ChatMessage returnedChatMessage);

        void failure(String msg);
    }

    public interface GetChatTopicListCallback {
        void complete(List<ChatTopic> returnedChatTopicList);

        void failure(String msg);
    }

    public interface GetIntCallback {
        void complete(int result);

        void failure(String msg);
    }
}
