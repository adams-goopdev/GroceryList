package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.spec.ECField;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

public class DeleteItem extends AppCompatActivity {
    public static final String TAG = "myDebug";
    Item item;
    ArrayList<Item> items;
    EditText etItem;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);

        this.setTitle("Edit Item");

        Bundle extras = getIntent().getExtras();


       // ReadFromTextFile();

        Log.d(TAG, "onCreate: We made it from the delete click");
        try{
            if(extras != null) {
                //Edit existing team
                Log.d(TAG, "onCreate: Yippi!" + extras.getInt("itemId"));

                initItem(extras.getInt("itemId"));

            }
            else {

                //Make a new one
                item = new Item();
                Log.d(TAG, "onCreate: new team");
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "onCreate: Womp Womp" + e.getMessage());
        }

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: Made it to the delete single " + extras.getInt("itemId"));

                ItemDataSource ds = new ItemDataSource(DeleteItem.this);

                    ds.open();
                    ds.delete(item.Id);

                Log.d(TAG, "onClick: This item has been removed" + item.Name + extras.getInt("itemId"));
                //WriteToTextFile();


                Intent intent = new Intent(DeleteItem.this, MasterList.class);
                startActivity(intent);

            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ItemDataSource ds = new ItemDataSource(DeleteItem.this);
                etItem = findViewById(R.id.etItem);

                name = etItem.getText().toString();

                item.setName(name);



                ds.open();
                ds.update(item);
               // WriteToTextFile();

                Intent intent = new Intent(DeleteItem.this, MasterList.class);
                startActivity(intent);

            }
        });

    }

    private void initCheck() {
        for (Item t: items)
        {
            item.CheckedState = 1;
            Log.d(TAG, "onResume: " + t.Name +" "+ t.CheckedState);

        }
    }

    private void initUncheck() {

        for (Item t: items)
        {
            item.CheckedState = 0;
            Log.d(TAG, "onResume: " + t.Name +" "+ t.CheckedState);

        }
    }

    private void initItem(int itemId) {

        ItemDataSource ds = new ItemDataSource(this);
        ds.open();
        item = ds.getItem(itemId);

        Log.d(TAG, "initItem: " + item.Name + itemId);

        TextView textView = findViewById(R.id.etItem);
        textView.setText(item.Name);

    }


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
            items.add(new Item(Integer.parseInt(data[0]),data[1],Integer.parseInt(data[2]),Integer.parseInt(data[3])));
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