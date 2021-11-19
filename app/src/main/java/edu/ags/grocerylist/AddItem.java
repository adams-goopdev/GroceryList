package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    public static final String VEHICLETRACKERAPI = "https://vehicletrackerapi.azurewebsites.net/api/GroceryList/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        this.setTitle("Add Item");

        initAddItem();
        Log.d(TAG, "onCreate: End of Oncreate ");
    }


    private void initAddItem() {
        Button btnAddItem = findViewById(R.id.btnAddItem);
        EditText editText = findViewById(R.id.etAddItem);
        CheckBox checkBox = findViewById(R.id.cbAddItem);



        android.content.SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String user = preferences.getString("User", "");


        Log.d(TAG, "initAddItem: Did I make it here 1 ");
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "initAddItem: Did I make it here 2 ");

                item = new Item();

                item.setName(editText.getText().toString());
                if(checkBox.isChecked())
                {
                    item.setCheckedState(1);
                }
                else
                {
                    item.setCheckedState(0);
                }


                item.setOwner(user);
                saveToAPI(true);

                editText.setText("");
                checkBox.setChecked(false);

                Log.d(TAG, "save new item to API" + item.getOwner() + " " + item.Name);
            }
        });

    }

    private void saveToAPI(boolean post) {

        try {
            if (post) {
                RestClient.executePostRequest(item,
                        VEHICLETRACKERAPI,
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Item> result) {
                                Log.d(TAG, "onSuccess: Post" + result);
                            }
                        });
            } else {
                RestClient.executePutRequest(item,
                        VEHICLETRACKERAPI + item.getId(),
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Item> result) {
                                Log.d(TAG, "onSuccess: Put" + result);
                            }
                        });
            }
        } catch (Exception e) {
            Log.d(TAG, "saveToAPI: " + e.getMessage());
        }


    }

    @Override
    public void onResume() {
        try {
            super.onResume();

            android.content.SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

            String user = preferences.getString("User", "") + "/";


            try {
                RestClient.executeGetIsOnListRequest(ShoppingList.VEHICLETRACKERAPI + user, this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Item> result) {
                                for (Item t : result) {
                                    Log.d(TAG, "onSuccess: " + VEHICLETRACKERAPI);
                                    Log.d(TAG, "onSuccess: " + t.getName());
                                }
                                items = result;
                            }
                        });
            } catch (Exception e) {
                Log.d(TAG, "onResume: " + e.getMessage());
            }

        } catch (Exception e) {
            Log.d(TAG, "onResume: " + e.getMessage());
        }

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
        } else if (id == R.id.ShoppingList) {
            startActivity(new Intent(this, ShoppingList.class));

            return true;
        } else if (id == R.id.AddItem) {
            startActivity(new Intent(this, AddItem.class));

            return true;
        } else if (id == R.id.SetUser) {
            startActivity(new Intent(this, SharedPreferences.class));

            return true;
        }
        else if (id == R.id.Location)
        {
            startActivity(new Intent(this, GroceryLocation.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}