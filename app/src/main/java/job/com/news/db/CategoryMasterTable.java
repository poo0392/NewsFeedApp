package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    /*   public int getCategoryIdByName(String catgName){
           db = dbHelper.getWritableDatabase();
           int id;
           String query1 = "select DISTINCT category_master.category_id from category_master  INNER JOIN  NewsList  ON NewsList.category = category_master.category_name";
           Cursor cursor = db.rawQuery(query1, null);

           return id;
       }*/
    public ArrayList<Integer> getCategoryId() {
        db = dbHelper.getWritableDatabase();
        // Cursor cursor = mDb.query(NewsListTable.NEWS_LIST_TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Integer> listAll = new ArrayList<Integer>();
        String query1 = "SELECT " + CategoryMasterTable.CATEGORY_ID + " FROM " + CategoryMasterTable.CATEGORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query1, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                listAll.add(cursor.getInt(cursor.getColumnIndex(CategoryMasterTable.CATEGORY_ID)));
            }
        }
        cursor.close();
        db.close();
        return listAll;
    }

    public int getCategoryIdByName(String cat_name){
        db = dbHelper.getWritableDatabase();
        // Cursor cursor = mDb.query(NewsListTable.NEWS_LIST_TABLE_NAME, null, null, null, null, null, null);
     int id=0;
        String query1 = "SELECT " + CategoryMasterTable.CATEGORY_ID + " FROM " + CategoryMasterTable.CATEGORY_TABLE_NAME +" where "+CategoryMasterTable.CATEGORY_NAME+" = '"+cat_name+"'";
        Cursor cursor = db.rawQuery(query1, null);

        while (cursor.moveToNext()) {
            id = (cursor.getInt(cursor.getColumnIndex(CategoryMasterTable.CATEGORY_ID)));

        }
        cursor.close();
        db.close();
        return id;
    }

}
