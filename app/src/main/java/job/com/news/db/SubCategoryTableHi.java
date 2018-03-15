package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zafar.Hussain on 15/03/2018.
 */

public class SubCategoryTableHi {
    public static final String SUB_CATEGORY_HI_TABLE_NAME = "sub_category_hi";
    public static final String SUB_CATEGORY_ID = "sub_category_id";
    public static final String SUB_CATEGORY_NAME_HI = "sub_category_name";
    public static final String CATEGORY_ID = "category_id";

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public SubCategoryTableHi(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        db.insert(SUB_CATEGORY_HI_TABLE_NAME, null, cv);
    }

    public int getSubCategoryIdByName(String sub_cat_name) {
        db = dbHelper.getWritableDatabase();
        int id=0;
        String query1 = "SELECT sub_category_id FROM " + SubCategoryTableHi.SUB_CATEGORY_HI_TABLE_NAME + " where " + SubCategoryTableHi.SUB_CATEGORY_NAME_HI + " = '" + sub_cat_name + "'";
        Cursor cursor = db.rawQuery(query1, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                id = cursor.getInt(cursor.getColumnIndex(SubCategoryTableHi.SUB_CATEGORY_ID));
            }
        }
        return id;
    }
}
