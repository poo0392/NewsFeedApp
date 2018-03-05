package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zafar.Hussain on 01/03/2018.
 */

public class SubCategoryTable {
    //added on 01/03
    public static final String SUB_CATEGORY_TABLE_NAME = "sub_category";
    public static final String COLUMN_ID = "column_id";
    public static final String SUB_CATEGORY_ID = "sub_category_id";
    public static final String SUB_CATEGORY_NAME = "sub_category_name";
    public static final String CATEGORY_ID = "category_id";
  //  public static final String CATEGORY_ID = "category_id"; // changes

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public SubCategoryTable(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        db.insert(SUB_CATEGORY_TABLE_NAME, null, cv);
    }


}
