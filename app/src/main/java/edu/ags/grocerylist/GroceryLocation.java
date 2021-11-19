package edu.ags.grocerylist;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroceryLocation extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "myDebug";
    LocationManager locationManager;
    LocationListener locationListener;
    final int PERMISSION_REQUEST_LOCATION = 1001;
    private static final int PERMISSION_REQUEST_PHONE = 102;
    Item item;
    GoogleMap gMap;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    SupportMapFragment mapFragment;



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
            initItem(extras.getInt("itemId"));

        }

        this.setTitle("MAP");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initItem(int itemId) {

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

        mapFragment.getMapAsync( this);


    }

    private void initSaveButton() {
        Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item !=null)
                {
                    Log.d(TAG, "onClick: Testing out save button");
                    TextView textViewLat = findViewById(R.id.textView_latitude);
                    TextView textViewLong = findViewById(R.id.textView_longitude);

                    item.setLatitude(Double.parseDouble(textViewLat.getText().toString()));
                    item.setLongitude(Double.parseDouble(textViewLong.getText().toString()));

                    try {
                        saveToAPI();
                        RebindItems();

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
                    Log.d(TAG, "onClick: ");

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


    public void onMapReady(GoogleMap googleMap) {
        try {
            Log.d(TAG, "onMapReady: Start of Map");

            gMap = googleMap;
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            Point size = new Point();
            WindowManager windowManager = getWindowManager();
            windowManager.getDefaultDisplay().getSize(size);
            int measuredWidth = size.x;
            int measuredHeight = size.y;


            if (item != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                LatLng point = new LatLng(item.getLatitude(), item.getLongitude());
                builder.include(point);

                gMap.addMarker(new MarkerOptions().position(point)
                        .title(item.getName())
                        .snippet(item.getCity() + ": " ));

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15f));
            } else {
                Log.d(TAG, "onMapReady: Team is null");
            }

        } catch (Exception e) {
            Log.d(TAG, "onMapReady: " + e.getMessage());
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
        else if (id == R.id.Location)
        {
            startActivity(new Intent(this, GroceryLocation.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}