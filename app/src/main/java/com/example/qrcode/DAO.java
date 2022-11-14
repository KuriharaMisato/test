package com.example.qrcode;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DAO {
    private SQLiteDatabase db;
    private final String TABLE = "History";
    public DAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(History history){
        ContentValues values = new ContentValues();
        values.put("content", history.content);
        values.put("date", history.strDate);
        return db.insert(TABLE, null, values);
    }

    public int delete(int id){
        return db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
    }

    public List<History> getAll(){
        String sql = "SELECT * FROM "+TABLE;
        return getData(sql);
    }

    @SuppressLint("Range")
    private List<History> getData(String sql , String...selectionArgs){
        List<History> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            History obj = new History();
            obj.id = Integer.parseInt((cursor.getString(cursor.getColumnIndex("id"))));
            obj.content = (cursor.getString(cursor.getColumnIndex("content")));
            obj.strDate = (cursor.getString(cursor.getColumnIndex("date")));
            list.add(obj);
        }
        return list;
    }
}
