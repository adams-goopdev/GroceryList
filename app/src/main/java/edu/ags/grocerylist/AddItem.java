package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

    public static final String TAG = "myDebug";
    Item item;
    ArrayList<Item> items;

    ItemAdapter itemAdapter;
    RecyclerView itemList;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ReadFromTextFile();
        item = new Item();

        initAddItem();
        Log.d(TAG, "onCreate: " + item.Id);
    }


    private void initAddItem( ) {
        Button btnAddItem = findViewById(R.id.btnAddItem);
        EditText editText = findViewById(R.id.etAddItem);
        CheckBox checkBox = findViewById(R.id.cbAddItem);

        Log.d(TAG, "initAddItem: Did I make it here 1 ");
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "initAddItem: Did I make it here 2 ");

                try{
                    if(item.Id == -1)
                    {

                        item.Id = items.get(items.size() -1 ).Id + 1;
                        Log.d(TAG, "onClick: before get text" );
                        item.setName(editText.getText().toString());
                        item.CheckedState = checkBox.isChecked();

                        items.add(item);
                        Log.d(TAG, "onClick: " + item.Name);


                        editText.setText("");
                        checkBox.setChecked(false);
                    }
                    WriteToTextFile();
                    Log.d(TAG, "onClick: Added new item to file");
                }
                catch (Exception e)
                {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }


            }
        });




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