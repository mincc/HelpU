package com.example.chungmin.helpu.functional_test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.ChatMessage;
import com.example.chungmin.helpu.serverrequest.ChatMessageManager;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Rule;

import java.text.DateFormat;
import java.util.Date;

import HelpUTestUtilities.ValueGenerator;

import static android.os.SystemClock.sleep;

/**
 * Created by Chung Min on 10/19/2015.
 */
public class ChatMessageTest {
    Activity mActivity = null;

    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);

    private ChatMessage mCreatedChatMessage;
    private ChatMessage mReadChatMessage;
    private ChatMessage mUpdateChatMessage;
    private int mTimer = 1000 * 5;

    @org.junit.Ignore
    public void TestCRUD() {
        mActivity = main.getActivity();
        ChatMessage randomChatMessage = GetRandomChatMessage();

        Create(randomChatMessage);
        sleep(mTimer);

        //Create ok?
        Assert.assertTrue(mCreatedChatMessage.getChatMessageId() > 0);
        AssertMatch(mCreatedChatMessage, randomChatMessage);

        //Read
        Read(mCreatedChatMessage);
        sleep(mTimer);
        AssertMatch(mReadChatMessage, mCreatedChatMessage);

        //Update inserted record to new random values
        mUpdateChatMessage = GetRandomChatMessage();
        mUpdateChatMessage.setChatMessageId(mCreatedChatMessage.getChatMessageId());
        Update(mUpdateChatMessage);

        //Update ok?
        Read(mCreatedChatMessage);
        sleep(mTimer);
        AssertMatch(mUpdateChatMessage, mReadChatMessage);

        //Delete
        Delete(mUpdateChatMessage.getChatMessageId());
        Read(mUpdateChatMessage);
        sleep(mTimer);

        //Delete ok?
        Read(mUpdateChatMessage);
        sleep(mTimer);
        Assert.assertEquals(null, mReadChatMessage);
    }

    private void Delete(int chatMessageId) {
        ChatMessageManager.delete(chatMessageId, 0, new Callback.GetChatMessageCallback() {

            @Override
            public void complete(ChatMessage returnedChatMessage) {

            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private void Update(ChatMessage chatMessage) {
        ChatMessageManager.update(chatMessage, new Callback.GetChatMessageCallback() {

            @Override
            public void complete(ChatMessage returnedChatMessage) {

            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private void Read(ChatMessage chatMessage) {
        mReadChatMessage = null;
        ChatMessageManager.getByID(chatMessage.getChatMessageId(), new Callback.GetChatMessageCallback() {
            @Override
            public void complete(ChatMessage returnedChatMessage) {
                mReadChatMessage = returnedChatMessage;
            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private void Create(ChatMessage testChatMessage) {
        ChatMessageManager.insert(testChatMessage, new Callback.GetChatMessageCallback() {
            @Override
            public void complete(ChatMessage returnedChatMessage) {
                mCreatedChatMessage = returnedChatMessage;
            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private ChatMessage GetRandomChatMessage() {
        ChatMessage target = new ChatMessage();
        target.setId(ValueGenerator.getRandomInt(1, 1000));
        target.setChatMessageId(0);
        int userIdFrom = ValueGenerator.getRandomInt(1, 2);
        int userIdTo = 0;
        if (userIdFrom == 1) {
            userIdTo = 2;
        } else {
            userIdTo = 1;
        }

        target.setMessage("Message Test : " + ValueGenerator.getRandomComment());
        target.setUserIdFrom(userIdFrom);
        target.setUserIdTo(userIdTo);
        target.setCreatedDate(DateTime.now());

        return target;
    }

    private void AssertMatch(ChatMessage obj1, ChatMessage obj2) {
        Assert.assertEquals(obj1.getId(), obj2.getId());
        Assert.assertEquals(obj1.getMessage(), obj2.getMessage());
        Assert.assertEquals(obj1.getUserIdFrom(), obj2.getUserIdFrom());
        Assert.assertEquals(obj1.getUserIdTo(), obj2.getUserIdTo());
    }

}
