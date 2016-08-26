package org.hyochan.testapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.hyochan.testapplication.multi_imagepicker_test.MultiImageItem;

/**
 * Created by hyochan on 2016-08-26.
 */
public class DBOpenHelper {

    private static final String DATABASE_NAME = "multiimage.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper dbHelper;
    private Context context;
    public static final String TABLE_MULTI_IMAGE = "multi_image";


    private final String KEY_ID = "_id";
    private final String KEY_MAIN_URI = "main_uri";
    private final String KEY_IMG_NAME = "img_name";

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_MULTI_IMAGE +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "main_uri TEXT,"
                    + "img_name TEXT)");
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_MULTI_IMAGE);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context){
        this.context = context;
    }

    public DBOpenHelper open() throws SQLException{
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = dbHelper.getWritableDatabase();
        return this;
    }

    public int addItem(MultiImageItem multiImageItem){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MAIN_URI, multiImageItem.getStrUri());
        values.put(KEY_IMG_NAME, multiImageItem.getimageName());

        int insertId = (int) db.insert(TABLE_MULTI_IMAGE, null, values);
        db.close();

        return insertId;
    }

    public MultiImageItem getItem(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MULTI_IMAGE,
                new String[]{KEY_ID, KEY_MAIN_URI, KEY_IMG_NAME}, KEY_ID + "=?",
                new String[] {String.valueOf(KEY_ID)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        MultiImageItem item = new MultiImageItem(cursor.getString(1), cursor.getString(2));
        item.setId(cursor.getInt(0));

        return item;
    }

    public int deleteItem(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] whereArgs = {String.valueOf(id)};
        int rowAffected = db.delete(TABLE_MULTI_IMAGE,  "_id = ?", whereArgs);
        dbHelper.close();

        return rowAffected;
    }

    public void close(){
        mDB.close();
    }

}