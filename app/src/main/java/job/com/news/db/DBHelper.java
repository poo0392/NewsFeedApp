package job.com.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by POOJA on 2/10/2018.
 */

public class DBHelper  {
    private static DBHelper mInstance = null;
    private DBHelper mDbHelper;
    public static SQLiteDatabase mDb;
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


    /*public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }
*/
    public DbHelper(Context ctx) {
        context = ctx;
        mDbHelper = new DatabaseHelper(context);
    }

    public class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(query_member);
            db.execSQL(query_news_list);
            Log.v("DBHelper ", "Database created on create");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + MemberTable.MEMBER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + NewsListTable.NEWS_LIST_TABLE_NAME);

            // Create tables again
            onCreate(db);
        }
    }
        public DBHelper open() throws SQLException {
            mDb = mDbHelper.getWritableDatabase();
            return this;
        }

        public void close() {
            mDbHelper.close();
        }





}
