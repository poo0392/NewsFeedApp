package com.org.apnanews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.org.apnanews.register.RegisterMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by POOJA on 2/20/2018.
 */
//chnages added on 3/9/2018.
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
    public static final String ROLE = "role";
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
        cv.put(PersonalDetails.ROLE, model.getRole());
        cv.put(PersonalDetails.STATUS, "A");

        db.insert(PersonalDetails.TABLE_NAME, null, cv);
       // Toast.makeText(context, "Data inserted in db", Toast.LENGTH_SHORT).show();
    }

    public boolean checkUser(int member_id) {
        db = dbHelper.getWritableDatabase();
        String query = "select * from Member where " + PersonalDetails.MEMBER_ID + " = " + member_id;

        Cursor cursor = db.rawQuery(query, null);
        int cursorCount = cursor.getCount();
        //   if (cursor.getCount() > 0) {
        cursor.close();
        // mDb.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public List<RegisterMember> getMemberListByMemberId(int member_id) {// here member_id = news_list.member_id
        db = dbHelper.getWritableDatabase();

        ArrayList<RegisterMember> listAll = new ArrayList<RegisterMember>();
        RegisterMember model;
        // String query1 = "SELECT * FROM " + MemberTable.MEMBER_TABLE_NAME + " where " + MemberTable.MEMBER_ID + " = " + member_id;
      String query1="select * from "+PersonalDetails.TABLE_NAME+" where "+PersonalDetails.MEMBER_ID+" = "+member_id;
        Cursor cursor = db.rawQuery(query1, null);
        //  while (cursor != null && cursor.moveToNext()) {
        while (cursor.moveToNext()) {
            model = new RegisterMember();
            model.setMemberId(cursor.getColumnIndex(PersonalDetails.MEMBER_ID));
            model.setMemberToken(cursor.getString(cursor.getColumnIndex(PersonalDetails.MEMBER_TOKEN)));
            model.setFirstName(cursor.getString(cursor.getColumnIndex(PersonalDetails.FIRST_NAME)));
            model.setLastName(cursor.getString(cursor.getColumnIndex(PersonalDetails.LAST_NAME)));
            model.setEmailId(cursor.getString(cursor.getColumnIndex(PersonalDetails.EMAIL_ID)));
            model.setMobile(cursor.getString(cursor.getColumnIndex(PersonalDetails.MOBILE)));
            listAll.add(model);
        }
        // Log.v("DbHelper ", " MemList(mrm_id) " + listAll.toString());
        cursor.close();
        db.close();
        return listAll;


    }
}
