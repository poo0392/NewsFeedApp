package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zafar.Hussain on 01/03/2018.
 */

public class CategoryMasterTable {
    //changes 06_03
    public static final String CATEGORY_TABLE_NAME = "category_master";
    // public static final String COLUMN_ID = "column_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    //  public static final String CATEGORY_ID = "category_id"; // changes


    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public CategoryMasterTable(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        db.insert(CATEGORY_TABLE_NAME, null, cv);
    }



}
