package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {

    private static final String TAG = "myDebug";

    Item item;
    ArrayList<Item> items;

    ItemAdapterSL itemAdapter;
    RecyclerView itemList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.masterList) {

            //Navigate to Master List activity
            startActivity(new Intent(this, MasterList.class));

            return true;
        }
        else if (id == R.id.ShoppingList)
        {
            startActivity(new Intent(this, ShoppingList.class));

            return true;
        }
        else if (id == R.id.AddItem)
        {
            startActivity(new Intent(this, AddItem.class));

            return true;
        }
/*        else if (id == R.id.DeleteItems)
        {
            startActivity(new Intent(this, DeleteItem.class));

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        this.setTitle("Today's Shopping List");

        ReadFromTextFile();
    }

    @Override
    public void onResume()
    {
        try
        {
            super.onResume();


            itemList = findViewById(R.id.rvShoppingItems);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            itemList.setLayoutManager(layoutManager);

            itemAdapter = new ItemAdapterSL(items, this);

           itemList.setAdapter(itemAdapter);



            for (Item t: items)
            {
                Log.d(TAG, "onResume: " + t.Name +" "+ t.CheckedState);
            }

        }
        catch (Exception e)
        {
            Log.d(TAG, "onResume: " + e.getMessage());
        }

    }

    private void ReadFromTextFile() {

        FileIO fileIO = new FileIO();

        Integer counter = 0;
        String[] data ;//= new String [items.size()];
        //for(Item t : items) data[counter++] = t.toString();



        //Read the data out of the file
        ArrayList<String> strData = fileIO.readFile(this);
        items = new ArrayList<Item>();

        for(String s : strData)
        {
            data = s.split("\\|");
            if(Boolean.parseBoolean(data[2]) == true)
            {
                items.add(new Item(Integer.parseInt(data[0]),data[1],Boolean.parseBoolean(data[2])));
            }

        }



    }

    }
