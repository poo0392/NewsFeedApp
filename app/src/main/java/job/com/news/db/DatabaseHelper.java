package job.com.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zafar.Hussain on 12/02/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;
    private Context context;
    private static final String DATABASE_NAME = "NewsApp";
    private static final int DATABASE_VERSION = 1;

    String query_member = " CREATE TABLE IF NOT EXISTS " + MemberTable.MEMBER_TABLE_NAME +
            "(" + MemberTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MemberTable.MEMBER_ID + " INTEGER," + MemberTable.MEMBER_TOKEN + " TEXT,"
            + MemberTable.FIRST_NAME + " TEXT," + MemberTable.LAST_NAME + " TEXT,"
            + MemberTable.EMAIL_ID + " TEXT," + MemberTable.MOBILE + " TEXT," + MemberTable.STATUS + " TEXT)";

    String query_news_list = " CREATE TABLE IF NOT EXISTS " + NewsListTable.NEWS_LIST_TABLE_NAME +
            "(" + NewsListTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NewsListTable.NEWS_ID + " INTEGER," + NewsListTable.NEWS_UUID + " TEXT," + NewsListTable.CATEGORY + " TEXT," +
            NewsListTable.COUNTRY + " TEXT," + NewsListTable.STATE + " TEXT," + NewsListTable.CITY + " TEXT," +
            NewsListTable.NEWS_TITLE + " TEXT," + NewsListTable.NEWS_DESCRIPTION + " TEXT," + NewsListTable.NEWS_PIC + " TEXT," +
            NewsListTable.LIKE_COUNT + " TEXT," + NewsListTable.MEMBER_ID + " INTEGER," + NewsListTable.CREATED_AT + " TEXT,"
            + NewsListTable.STATUS + " TEXT)";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(query_member);
        db.execSQL(query_news_list);
        //      Toast.makeText(mCtx, "Table Created in db", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + query_member);
        db.execSQL("DROP TABLE IF EXISTS " + query_news_list);
        onCreate(db);
    }
}
