package edu.ags.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MasterList extends AppCompatActivity {

    public static final String TAG = "myDebug";
    Item item;
    ArrayList<Item> items;

    ItemAdapter itemAdapter;
    RecyclerView itemList;
    TextView textView;
    CheckBox checkBox;
    public static final String VEHICLETRACKERAPI = "https://vehicletrackerapi.azurewebsites.net/api/GroceryList/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);

        items = new ArrayList<Item>();


        this.setTitle("Master List");



    }



    private void saveToAPI(boolean post) {

        android.content.SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String user = preferences.getString("User","") + "/";

        try {
            if(post)
            {
                RestClient.executePostRequest(item,
                        MasterList.VEHICLETRACKERAPI + user,
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Item> result) {
                                Log.d(TAG, "onSuccess: Post" + result);
                            }
                        });
            }
            else
            {
                RestClient.executePutRequest(item,
                        MasterList.VEHICLETRACKERAPI + item.getId() ,
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Item> result) {
                                Log.d(TAG, "onSuccess: Put" + result);
                            }
                        });
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "saveToAPI: " + e.getMessage());
        }


    }




    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int itemID = items.get(position).getId();
            Log.d(TAG, "onClick: " + items.get(position).Name + itemID + " " + position);

            Intent intent = new Intent(MasterList.this, DeleteItem.class);
            intent.putExtra("itemId", itemID);
            startActivity(intent);

            Log.d(TAG, "onClick: Send to delete");
        }
    };


    @Override
    public void onResume() {
        try
        {
            super.onResume();

            android.content.SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

            String user = preferences.getString("User","") + "/";

            try {
                RestClient.executeGetRequest(ShoppingList.VEHICLETRACKERAPI + user, this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Item> result) {
                                for(Item t : result)
                                {
                                    Log.d(TAG, "onSuccess: " + t.getName());
                                }
                                items = result;
                                RebindList();
                            }
                        });
            }
            catch (Exception e)
            {
                Log.d(TAG, "onResume: "+ e.getMessage());
            }

        }
        catch (Exception e)
        {
            Log.d(TAG, "onResume: " + e.getMessage());
        }
    }

    private void RebindList() {
        itemList = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        itemList.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(items, this);
        itemList.setAdapter(itemAdapter);

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
        else if (id == R.id.SetUser)
        {
            startActivity(new Intent(this, SharedPreferences.class));

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