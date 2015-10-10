package HelpUGenericUtilities;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.chungmin.helpu.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


/**
 * Created by Chung Min on 10/1/2015.
 */
public class DateTimeUtils {
    private static final long AVERAGE_MONTH_IN_MILLIS = DateUtils.DAY_IN_MILLIS * 30;

    /// <summary>
    /// Translates a DateTime value to human readable.
    /// </summary>
    /// <param name="value">The DateTime value.</param>
    /// <param name="format">How the date is to be displayed.</param>
    /// <returns></returns>
    public static String ToHuman(DateTime value, Enumerations.DateStyle format) {
        return ToHuman(value, format, null);
    }

    public static String ToHuman(DateTime value, Enumerations.DateStyle format, Context context) {

        switch (format) {
            case PlainDate:
                return value.toString("d MMM yyyy");
            case PlainDateTime12Hr:
                return value.toString("d MMM yyyy hh:mm:ss");
            case HowLong:
            default:
                DateTime minDT = new DateTime(0);
                DateTime maxDT = new DateTime(292278994);

                if (value.equals(minDT) || value.equals(maxDT))
                    return context.getString(R.string.strNeverReachDate);

                long time = value.getMillis();
                final long now = new Date().getTime();
                final long delta = Math.abs(now - time);
                boolean past = (now >= time);
                long resolution;

                if (delta <= DateUtils.MINUTE_IN_MILLIS) {
                    resolution = DateUtils.SECOND_IN_MILLIS;
                } else if (delta <= DateUtils.HOUR_IN_MILLIS) {
                    resolution = DateUtils.MINUTE_IN_MILLIS;
                } else if (delta <= DateUtils.DAY_IN_MILLIS) {
                    resolution = DateUtils.HOUR_IN_MILLIS;
                } else if (delta <= DateUtils.WEEK_IN_MILLIS) {
                    resolution = DateUtils.DAY_IN_MILLIS;
                } else if (delta <= AVERAGE_MONTH_IN_MILLIS) {
                    if (past) {
                        if (DateUtils.WEEK_IN_MILLIS <= delta && delta < DateUtils.WEEK_IN_MILLIS * 2) {
                            return context.getString(R.string.strHowLongAgoWeek);
                        } else {
                            return String.format(context.getString(R.string.strHowLongAgoWeeks), Integer.toString((int) (delta / DateUtils.WEEK_IN_MILLIS)));
                        }
                    } else {
                        if (DateUtils.WEEK_IN_MILLIS <= delta && delta < DateUtils.WEEK_IN_MILLIS * 2) {
                            return context.getString(R.string.strHowLongToGoWeek);
                        } else {
                            return String.format(context.getString(R.string.strHowLongToGoWeeks), Integer.toString((int) (delta / DateUtils.WEEK_IN_MILLIS)));
                        }
                    }
                } else if (delta <= DateUtils.YEAR_IN_MILLIS) {
                    if (past) {
                        if (AVERAGE_MONTH_IN_MILLIS <= delta && delta < AVERAGE_MONTH_IN_MILLIS * 2) {
                            return context.getString(R.string.strHowLongAgoMonth);
                        } else {
                            return String.format(context.getString(R.string.strHowLongAgoMonths), Integer.toString((int) (delta / AVERAGE_MONTH_IN_MILLIS)));
                        }
                    } else {
                        if (AVERAGE_MONTH_IN_MILLIS <= delta && delta < AVERAGE_MONTH_IN_MILLIS * 2) {
                            return context.getString(R.string.strHowLongToGoMonth);
                        } else {
                            return String.format(context.getString(R.string.strHowLongToGoMonths), Integer.toString((int) (delta / AVERAGE_MONTH_IN_MILLIS)));
                        }
                    }
                } else {
                    if (past) {
                        if (DateUtils.YEAR_IN_MILLIS <= delta && delta < DateUtils.YEAR_IN_MILLIS * 2) {
                            return context.getString(R.string.strHowLongAgoYear);
                        } else {
                            return String.format(context.getString(R.string.strHowLongAgoYears), Integer.toString((int) (delta / DateUtils.YEAR_IN_MILLIS)));
                        }
                    } else {
                        if (DateUtils.YEAR_IN_MILLIS <= delta && delta < DateUtils.YEAR_IN_MILLIS * 2) {
                            return context.getString(R.string.strHowLongToGoYear);
                        } else {
                            return String.format(context.getString(R.string.strHowLongToGoYears), Integer.toString((int) (delta / DateUtils.YEAR_IN_MILLIS)));
                        }
                    }
                }


                return DateUtils.getRelativeTimeSpanString(time, now, resolution).toString();
        }

    }

    public static DateTime parseTo(String strDateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.parseDateTime(strDateTime);
    }
}
