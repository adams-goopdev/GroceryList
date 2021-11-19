package edu.ags.grocerylist;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroceryLocation extends AppCompatActivity {

    public static final String TAG = "myDebug";
    LocationManager locationManager;
    LocationListener locationListener;
    final int PERMISSION_REQUEST_LOCATION = 1001;
    Item item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_location);


        initLookUpButton();
        initLookupFromLocation();
        initFindButton();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            initTeam(extras.getInt("teamId"));

        }

        this.setTitle("MAP");
    }

    private void initTeam(int itemId) {

        try {
            Log.d(TAG, "initTeam: "+ ShoppingList.VEHICLETRACKERAPI + itemId);
            RestClient.executeGetOneRequest(ShoppingList.VEHICLETRACKERAPI  + itemId, this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {
                            item = result.get(0);
                            RebindItems();
                        }
                    });
        }
        catch (Exception e)
        {
            Log.d(TAG, "initItem: " + e.getMessage());
        }

    }

    private void RebindItems() {

        TextView editName = findViewById(R.id.textView_name);
        TextView textLat = findViewById(R.id.textView_latitude);
        TextView textLong = findViewById(R.id.textView_longitude);
        EditText editCity = findViewById(R.id.editText_city);

        editName.setText(item.getName());
        editCity.setText(item.getCity());

        textLat.setText(String.valueOf(item.getLatitude()));
        textLong.setText(String.valueOf(item.getLongitude()));

    }

    private void initSaveButton() {
        Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item !=null)
                {
                    TextView textViewLat = findViewById(R.id.textView_latitude);
                    TextView textViewLong = findViewById(R.id.textView_longitude);

                    item.setLatitude(Double.parseDouble(textViewLat.getText().toString()));
                    item.setLongitude(Double.parseDouble(textViewLong.getText().toString()));

                    try {
                        saveToAPI();
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onClick: " + e.getMessage());
                    }
                }
            }


        });
    }

    private void saveToAPI() {

        try {

            RestClient.executePutRequest(item,
                    ShoppingList.VEHICLETRACKERAPI + item.getId(),
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {
                            Log.d(TAG, "onSuccess: Put" + result);
                        }
                    });

        }
        catch (Exception e)
        {
            Log.d(TAG, "saveToAPI: " + e.getMessage());
        }


    }

    private void initFindButton() {
        Button button = findViewById(R.id.button_find);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(GroceryLocation.this);
                List<Address> addressList = null;

                EditText editTextAddress = findViewById(R.id.editText_streetAddress);
                EditText editTextCity = findViewById(R.id.editText_city);
                EditText editTextState = findViewById(R.id.editText_state);
                EditText editTextZip = findViewById(R.id.editText_zip);


                String address = editTextAddress.getText().toString() + ", " +
                        editTextCity.getText().toString() + ", " +
                        editTextState.getText().toString() + " " +
                        editTextZip.getText().toString();

                try {
                    addressList = geocoder.getFromLocationName(address, 1);
                    TextView textViewLatitude = findViewById(R.id.textView_latitude);
                    TextView textViewLongitude = findViewById(R.id.textView_longitude);
                    textViewLatitude.setText(String.valueOf(addressList.get(0).getLatitude()));
                    textViewLongitude.setText(String.valueOf(addressList.get(0).getLongitude()));


                }
                catch (IOException e)
                {
                    Log.d(TAG, "GeoCodeError " + e.getMessage());
                }

            }
        });
    }

    private void initLookupFromLocation() {
        Button button = findViewById(R.id.button_fromlocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibility(View.INVISIBLE);

                try {

                    if(Build.VERSION.SDK_INT >= 23)
                    {
                        //Check for the manifest permission
                        if(ContextCompat.checkSelfPermission(GroceryLocation.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
                        {
                            if(ActivityCompat.shouldShowRequestPermissionRationale(GroceryLocation.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                                Snackbar.make(findViewById(R.id.shoppingList), "Teams requires this permission to place a call from the app.",
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ActivityCompat.requestPermissions(GroceryLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                    }
                                }).show();
                            }
                            else
                            {
                                ActivityCompat.requestPermissions(GroceryLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                startLocationUpdates();
                            }
                        }
                        else
                        {
                            //Permission was previously granted
                            startLocationUpdates();
                        }
                    }
                    else{
                        startLocationUpdates();
                    }
                }
                catch (Exception e)
                {

                }

            }
        });
    }

    private void startLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PERMISSION_GRANTED )
        {
            Log.d(TAG, "startLocationUpdates: Permissions Problem");
            return;
        }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d(TAG, "onLocationChanged: " + location.getLatitude() + " : " + location.getLongitude());

                    TextView textViewLatitude = findViewById(R.id.textView_latitude);
                    TextView textViewLongitude = findViewById(R.id.textView_longitude);
                    textViewLatitude.setText(String.valueOf((Math.round(location.getLatitude()*100000.0)/100000.0)));
                    textViewLongitude.setText(String.valueOf((Math.round(location.getLongitude()*100000.0)/100000.0)));

                }

            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
        }
        catch (Exception e)
        {
            Log.d(TAG, "startLocationUpdates: " + e.getMessage());
        }
    }
    private void stopLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PERMISSION_GRANTED )
        {
            Log.d(TAG, "startLocationUpdates: Permissions Problem");
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    private void initLookUpButton() {
        Button button = findViewById(R.id.button_lookup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibility(View.VISIBLE);
            }
        });
    }

    private void changeVisibility(int visible) {
        EditText editText = findViewById(R.id.editText_streetAddress);
        editText.setVisibility(visible);

        editText = findViewById(R.id.editText_city);
        editText.setVisibility(visible);

        editText = findViewById(R.id.editText_state);
        editText.setVisibility(visible);

        editText = findViewById(R.id.editText_zip);
        editText.setVisibility(visible);

        Button button = findViewById(R.id.button_find);
        button.setVisibility(visible);
    }
}