package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import job.com.news.register.RegisterMember;

/**
 * Created by POOJA on 2/11/2018.
 */

public class MemberTable {
    public static final String MEMBER_TABLE_NAME = "Member";
    public static final String COLUMN_ID = "id";
    public static final String MEMBER_ID = "member_id";
    public static final String MEMBER_TOKEN = "member_token";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL_ID = "email_id";
    public static final String MOBILE = "mobile";
    public static final String STATUS = "status";

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public MemberTable(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        db.insert(MEMBER_TABLE_NAME, null, cv);
    }

    public void insertMembers(RegisterMember model) {
        db = dbHelper.getWritableDatabase();
        Log.v("DB ", "MEMBER_ID " + model.getMemberId());
        ContentValues cv = new ContentValues();
        cv.put(MemberTable.MEMBER_ID, model.getMemberId());
        cv.put(MemberTable.MEMBER_TOKEN, model.getMemberToken());
        cv.put(MemberTable.FIRST_NAME, model.getFirstName());
        cv.put(MemberTable.LAST_NAME, model.getLastName());
        cv.put(MemberTable.EMAIL_ID, model.getEmailId());
        cv.put(MemberTable.MOBILE, model.getMobile());
        cv.put(MemberTable.STATUS, "A");

        db.insert(MemberTable.MEMBER_TABLE_NAME, null, cv);
        Toast.makeText(context, "Data inserted in db", Toast.LENGTH_SHORT).show();
    }

    public boolean checkUser(int member_id) {
        db = dbHelper.getWritableDatabase();
        String query = "select * from Member where " + MemberTable.MEMBER_ID + " = " + member_id;

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

    public List<RegisterMember> getMember() {
        db = dbHelper.getWritableDatabase();
        ArrayList<RegisterMember> listAll = new ArrayList<RegisterMember>();
        RegisterMember model;
        String query1 = "SELECT * FROM " + MemberTable.MEMBER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query1, null);
        while (cursor != null && cursor.moveToNext()) {
            model = new RegisterMember();
            model.setMemberId(cursor.getColumnIndex(MemberTable.MEMBER_ID));
            model.setMemberToken(cursor.getString(cursor.getColumnIndex(MemberTable.MEMBER_TOKEN)));
            model.setFirstName(cursor.getString(cursor.getColumnIndex(MemberTable.FIRST_NAME)));
            model.setLastName(cursor.getString(cursor.getColumnIndex(MemberTable.LAST_NAME)));
            model.setEmailId(cursor.getString(cursor.getColumnIndex(MemberTable.EMAIL_ID)));
            model.setMobile(cursor.getString(cursor.getColumnIndex(MemberTable.MOBILE)));
            listAll.add(model);
        }
        Log.v("DbHelper ", " list from db " + listAll.toString());
        cursor.close();
        db.close();
        return listAll;
    }

    public List<RegisterMember> getMemberListByMemberId(int member_id) {// here member_id = news_list.member_id
        db = dbHelper.getWritableDatabase();

        ArrayList<RegisterMember> listAll = new ArrayList<RegisterMember>();
        RegisterMember model;
        // String query1 = "SELECT * FROM " + MemberTable.MEMBER_TABLE_NAME + " where " + MemberTable.MEMBER_ID + " = " + member_id;
        String query1 = "select * from " + MemberTable.MEMBER_TABLE_NAME + " m INNER JOIN " + NewsListTable.NEWS_LIST_TABLE_NAME
                + " n ON " + " m." + MemberTable.MEMBER_ID + " = " + " n." + NewsListTable.MEMBER_ID + " where "
                + "n." + NewsListTable.MEMBER_ID + " = " + member_id;
        Cursor cursor = db.rawQuery(query1, null);
        //  while (cursor != null && cursor.moveToNext()) {
        while (cursor.moveToNext()) {
            model = new RegisterMember();
            model.setMemberId(cursor.getColumnIndex(MemberTable.MEMBER_ID));
            model.setMemberToken(cursor.getString(cursor.getColumnIndex(MemberTable.MEMBER_TOKEN)));
            model.setFirstName(cursor.getString(cursor.getColumnIndex(MemberTable.FIRST_NAME)));
            model.setLastName(cursor.getString(cursor.getColumnIndex(MemberTable.LAST_NAME)));
            model.setEmailId(cursor.getString(cursor.getColumnIndex(MemberTable.EMAIL_ID)));
            model.setMobile(cursor.getString(cursor.getColumnIndex(MemberTable.MOBILE)));
            listAll.add(model);
        }
        Log.v("DbHelper ", " MemList(mrm_id) " + listAll.toString());
        cursor.close();
        db.close();
        return listAll;


    }

    public int getMemberByMemberId(int member_id) {// here member_id = news_list.member_id
        int memberId = 0;
        db = dbHelper.getWritableDatabase();

        RegisterMember model;
        // String query1 = "SELECT * FROM " + MemberTable.MEMBER_TABLE_NAME + " where " + MemberTable.MEMBER_ID + " = " + member_id;
        String query1 = "select * from " + MemberTable.MEMBER_TABLE_NAME + " m INNER JOIN " + NewsListTable.NEWS_LIST_TABLE_NAME
                + " n ON " + " m." + MemberTable.MEMBER_ID + " = " + " n." + NewsListTable.MEMBER_ID + " where "
                + "n." + NewsListTable.MEMBER_ID + " = " + member_id;
        Cursor cursor = db.rawQuery(query1, null);
        //  while (cursor != null && cursor.moveToNext()) {
        while (cursor.moveToNext()) {
            // model = new RegisterMember();
            // model.setMemberId(cursor.getColumnIndex(MemberTable.MEMBER_ID));
            memberId = cursor.getColumnIndex(MemberTable.MEMBER_ID);

        }
        cursor.close();
        db.close();
        return memberId;
    }

}
