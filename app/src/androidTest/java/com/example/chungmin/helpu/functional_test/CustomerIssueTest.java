package com.example.chungmin.helpu.functional_test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.CustomerIssue;
import com.example.chungmin.helpu.serverrequest.CustomerIssueManager;

import junit.framework.Assert;

import org.junit.Rule;

import HelpUTestUtilities.ValueGenerator;

import static android.os.SystemClock.sleep;

/**
 * Created by Chung Min on 9/25/2015.
 */
public class CustomerIssueTest {
    Activity mActivity = null;

    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);
    private CustomerIssue mCreatedCustomerIssue;
    private CustomerIssue mReadCustomerIssue;
    private CustomerIssue mUpdateCustomerIssue;
    private int mTimer = 1000 * 5;

    @org.junit.Ignore
    public void TestCRUD() {
        mActivity = main.getActivity();
        CustomerIssue randomCustomerIssue = GetRandomCustomerIssue();

        Create(randomCustomerIssue);
        sleep(mTimer);

        //Create ok?
        Assert.assertTrue(mCreatedCustomerIssue.getCustomerIssueId() > 0);
        AssertMatch(mCreatedCustomerIssue, randomCustomerIssue);

        //Read
        Read(mCreatedCustomerIssue);
        sleep(mTimer);
        AssertMatch(mReadCustomerIssue, mCreatedCustomerIssue);

        //Update inserted record to new random values
        mUpdateCustomerIssue = GetRandomCustomerIssue();
        mUpdateCustomerIssue.setCustomerIssueId(mCreatedCustomerIssue.getCustomerIssueId());
        Update(mUpdateCustomerIssue);

        //Update ok?
        Read(mCreatedCustomerIssue);
        sleep(mTimer);
        AssertMatch(mUpdateCustomerIssue, mReadCustomerIssue);

        //Delete
        Delete(mUpdateCustomerIssue.getCustomerIssueId());
        Read(mUpdateCustomerIssue);
        sleep(mTimer);

        //Delete ok?
        Read(mUpdateCustomerIssue);
        sleep(mTimer);
        Assert.assertEquals(null, mReadCustomerIssue);
    }

    private void Delete(int customerIssueId) {
        CustomerIssueManager.delete(customerIssueId, 0, new Callback.GetCustomerIssueCallback() {

            @Override
            public void complete(CustomerIssue returnedCustomerIssue) {

            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private void Update(CustomerIssue customerIssue) {
        CustomerIssueManager.update(customerIssue, new Callback.GetCustomerIssueCallback() {

            @Override
            public void complete(CustomerIssue returnedCustomerIssue) {

            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private void Read(CustomerIssue customerIssue) {
        CustomerIssueManager.getByID(customerIssue.getCustomerIssueId(), new Callback.GetCustomerIssueCallback() {
            @Override
            public void complete(CustomerIssue returnedCustomerIssue) {
                mReadCustomerIssue = returnedCustomerIssue;
            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private void Create(CustomerIssue testCustomerIssue) {
        CustomerIssueManager.insert(testCustomerIssue, new Callback.GetCustomerIssueCallback() {
            @Override
            public void complete(CustomerIssue returnedCustomerIssue) {
                mCreatedCustomerIssue = returnedCustomerIssue;
            }

            @Override
            public void failure(String msg) {

            }
        });
    }

    private CustomerIssue GetRandomCustomerIssue() {
        CustomerIssue target = new CustomerIssue();
        target.setCustomerIssueId(0);
        target.setUserId(ValueGenerator.getRandomInt(1, 3));
        target.setSubject("Test Subject : " + ValueGenerator.getRandomArticleTitle());
        target.setDescription("Test Description : " + ValueGenerator.getRandomComment());
        target.setCustomerIssueStatusId(ValueGenerator.getRandomInt(1, 5));
        target.setCustomerIssueTypeId(ValueGenerator.getRandomInt(1, 4));
        return target;
    }

    private void AssertMatch(CustomerIssue obj1, CustomerIssue obj2) {
        Assert.assertEquals(obj1.getUserId(), obj2.getUserId());
        Assert.assertEquals(obj1.getSubject(), obj2.getSubject());
        Assert.assertEquals(obj1.getDescription(), obj2.getDescription());
        Assert.assertEquals(obj1.getCustomerIssueStatusId(), obj2.getCustomerIssueStatusId());
        Assert.assertEquals(obj1.getCustomerIssueTypeId(), obj2.getCustomerIssueTypeId());
        Assert.assertEquals(obj1.getCustomerIssueTypeId(), obj2.getCustomerIssueTypeId());
    }

}
