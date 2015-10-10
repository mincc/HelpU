package HelpUGenericUtilities;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chung Min on 8/22/2015.
 */
public class ValidationUtils {
    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }


    public static boolean isEditTextFill(EditText editText) {
        if (editText.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isSpinnerSelected(int value) {
        if (value == 0) {
            return false;
        } else {
            return true;
        }
    }
}
