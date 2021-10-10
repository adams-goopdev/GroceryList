package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MasterList extends AppCompatActivity {

    public static final String TAG = "myDebug";
    Item item;
    ArrayList<Item> items;

    ItemAdapter itemAdapter;
    RecyclerView itemList;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);

        Log.d(TAG, "onCreate: Before read");

        items = new ArrayList<Item>();
/*
        items.add(new Item(1,"Bubbly",true));
        items.add(new Item(2,"Eggs",false));
        items.add(new Item(3,"Yogurt",true));
*/


        ReadFromTextFile();
        //WriteToTextFile();

        this.setTitle("Master List");


    }

    private void ReadFromTextFile()
    {

        FileIO fileIO = new FileIO();

        Integer counter = 0;
        String[] data ;//= new String [items.size()];
        //for(Item t : items) data[counter++] = t.toString();

        //fileIO.writeFile(this, data);


        //Read the data out of the file
        ArrayList<String> strData = fileIO.readFile(this);
        items = new ArrayList<Item>();

        for(String s : strData)
        {
            data = s.split("\\|");
            items.add(new Item(Integer.parseInt(data[0]),data[1],Boolean.parseBoolean(data[2])));
        }
    }

    private void WriteToTextFile() {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String [items.size()];
        for (Item t : items) data[counter++] = t.toString();
        fileIO.writeFile(this,data);
    }

    @Override
    public void onResume() {
        try
        {
            super.onResume();

            itemList = findViewById(R.id.rvItems);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            itemList.setLayoutManager(layoutManager);

            itemAdapter = new ItemAdapter(items, this);
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



}