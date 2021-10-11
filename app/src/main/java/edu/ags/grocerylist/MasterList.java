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
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);

        Log.d(TAG, "onCreate: Before read");

        items = new ArrayList<Item>();

/*
      items.add(new Item(0,"Bubbly",true));
        items.add(new Item(1,"Eggs",false));
        items.add(new Item(2,"Yogurt",true));
*/



     ReadFromTextFile();
       //WriteToTextFile();

        this.setTitle("Master List");
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int itemID = items.get(position).getId();
            Log.d(TAG, "onClick: asdfasdfasdfasdfasddfasdf" + items.get(position).Name + itemID + " " + position);

            Intent intent = new Intent(MasterList.this, DeleteItem.class);
            intent.putExtra("itemId", itemID);
            startActivity(intent);

            Log.d(TAG, "onClick: Send to delete");
        }
    };

    private void ReadFromTextFile() {

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
            itemAdapter.setOnClickListener(onClickListener);
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

        ReadFromTextFile();
    }

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


}