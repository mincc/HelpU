package HelpUGenericUtilities;

/**
 * Created by Chung Min on 10/1/2015.
 */
public class Enumerations {

    public enum DateStyle {

        HowLong("Minutes to 1h, Hours to 12h, Today, Yesterday, d MMM yyyy", 1),
        PlainDate("dd MMM YYYY", 2),
        PlainDateTime12Hr("dd MMM YYYY hh:mm tt", 3);

        private final String stringValue;
        private final int intValue;

        DateStyle(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int getId() {
            return intValue;
        }
    }

    ;
}
