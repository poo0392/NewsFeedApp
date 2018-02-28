package job.com.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static job.com.news.db.MemberTable.MEMBER_ID;

/**
 * Created by POOJA on 2/10/2018.
 */

public class DBHelper extends SQLiteOpenHelper{

    public static SQLiteDatabase mDb;
    private static DBHelper mInstance = null;
    private Context context;
    public static final String DATABASE_NAME = "NewsApp";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }
    public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    String personal_details=" CREATE TABLE IF NOT EXISTS " + PersonalDetails.TABLE_NAME +
            "(" + PersonalDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PersonalDetails.MEMBER_ID + " INTEGER," + PersonalDetails.MEMBER_TOKEN + " TEXT,"
            + PersonalDetails.FIRST_NAME + " TEXT," + PersonalDetails.LAST_NAME + " TEXT,"
            + PersonalDetails.EMAIL_ID + " TEXT," + PersonalDetails.MOBILE + " TEXT," + PersonalDetails.STATUS + " TEXT)";

    String query_member = " CREATE TABLE IF NOT EXISTS " + MemberTable.MEMBER_TABLE_NAME +
            "(" + MemberTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MEMBER_ID + " INTEGER," + MemberTable.MEMBER_TOKEN + " TEXT,"
            + MemberTable.FIRST_NAME + " TEXT," + MemberTable.LAST_NAME + " TEXT,"
            + MemberTable.EMAIL_ID + " TEXT," + MemberTable.MOBILE + " TEXT," + MemberTable.STATUS + " TEXT)";

    String query_news_list = " CREATE TABLE IF NOT EXISTS " + NewsListTable.NEWS_LIST_TABLE_NAME +
            "(" + NewsListTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NewsListTable.NEWS_ID + " INTEGER," + NewsListTable.NEWS_UUID + " TEXT," + NewsListTable.CATEGORY + " TEXT," +
            NewsListTable.COUNTRY + " TEXT," + NewsListTable.STATE + " TEXT," + NewsListTable.CITY + " TEXT," +
            NewsListTable.NEWS_TITLE + " TEXT," + NewsListTable.NEWS_DESCRIPTION + " TEXT," + NewsListTable.NEWS_PIC + " TEXT," +
            NewsListTable.LIKE_COUNT + " TEXT," + NewsListTable.MEMBER_ID + " INTEGER," +
            NewsListTable.CREATED_AT + " TEXT,"
            + NewsListTable.IS_UPDATED + " TEXT," + NewsListTable.STATUS + " TEXT," +
            " FOREIGN KEY(" + NewsListTable.MEMBER_ID + ") REFERENCES " +
            MemberTable.MEMBER_TABLE_NAME + "(id)" + ")";


      /*
  public void Reset() {
        mDbHelper.onUpgrade(mDb, 1, 1);
    }
        */
   /* public DBHelper(Context ctx) {
        context = ctx;
        mDbHelper = new DatabaseHelper(context);
    }

    public DBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(personal_details);
        db.execSQL(query_member);
        db.execSQL(query_news_list);
        Toast.makeText(context, "Table Created in db", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + personal_details);
        db.execSQL("DROP TABLE IF EXISTS " + query_member);
        db.execSQL("DROP TABLE IF EXISTS " + query_news_list);
        onCreate(db);
    }





}
