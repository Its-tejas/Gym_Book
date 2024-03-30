package com.mydroid.gymbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mydroid.gymbook.Model.AdapterModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "GYM_BOOK_DB";
    public static final String TABLE_NAME = "GYM_BOOK";
    private static final String KEY_ID = "id";
    private static final String KEY_PIC_PATH = "pic_path";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    private static final String FIREBASE_MEMBER_ID = "member_id";

    FirebaseDatabase database;
    DatabaseReference reference;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("gym_members");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_PIC_PATH+" TEXT, "+KEY_NAME+" TEXT, "+KEY_PHONE+" TEXT, "+KEY_AMOUNT+" TEXT, "+KEY_DATE+" TEXT, "+FIREBASE_MEMBER_ID+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public int addMember(String pic_path, String name, String phone, String amount, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        AdapterModel member = new AdapterModel( pic_path,  name,  phone,  amount,  date);
        DatabaseReference newMemberRef = reference.push();
        newMemberRef.setValue(member);
        String memberId = newMemberRef.getKey(); // Store the generated key

        cv.put(KEY_PIC_PATH,pic_path);
        cv.put(KEY_NAME,name);
        cv.put(KEY_PHONE,phone);
        cv.put(KEY_AMOUNT,amount);
        cv.put(KEY_DATE,date);
        cv.put(FIREBASE_MEMBER_ID, memberId);


        db.insert(TABLE_NAME,null,cv);
        db.close();

        return 1;
    }

    public int updateMember(int id, String pic_path, String name, String phone, String amount, String date, String firebase_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        AdapterModel member = new AdapterModel(pic_path, name, phone, amount, date);
        reference.child(firebase_id).setValue(member);

        cv.put(KEY_PIC_PATH,pic_path);
        cv.put(KEY_NAME,name);
        cv.put(KEY_PHONE,phone);
        cv.put(KEY_AMOUNT,amount);
        cv.put(KEY_DATE,date);
        cv.put(FIREBASE_MEMBER_ID, firebase_id);

        db.update(TABLE_NAME,cv,KEY_ID+" = "+id,null);
        db.close();

        return 1;
    }

    public int deleteMember(int id, String firebase_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        reference.child(firebase_id).removeValue();

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
            model.pic_path = cursor.getString(1);
            model.name = cursor.getString(2);
            model.phone = cursor.getString(3);
            model.amount = cursor.getString(4);
            model.date = cursor.getString(5);
            model.firebase_id = cursor.getString(6);

            arrayGBook.add(model);
        }
        cursor.close();
        db.close();
        return arrayGBook;
    }
}
