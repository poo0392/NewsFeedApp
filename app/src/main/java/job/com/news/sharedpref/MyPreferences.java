package job.com.news.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by deepak on 6/21/2017.
 */

public class MyPreferences {

    static MyPreferences myAppPreferences;
    ManipulatePref manipulatePref;
    private SharedPreferences sharedPreferences;

    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("newsFeedPref", Context.MODE_PRIVATE);
        manipulatePref = new ManipulatePref();
    }

    public static MyPreferences getMyAppPref(Context mContext) {
        if (null == myAppPreferences) {

            myAppPreferences = new MyPreferences(mContext);
        }
        return myAppPreferences;
    }
    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences("NewsFeed", Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }
    //First Name
    public String getFirstName() {
        return manipulatePref.getString("firstname");
    }

    public void setFirstName(String name) {
        manipulatePref.setString("firstname", name);
    }

    //Last Name
    public String getLastName() {
        return manipulatePref.getString("lastname");
    }

    public void setLastName(String name) {
        manipulatePref.setString("lastname", name);
    }

    //Email ID
    public String getEmailId() {
        return manipulatePref.getString("emailid");
    }

    public void setEmailId(String id) {
        manipulatePref.setString("emailid", id);
    }

    //mobile number
    public void setMobile(String no) {
        manipulatePref.setString("mobilenumber", no);
    }

    public String getMobile() {
        return manipulatePref.getString("mobilenumber");
    }

    //member id
    public void setMemberId(int id) {
        manipulatePref.setInt("memberId", id);
    }

    public int getMemberId() {
        return manipulatePref.getInt("memberId");
    }

    //memeer token
    public String getMemberToken() {
        return manipulatePref.getString("membertoken");
    }

    public void setMemberToken(String token) {
        manipulatePref.setString("membertoken", token);
    }

    class ManipulatePref {

        final String DEFAULT_Str = null;
        final int DEFAULT_INT = 1;

        public void setString(String key, String value) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString(key, value);
            prefsEditor.apply();
        }

        public void setInt(String key, int value) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putInt(key, value);
            prefsEditor.apply();
        }

        public String getString(String key) {
            if (sharedPreferences != null) {
                return sharedPreferences.getString(key, DEFAULT_Str);
            }
            return DEFAULT_Str;
        }

        public int getInt(String key) {
            if (sharedPreferences != null) {
                return sharedPreferences.getInt(key, DEFAULT_INT);
            }
            return DEFAULT_INT;
        }
        public boolean getBoolean(String key) {
            if (sharedPreferences != null) {
                return sharedPreferences.getBoolean(key, false);
            }
            return false;
        }
        public void setBoolena(String key, boolean value) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putBoolean(key, value);
            prefsEditor.apply();
        }




    }
}
