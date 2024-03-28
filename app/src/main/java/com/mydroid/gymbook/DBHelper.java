package com.mydroid.gymbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mydroid.gymbook.Model.AdapterModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "GYM_BOOK_DB";
    public static final String TABLE_NAME = "GYM_BOOK";
    private static final String KEY_ID = "id";
    private static final String KEY_PIC = "pic";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_NAME+" TEXT, "+KEY_PHONE+" TEXT, "+KEY_AMOUNT+" TEXT, "+KEY_DATE+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public int addMember(String name, String phone, String amount, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME,name);
        cv.put(KEY_PHONE,phone);
        cv.put(KEY_AMOUNT,amount);
        cv.put(KEY_DATE,date);

        db.insert(TABLE_NAME,null,cv);
        db.close();
        return 1;
    }

    public int updateMember(int id, String name, String phone, String amount, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME,name);
        cv.put(KEY_PHONE,phone);
        cv.put(KEY_AMOUNT,amount);
        cv.put(KEY_DATE,date);

        db.update(TABLE_NAME,cv,KEY_ID+" = "+id,null);
        db.close();
        return 1;
    }

    public int deleteMember(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,KEY_ID+" = ? ", new String[]{String.valueOf(id)});
        db.close();
        return 1;
    }

    public ArrayList<AdapterModel> getMember()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        ArrayList<AdapterModel> arrayGBook = new ArrayList<>();

        while (cursor.moveToNext())
        {
            AdapterModel model = new AdapterModel();
            model.id = cursor.getInt(0);
            model.name = cursor.getString(1);
            model.phone = cursor.getString(2);
            model.amount = cursor.getString(3);
            model.date = cursor.getString(4);

            arrayGBook.add(model);
        }
        cursor.close();
        db.close();
        return arrayGBook;
    }
}
