package com.example.chungmin.helpu.ui_test.custom;

import android.support.test.espresso.matcher.BoundedMatcher;

import com.example.chungmin.helpu.enumeration.CustomerIssueTypes;
import com.example.chungmin.helpu.models.CustomerIssue;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Service;
import com.example.chungmin.helpu.models.ServiceProvider;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Chung Min on 9/14/2015.
 */
public class CustomMatchers {
    /**
     * Matches a Service with a specific ID
     */
    public static Matcher<Object> withServiceId(final int serviceId) {
        return new BoundedMatcher<Object, Service>(Service.class) {
            @Override
            protected boolean matchesSafely(Service service) {
                return serviceId == service.getId();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + serviceId);
            }
        };
    }

    /**
     * Matches a Service with a specific name
     */
    public static Matcher<Object> withServiceName(final String serviceName) {
        return new BoundedMatcher<Object, Service>(Service.class) {
            @Override
            protected boolean matchesSafely(Service service) {
                return serviceName.equals(service.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + serviceName);
            }
        };
    }

    /**
     * Matches CustomerRequest with a specific description
     */
    public static Matcher<Object> customerRequestWithDescription(final String description) {
        return new BoundedMatcher<Object, CustomerRequest>(CustomerRequest.class) {
            @Override
            protected boolean matchesSafely(CustomerRequest customerRequest) {
                return description.equals(customerRequest.getDescription());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + description);
            }
        };
    }

    /**
     * Matches CustomerRequest with a specific email
     */
    public static Matcher<Object> customerRequestWithEmail(final String email) {
        return new BoundedMatcher<Object, CustomerRequest>(CustomerRequest.class) {
            @Override
            protected boolean matchesSafely(CustomerRequest customerRequest) {
                return email.equals(customerRequest.getUserEmail());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + description);
            }
        };
    }

    /**
     * Matches Service Provider with a specific email
     */
    public static Matcher<Object> serviceProviderWithEmail(final String email) {
        return new BoundedMatcher<Object, ServiceProvider>(ServiceProvider.class) {
            @Override
            protected boolean matchesSafely(ServiceProvider serviceProvider) {
                return email.equals(serviceProvider.getEmail());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + email);
            }
        };
    }

    /**
     * Matches Service Provider with a specific phone
     */
    public static Matcher<Object> serviceProviderWithPhone(final String phone) {
        return new BoundedMatcher<Object, ServiceProvider>(ServiceProvider.class) {
            @Override
            protected boolean matchesSafely(ServiceProvider serviceProvider) {
                return phone.equals(serviceProvider.getPhone());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + phone);
            }
        };
    }

    /**
     * Matches CustomerRequest with a specific id
     */
    public static Matcher<Object> customerRequestWithId(final int customerRequestId) {
        return new BoundedMatcher<Object, CustomerRequest>(CustomerRequest.class) {
            @Override
            protected boolean matchesSafely(CustomerRequest customerRequest) {
                if (customerRequestId == customerRequest.getCustomerRequestId()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + customerRequestId);
            }
        };
    }

    /**
     * Matches CustomerIssue with a specific type
     */
    public static Matcher<Object> withCustomerIssueType(final String typeName) {
        return new BoundedMatcher<Object, CustomerIssueTypes>(CustomerIssueTypes.class) {
            @Override
            protected boolean matchesSafely(CustomerIssueTypes customerIssueType) {
                return typeName.equals(customerIssueType.toString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with type name: " + typeName);
            }
        };
    }

    /**
     * Matches a CustomerIssueTypeId with a specific ID
     */
    public static Matcher<Object> withCustomerIssueTypeId(final int customerIssueTypeId) {
        return new BoundedMatcher<Object, CustomerIssueTypes>(CustomerIssueTypes.class) {
            @Override
            protected boolean matchesSafely(CustomerIssueTypes customerIssueType) {
                return customerIssueTypeId == customerIssueType.getId();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with customer issue type id: " + customerIssueTypeId);
            }
        };
    }
}
