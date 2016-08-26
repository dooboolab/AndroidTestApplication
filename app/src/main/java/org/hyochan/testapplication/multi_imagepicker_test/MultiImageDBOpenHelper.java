package org.hyochan.testapplication.multi_imagepicker_test;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hyochan on 2016-08-26.
 */
public class MultiImageDBOpenHelper {

    private static final String DATABASE_NAME = "multiimage.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context context;
    public static final String TABLE_MULTI_IMAGE = "multi_image";

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
                    + "thumb_uri TEXT, "
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

    public MultiImageDBOpenHelper(Context context){
        this.context = context;
    }

    public MultiImageDBOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

}