package edu.ags.grocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "item.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ITEM= "CREATE table item "
            + " (id integer primary key autoincrement, "
            + "name text not null,"
            + "isInCart int,"
            + "checkedstate int); ";

    public ItemDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
