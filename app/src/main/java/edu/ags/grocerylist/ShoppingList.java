package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    CheckBox checkBox;
    TextView textView;



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

            checkBox = findViewById(R.id.cbML);

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

    private void ReadFromTextFile()
    {

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
