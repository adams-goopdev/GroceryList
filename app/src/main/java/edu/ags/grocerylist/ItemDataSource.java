package edu.ags.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ItemDataSource {

    public static final String TAG = "myDebug";
    private static final String ITEM = "item";
    private SQLiteDatabase database;
    private ItemDBHelper dbHelper;

    public ItemDataSource(Context context){
        dbHelper = new ItemDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public boolean delete(int id){

        boolean didSucceed = false;
        try {
            didSucceed = database.delete(ITEM,"id=" + id, null) >0;
        }
        catch (Exception e)
        {
            Log.d(TAG, "delete: Error " + e.getMessage());
        }
        return didSucceed;
    }

    public boolean update(Item item) {
        boolean didSucceed = false;

        try {
            ContentValues updateValues = new ContentValues();

            //Set the values
            Long id = (long)item.getId();

            updateValues.put("name",item.getName());
            updateValues.put("checkedstate",item.getCheckedState());

            Log.d(TAG, "Update line: " + updateValues);
            didSucceed = database.update(ITEM, updateValues,"id=" + id, null) > 0;
        }
        catch (Exception e)
        {
            Log.d(TAG, "Update line: Error " + e.getMessage());
        }
        return didSucceed;
    }

    public boolean insert(Item item) {

        boolean didSucceed = false;

        try {
            ContentValues initialValues = new ContentValues();
            //Set the values
            initialValues.put("name",item.getName());
            initialValues.put("checkedstate",item.getCheckedState());

            Log.d(TAG, "insert: " + initialValues);
            didSucceed = database.insert(ITEM,null, initialValues) > 0;
        }
        catch (Exception e)
        {
            Log.d(TAG, "insert: Error " + e.getMessage());
        }
        return didSucceed;
    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            String query = "Select * from Item";
            Cursor cursor = database.rawQuery(query, null);

            Item item;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setCheckedState((cursor.getInt(2)));
                items.add(item);
                cursor.moveToNext();

                Log.d(TAG, "getItems: " + item);

            }
            cursor.close();
        }
        catch (Exception e)
        {
            Log.d(TAG, "getItems: " + e.getMessage());
        }
        return items;
    }

    public Item getItem(int id){

        Item item = item = new Item();
        try {
            String query = "Select * from Item WHERE Id = " + id;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            if(cursor.moveToFirst()){
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setCheckedState((cursor.getInt(2)));
                cursor.close();
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "getItems: " + e.getMessage());
        }
        return item;
    }

    public ArrayList<Item> getSLItems(){
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            String query = "Select * from Item Where checkedstate = 1";
            Cursor cursor = database.rawQuery(query, null);

            Item item;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setCheckedState((cursor.getInt(2)));
                items.add(item);
                cursor.moveToNext();

                Log.d(TAG, "getItems: " + item);

            }
            cursor.close();
        }
        catch (Exception e)
        {
            Log.d(TAG, "getItems: " + e.getMessage());
        }
        return items;
    }


}
