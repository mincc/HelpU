package com.example.chungmin.helpu.functional_test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.example.chungmin.helpu.serverrequest.DatabaseManager;

import junit.framework.Assert;

import org.junit.Rule;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import HelpUTestUtilities.ValueGenerator;

import static android.os.SystemClock.sleep;

/**
 * Created by Chung Min on 10/22/2015.
 */
public class CustomerRequestTest {
    Activity mActivity = null;

    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);

    private CustomerRequest mCreatedCustomerRequest;
    private CustomerRequest mReadCustomerRequest;
    private CustomerRequest mUpdateCustomerRequest;
    private int mTimer = 1000 * 1;

    @org.junit.Ignore
    public void TestCRUD() {
        mActivity = main.getActivity();
        CustomerRequest randomCustomerRequest = GetRandomCustomerRequest();

        Create(randomCustomerRequest);

        //Create ok?
        Assert.assertTrue(mCreatedCustomerRequest.getCustomerRequestId() > 0);
        AssertMatch(mCreatedCustomerRequest, randomCustomerRequest);

        //Read
        Read(mCreatedCustomerRequest);
        AssertMatch(mReadCustomerRequest, mCreatedCustomerRequest);

        //Update inserted record to new random values
        mUpdateCustomerRequest = GetRandomCustomerRequest();
        mUpdateCustomerRequest.setCustomerRequestId(mCreatedCustomerRequest.getCustomerRequestId());
        Update(mUpdateCustomerRequest);

        //Update ok?
        Read(mCreatedCustomerRequest);
        AssertMatch(mUpdateCustomerRequest, mReadCustomerRequest);

        //logical Delete
        Delete(mUpdateCustomerRequest.getCustomerRequestId(), 1);
        int result = CheckResult();
        Assert.assertEquals(1, result);

        //fully Delete
        Delete(mUpdateCustomerRequest.getCustomerRequestId(), 0);
        Read(mUpdateCustomerRequest);

        //Delete ok?
        Read(mUpdateCustomerRequest);
        Assert.assertEquals(null, mReadCustomerRequest);
    }

    private int CheckResult() {
        final CountDownLatch signal = new CountDownLatch(1);

        final int[] result = {0};

        String sql = "SELECT isDelete FROM customerRequest WHERE customerRequestId = " + mUpdateCustomerRequest.getCustomerRequestId();
        DatabaseManager.execSQL(sql, new Callback.GetIntCallback() {
            @Override
            public void complete(int returnValue) {
                result[0] = returnValue;
                signal.countDown();
            }

            @Override
            public void failure(String msg) {

            }
        });

        try {
            signal.await(mTimer, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        sleep(mTimer);
        return result[0];
    }

    private void Delete(int customerRequestId, int isLogicalDelete) {
        final CountDownLatch signal = new CountDownLatch(1);
        CustomerRequestManager.delete(customerRequestId, isLogicalDelete, new Callback.GetCustomerRequestCallback() {

            @Override
            public void complete(CustomerRequest returnedCustomerRequest) {
                signal.countDown();
            }

            @Override
            public void failure(String msg) {
                signal.countDown();
            }
        });

        try {
            signal.await(mTimer, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void Update(CustomerRequest customerRequest) {
        final CountDownLatch signal = new CountDownLatch(1);
        CustomerRequestManager.update(customerRequest, new Callback.GetCustomerRequestCallback() {

            @Override
            public void complete(CustomerRequest returnedCustomerRequest) {
                signal.countDown();
            }

            @Override
            public void failure(String msg) {
                signal.countDown();
            }
        });

        try {
            signal.await(mTimer, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void Read(CustomerRequest customerRequest) {
        final CountDownLatch signal = new CountDownLatch(1);
        mReadCustomerRequest = null;
        CustomerRequestManager.getByID(customerRequest.getCustomerRequestId(), new Callback.GetCustomerRequestCallback() {
            @Override
            public void complete(CustomerRequest returnedCustomerRequest) {
                mReadCustomerRequest = returnedCustomerRequest;
                signal.countDown();
            }

            @Override
            public void failure(String msg) {
                signal.countDown();
            }
        });

        try {
            signal.await(mTimer, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void Create(CustomerRequest testCustomerRequest) {
        final CountDownLatch signal = new CountDownLatch(1);
        CustomerRequestManager.insert(testCustomerRequest, new Callback.GetCustomerRequestCallback() {
            @Override
            public void complete(CustomerRequest returnedCustomerRequest) {
                mCreatedCustomerRequest = returnedCustomerRequest;
                signal.countDown();
            }

            @Override
            public void failure(String msg) {
                signal.countDown();
            }
        });

        try {
            signal.await(mTimer, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private CustomerRequest GetRandomCustomerRequest() {
        CustomerRequest target = new CustomerRequest();
        target.setUserId(ValueGenerator.getRandomInt(1, 3));
        target.setServiceId(ValueGenerator.getRandomInt(1, 10));
        target.setDescription("Sample Description - " + ValueGenerator.getRandomComment());
        target.setProjectStatus(ProjectStatus.values()[ValueGenerator.getRandomInt(1, 3)]);

        return target;
    }

    private void AssertMatch(CustomerRequest obj1, CustomerRequest obj2) {
        Assert.assertEquals(obj1.getUserId(), obj2.getUserId());
        Assert.assertEquals(obj1.getServiceId(), obj2.getServiceId());
        Assert.assertEquals(obj1.getDescription(), obj2.getDescription());
        Assert.assertEquals(obj1.getProjectStatus(), obj2.getProjectStatus());
    }
}
