package com.org.apnanews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.org.apnanews.models.NewsImages;

/**
 * Created by Pooja.Patil on 06/03/2018.
 */

public class NewsImagesTable {
    //changes 06_03
    public static final String NEWS_IMAGES_TABLE_NAME = "NewsImages";
    public static final String COLUMN_ID = "column_id";
    public static final String IMAGE_ID = "id";
    public static final String NEWS_ID = "news_id";
    public static final String NEWS_PIC = "news_pic";
    public static final String IMAGE_CREATED_AT = "created_at";
    public static final String IMAGE_UPDATED_AT = "updated_at";


    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public NewsImagesTable(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        db.insert(NEWS_IMAGES_TABLE_NAME, null, cv);
    }

    public void insertNewsImages(NewsImages model) {
        db = dbHelper.getWritableDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(IMAGE_ID, model.getId());
        cv.put(NEWS_ID, model.getNews_id());
        cv.put(NEWS_PIC, model.getNews_pic());
        cv.put(IMAGE_CREATED_AT, model.getCreated_at());
        cv.put(IMAGE_UPDATED_AT, model.getUpdated_at());

        db.insert(NEWS_IMAGES_TABLE_NAME, null, cv);
      //  Toast.makeText(context, "Data inserted in NewsImages Table", Toast.LENGTH_SHORT).show();
    }

    //
    public List<NewsImages> getNewsImagesList(int news_id) {
        db = dbHelper.getWritableDatabase();
        // Cursor cursor = mDb.query(NewsListTable.NEWS_LIST_TABLE_NAME, null, null, null, null, null, null);
        ArrayList<NewsImages> listAll = new ArrayList<NewsImages>();
        NewsImages model;
        String query1 = "SELECT * FROM " + NEWS_IMAGES_TABLE_NAME + " where " + NEWS_ID + " = " + news_id;
        Cursor cursor = db.rawQuery(query1, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                model = new NewsImages();
                model.setId(cursor.getColumnIndex(IMAGE_ID));
                model.setNews_id(cursor.getString(cursor.getColumnIndex(NEWS_ID)));
                model.setNews_pic(cursor.getString(cursor.getColumnIndex(NEWS_PIC)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(IMAGE_CREATED_AT)));
                model.setUpdated_at(cursor.getString(cursor.getColumnIndex(IMAGE_UPDATED_AT)));

                listAll.add(model);

            }
        }
       // Log.v("DbHelper ", " list from db " + listAll.toString());
        cursor.close();
        db.close();
        return listAll;
    }

}
