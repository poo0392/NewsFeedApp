package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import job.com.news.register.RegisterMember;

/**
 * Created by POOJA on 2/20/2018.
 */

public class PersonalDetails {
    private static String TAG = "PersonalDetails";
    public static String TABLE_NAME = "personal_details";
    public static String _ID = "_id";
    public static final String MEMBER_ID = "member_id";
    public static final String MEMBER_TOKEN = "member_token";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL_ID = "email_id";
    public static final String MOBILE = "mobile";
    public static final String STATUS = "status";

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public PersonalDetails(Context context)
    {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv)
    {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);
    }

    public void insertMember(RegisterMember model) {
        db = dbHelper.getWritableDatabase();
      //  Log.v("DB ", "MEMBER_ID " + model.getMemberId());
        ContentValues cv = new ContentValues();
        cv.put(PersonalDetails.MEMBER_ID, model.getMemberId());
        cv.put(PersonalDetails.MEMBER_TOKEN, model.getMemberToken());
        cv.put(PersonalDetails.FIRST_NAME, model.getFirstName());
        cv.put(PersonalDetails.LAST_NAME, model.getLastName());
        cv.put(PersonalDetails.EMAIL_ID, model.getEmailId());
        cv.put(PersonalDetails.MOBILE, model.getMobile());
        cv.put(PersonalDetails.STATUS, "A");

        db.insert(PersonalDetails.TABLE_NAME, null, cv);
        Toast.makeText(context, "Data inserted in db", Toast.LENGTH_SHORT).show();
    }
}
