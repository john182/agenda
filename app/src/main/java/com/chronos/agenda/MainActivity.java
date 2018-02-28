package com.chronos.agenda;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends SQLiteOpenHelper {


    public MainActivity(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String[] parametros = new String[]{"1"};

        SQLiteDatabase db2 =  getReadableDatabase();
        String str = "";
        Cursor cursor = db2.rawQuery(str, new String[]{""});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
