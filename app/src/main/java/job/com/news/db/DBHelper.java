package job.com.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static job.com.news.db.MemberTable.MEMBER_ID;

/**
 * Created by POOJA on 2/10/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    //chnages added on 3/9/2018.
    public static SQLiteDatabase mDb;
    private static DBHelper mInstance = null;
    private Context context;
    public static final String DATABASE_NAME = "NewsApp";
    private static final int DATABASE_VERSION = 1;
//changes 16_03
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    String personal_details = " CREATE TABLE IF NOT EXISTS " + PersonalDetails.TABLE_NAME +
            "(" + PersonalDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PersonalDetails.MEMBER_ID + " INTEGER," + PersonalDetails.MEMBER_TOKEN + " TEXT,"
            + PersonalDetails.FIRST_NAME + " TEXT," + PersonalDetails.LAST_NAME + " TEXT,"
            + PersonalDetails.EMAIL_ID + " TEXT," + PersonalDetails.MOBILE + " TEXT," + PersonalDetails.ROLE + " TEXT,"
            + PersonalDetails.STATUS + " TEXT)";

    String query_member = " CREATE TABLE IF NOT EXISTS " + MemberTable.MEMBER_TABLE_NAME +
            "(" + MemberTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MEMBER_ID + " INTEGER," + MemberTable.MEMBER_TOKEN + " TEXT,"
            + MemberTable.FIRST_NAME + " TEXT," + MemberTable.LAST_NAME + " TEXT,"
            + MemberTable.EMAIL_ID + " TEXT," + MemberTable.MOBILE + " TEXT," + MemberTable.STATUS + " TEXT)";


    String category_query = " CREATE TABLE IF NOT EXISTS " + CategoryMasterTable.CATEGORY_TABLE_NAME +
            "("/*+CategoryMasterTable.COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"*/ +
            CategoryMasterTable.CATEGORY_ID + " INTEGER," + CategoryMasterTable.CATEGORY_NAME + " TEXT)";

    String sub_category_query = " CREATE TABLE IF NOT EXISTS " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME +
            "(" /*+ SubCategoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"*/ +
            SubCategoryTable.SUB_CATEGORY_ID + " INTEGER," + SubCategoryTable.SUB_CATEGORY_NAME + " TEXT," +
            SubCategoryTable.CATEGORY_ID + " INTEGER," +
            " FOREIGN KEY(" + SubCategoryTable.CATEGORY_ID + ") REFERENCES " +
            CategoryMasterTable.CATEGORY_TABLE_NAME + "(category_id)" + ")";

    String sub_category_query_mr = " CREATE TABLE IF NOT EXISTS " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME +
            "(" + SubCategoryTableMr.SUB_CATEGORY_ID + " INTEGER," + SubCategoryTableMr.SUB_CATEGORY_NAME_MR + " TEXT," +
            SubCategoryTableMr.CATEGORY_ID + " INTEGER," +
            " FOREIGN KEY(" + SubCategoryTableMr.CATEGORY_ID + ") REFERENCES " +
            CategoryMasterTable.CATEGORY_TABLE_NAME + "(category_id)" + ")";

    String sub_category_query_hi = " CREATE TABLE IF NOT EXISTS " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME +
            "(" + SubCategoryTableHi.SUB_CATEGORY_ID + " INTEGER," + SubCategoryTableHi.SUB_CATEGORY_NAME_HI + " TEXT," +
            SubCategoryTableHi.CATEGORY_ID + " INTEGER," +
            " FOREIGN KEY(" + SubCategoryTableHi.CATEGORY_ID + ") REFERENCES " +
            CategoryMasterTable.CATEGORY_TABLE_NAME + "(category_id)" + ")";

    String query_news_list = " CREATE TABLE IF NOT EXISTS " + NewsListTable.NEWS_LIST_TABLE_NAME +
            "(" + NewsListTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NewsListTable.NEWS_ID + " INTEGER," + NewsListTable.NEWS_UUID + " TEXT," +
            NewsListTable.CATEGORY + " TEXT," + NewsListTable.CATEGORY_ID + " TEXT," +
            NewsListTable.SUB_CATEGORY + " TEXT," + NewsListTable.SUB_CATEGORY_ID + " TEXT," +
            NewsListTable.COUNTRY + " TEXT," + NewsListTable.STATE + " TEXT," + NewsListTable.CITY + " TEXT," +
            NewsListTable.NEWS_TITLE + " TEXT," + NewsListTable.NEWS_DESCRIPTION + " TEXT,"
            + NewsListTable.LANGUAGE + " TEXT,"  +NewsListTable.COMMENT + " TEXT,"  +
            NewsListTable.LIKE_COUNT + " TEXT," + NewsListTable.MEMBER_ID + " INTEGER," +
            NewsListTable.CREATED_AT + " TEXT,"
            + NewsListTable.IS_UPDATED + " TEXT," + NewsListTable.STATUS + " TEXT," +
            " FOREIGN KEY(" + NewsListTable.MEMBER_ID + ") REFERENCES " +
            MemberTable.MEMBER_TABLE_NAME + "(id)" + "," +
             " FOREIGN KEY(" + NewsListTable.CATEGORY_ID + ") REFERENCES " +
            CategoryMasterTable.CATEGORY_TABLE_NAME + "(category_id)" +
            " FOREIGN KEY(" + NewsListTable.CATEGORY + ") REFERENCES " +
            CategoryMasterTable.CATEGORY_TABLE_NAME + "(category_name)" + ")";

    String images_query = " CREATE TABLE IF NOT EXISTS " + NewsImagesTable.NEWS_IMAGES_TABLE_NAME +
            "(" + NewsImagesTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NewsImagesTable.IMAGE_ID + " INTEGER," + NewsImagesTable.NEWS_ID + " TEXT," +
            NewsImagesTable.NEWS_PIC + " TEXT," + NewsImagesTable.IMAGE_CREATED_AT + " TEXT," +
            NewsImagesTable.IMAGE_UPDATED_AT + " TEXT)";
      /*
  public void Reset() {
        mDbHelper.onUpgrade(mDb, 1, 1);
    }
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
        db.execSQL(category_query);
        db.execSQL(sub_category_query);
        db.execSQL(sub_category_query_mr);
        db.execSQL(sub_category_query_hi);
        db.execSQL(images_query);

        insertCategory(db);
        insertSubCategory(db);
        insertSubCategoryMR(db);
        insertSubCategoryHI(db);
        Toast.makeText(context, "Table Created in db", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + personal_details);
        db.execSQL("DROP TABLE IF EXISTS " + query_member);
        db.execSQL("DROP TABLE IF EXISTS " + query_news_list);
        db.execSQL("DROP TABLE IF EXISTS " + category_query);
        db.execSQL("DROP TABLE IF EXISTS " + sub_category_query);
        db.execSQL("DROP TABLE IF EXISTS " + sub_category_query_mr);
        db.execSQL("DROP TABLE IF EXISTS " + sub_category_query_hi);
        db.execSQL("DROP TABLE IF EXISTS " + images_query);
        onCreate(db);
    }


    public void insertCategory(SQLiteDatabase db) {
//        db = this.getWritableDatabase();
        db.beginTransaction();

        String ls_sql;
        /*ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(1,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(2,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(3,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(4,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(5,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(6,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(7,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(8,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(9,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(10,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(11,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(12,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
        ls_sql="INSERT INTO "+CATEGORY_TABLE_NAME+" VALUES(13,"+context.getResources().getString(R.string.national_inter_menu)+")"; db.execSQL(ls_sql);
*/

        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(1,'National and International')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(2,'Government News')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(3,'Social and Related News')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(4,'Sports')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(5,'Science and Technology')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(6,'Health Related')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(7,'Business News')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(8,'Agricultural News')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(9,'Cinema Related')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(10,'Small Classifieds')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(11,'Other and Uncategorized News')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(12,'Entertainment News')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(13,'Career Related')";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + CategoryMasterTable.CATEGORY_TABLE_NAME + " VALUES(14,'Economical News')";
        db.execSQL(ls_sql);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void insertSubCategory(SQLiteDatabase db) {
        //   db = this.getWritableDatabase();
        db.beginTransaction();

        String ls_sql;
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(1,'Property',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(2,'Birthday Ads',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(3,'App Related Ads',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(4,'Buy and Sell',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(5,'Services',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(6,'Loan related',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(7,'Matrimony related',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(8,'Books and Literature',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(9,'Job',13)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(10,'Business',13)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTable.SUB_CATEGORY_TABLE_NAME + " VALUES(11,'Educational',13)";
        db.execSQL(ls_sql);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void insertSubCategoryHI(SQLiteDatabase db) {
        //   db = this.getWritableDatabase();
        db.beginTransaction();

        String ls_sql;
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(1,'संपत्ति',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(2,'जन्मदिन विज्ञापन',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(3,'ऐप संबंधित विज्ञापन',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(4,'खरीदो और बेचो',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(5,'सेवाएं',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(6,'संबंधित ऋण',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(7,'विवाह संबंधित',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(8,'किताबें और साहित्य',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(9,'काम',13)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(10,'व्यापार',13)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " VALUES(11,'शिक्षात्मक',13)";
        db.execSQL(ls_sql);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void insertSubCategoryMR(SQLiteDatabase db) {
        //   db = this.getWritableDatabase();
        db.beginTransaction();

        String ls_sql;
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(1,'मालमत्ता',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(2,'वाढदिवस जाहिराती',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(3,'अॅप संबंधित जाहिराती',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(4,'खरेदी आणि विक्री',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(5,'सेवा',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(6,'कर्ज संबंधित',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(7,'विवाह संबंधित',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(8,'पुस्तके आणि साहित्य',10)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(9,'जॉब',13)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(10,'व्यवसाय',13)";
        db.execSQL(ls_sql);
        ls_sql = "INSERT INTO " + SubCategoryTableMr.SUB_CATEGORY_MR_TABLE_NAME + " VALUES(11,'शैक्षणिक',13)";
        db.execSQL(ls_sql);

        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
