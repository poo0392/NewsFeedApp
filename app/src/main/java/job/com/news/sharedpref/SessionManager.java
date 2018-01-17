package job.com.news.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Zafar.Hussain on 10/11/2017.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MyApp";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_LANGUAGE = "language";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setLanguage(String lang) {
        editor.putString(KEY_LANGUAGE, lang);
        // commit changes
        editor.commit();
    }

    public String getLanguage() {
        return pref.getString(KEY_LANGUAGE, "");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
