package com.example.chungmin.helpu.functional_test;

import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.Login;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Rule;

import HelpUGenericUtilities.DateTimeUtils;
import HelpUGenericUtilities.Enumerations;

import static android.os.SystemClock.sleep;

/**
 * Created by Chung Min on 10/2/2015.
 */
public class DateTimeUtilsTest {

    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);

    @org.junit.Ignore
    public void TestDateToHumanLogic() {
        final long second = 1000;
        final long minute = 60 * second;
        final long hour = 60 * minute;
        final long day = 24 * hour;
        final long week = 7 * day;
        final long avgMonth = 30 * day;
        final long year = 364 * day;

        String result = "";
        DateTime now = DateTime.now();
        DateTime targetDate;

        //region case 1 : Max and min date time
        now = DateTime.now();
        targetDate = new DateTime(0);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("never", result);

        now = DateTime.now();
        targetDate = new DateTime(292278994);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("never", result);
        //endregion

        //region case 2 : second
        now = DateTime.now();
        targetDate = now.minus(second);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("1 second ago", result);

        now = DateTime.now();
        targetDate = now.minus(2 * second);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 seconds ago", result);

        now = DateTime.now();
        targetDate = now.plus(2 * second);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 1 second", result);

        now = DateTime.now();
        targetDate = now.plus(3 * second);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 seconds", result);
        //endregion

        //region case 3 : minutes
        now = DateTime.now();
        targetDate = now.minus(minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("1 minute ago", result);

        now = DateTime.now();
        targetDate = now.minus(2 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 minutes ago", result);

        now = DateTime.now();
        targetDate = now.plus(2 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 1 minute", result);

        now = DateTime.now();
        targetDate = now.plus(3 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 minutes", result);
        //endregion

        //region case 4 : hours
        now = DateTime.now();
        targetDate = now.minus(hour);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("1 hour ago", result);

        now = DateTime.now();
        targetDate = now.minus(2 * hour);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 hours ago", result);

        now = DateTime.now();
        targetDate = now.plus(2 * hour);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 1 hour", result);

        now = DateTime.now();
        targetDate = now.plus(3 * hour);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 hours", result);
        //endregion

        //region case 5 : days
        now = DateTime.now();
        targetDate = now.minus(day);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("Yesterday", result);

        now = DateTime.now();
        targetDate = now.minus(2 * day);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 days ago", result);

        now = DateTime.now();
        targetDate = now.plus(1 * day + hour);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("Tomorrow", result);

        now = DateTime.now();
        targetDate = now.plus(2 * day);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 days", result);

        now = DateTime.now();
        targetDate = now.plus(3 * day);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 3 days", result);
        //endregion

        //region case 6 : weeks
        now = DateTime.now();
        targetDate = now.minus(week);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("1 week ago", result);

        now = DateTime.now();
        targetDate = now.minus(2 * week);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 weeks ago", result);

        now = DateTime.now();
        targetDate = now.plus(week + 1 * second); //add 1 sec because the time keep reduce when code run
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 1 week", result);

        now = DateTime.now();
        targetDate = now.plus(2 * week + 1 * second);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 weeks", result);
        //endregion

        //region case 7 : months
        now = DateTime.now();
        targetDate = now.minus(avgMonth);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("1 month ago", result);

        now = DateTime.now();
        targetDate = now.minus(2 * avgMonth);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 months ago", result);

        now = DateTime.now();
        targetDate = now.plus(1 * avgMonth + 1 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 1 month", result);

        now = DateTime.now();
        targetDate = now.plus(2 * avgMonth + 1 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 months", result);
        //endregion

        //region case 8 : years
        now = DateTime.now();
        targetDate = now.minus(year);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("1 year ago", result);

        now = DateTime.now();
        targetDate = now.minus(2 * year);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("2 years ago", result);

        now = DateTime.now();
        targetDate = now.plus(1 * year + 1 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 1 year", result);

        now = DateTime.now();
        targetDate = now.plus(2 * year + 1 * minute);
        result = DateTimeUtils.ToHuman(targetDate, Enumerations.DateStyle.HowLong, main.getActivity());
        Assert.assertEquals("in 2 years", result);
        //endregion
    }

    @org.junit.Ignore
    public void TestPlainDateLogic() {
        String result = "";
        DateTime targetDateTime = new DateTime(2015, 10, 5, 1, 48, 30);

        result = DateTimeUtils.ToHuman(targetDateTime, Enumerations.DateStyle.PlainDate);
        Assert.assertEquals("5 Oct 2015", result);

        result = DateTimeUtils.ToHuman(targetDateTime, Enumerations.DateStyle.PlainDateTime12Hr);
        Assert.assertEquals("5 Oct 2015 01:48:30", result);

    }

    @org.junit.Ignore
    public void TestDatabaseParseToDateTime() {
        //date get from database
        String date = "2015-10-05 15:56:34";
        DateTime targetDateTime = DateTimeUtils.parseTo(date);
        DateTime dateTime = new DateTime(2015, 10, 5, 15, 56, 34);

        Assert.assertEquals(targetDateTime.getYear(), dateTime.getYear());
        Assert.assertEquals(targetDateTime.getMonthOfYear(), dateTime.getMonthOfYear());
        Assert.assertEquals(targetDateTime.getDayOfYear(), dateTime.getDayOfYear());
        Assert.assertEquals(targetDateTime.getHourOfDay(), dateTime.getHourOfDay());
        Assert.assertEquals(targetDateTime.getMinuteOfDay(), dateTime.getMinuteOfDay());
        Assert.assertEquals(targetDateTime.getSecondOfDay(), dateTime.getSecondOfDay());

    }
}
