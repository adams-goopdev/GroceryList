package edu.ags.grocerylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RestClient {

    private static final String TAG = "RestClient";

    public static void executeGetRequest(String url, Context context, VolleyCallback volleyCallback) {

        Log.d(TAG, "executeGetRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Item> items = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            //Process the JSON into an ArrayList<Team>
                            try {
                                JSONArray objects = new JSONArray(response);

                                for (int i = 0; i < objects.length(); i++) {
                                    JSONObject object = objects.getJSONObject(i);
                                    Item item = new Item();
                                    item.setId(object.getInt("id"));
                                    item.setName(object.getString("item"));
                                    item.setCheckedState(object.getInt("isOnShoppingList"));
                                    item.setIsInCart(object.getInt("isInCart"));


                                   items.add(item);

                                }
                                volleyCallback.onSuccess(items);
                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }
            });

            //THE MOST IMPORTANT LINE HERE.
            queue.add(stringRequest);

        } catch (Exception e) {
            Log.d(TAG, "executeGetRequest: " + e.getMessage());
        }

    }

    public static void executeGetIsOnListRequest(String url, Context context, VolleyCallback volleyCallback) {

        Log.d(TAG, "executeGetRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Item> items = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            //Process the JSON into an ArrayList<Team>
                            try {
                                JSONArray objects = new JSONArray(response);

                                for (int i = 0; i < objects.length(); i++) {
                                    JSONObject object = objects.getJSONObject(i);
                                    Item item = new Item();
                                    item.setId(object.getInt("id"));
                                    item.setName(object.getString("item"));
                                    item.setCheckedState(object.getInt("isOnShoppingList"));
                                    item.setIsInCart(object.getInt("isInCart"));


                                    if(item.CheckedState == 1) {
                                        items.add(item);
                                    }

                                }
                                volleyCallback.onSuccess(items);
                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }
            });

            //THE MOST IMPORTANT LINE HERE.
            queue.add(stringRequest);

        } catch (Exception e) {
            Log.d(TAG, "executeGetRequest: " + e.getMessage());
        }

    }

    public static void executeGetOneRequest(String url, Context context, VolleyCallback volleyCallback) {

        Log.d(TAG, "executeGetRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Item> items = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            //Process the JSON into an ArrayList<Team>
                            try {
                                JSONObject object = new JSONObject(response);

                                Item item = new Item();
                                item.setId(object.getInt("id"));
                                item.setName(object.getString("item"));
                                item.setCheckedState(object.getInt("isOnShoppingList"));
                                item.setIsInCart(object.getInt("isInCart"));

                                    items.add(item);


                                volleyCallback.onSuccess(items);
                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }
            });

            //THE MOST IMPORTANT LINE HERE.
            queue.add(stringRequest);

        } catch (Exception e) {
            Log.d(TAG, "executeGetRequest: " + e.getMessage());
        }

    }

    public static void executePostRequest(Item item, String url, Context context, VolleyCallback volleyCallback) {
        try {
            executeRequest(item, url, context, volleyCallback, Request.Method.POST);

        } catch (Exception e) {
            Log.d(TAG, "executePostRequest: " + e.getMessage());
        }

    }

    public static void executePutRequest(Item item, String url, Context context, VolleyCallback volleyCallback) {
        try {
            executeRequest(item, url, context, volleyCallback, Request.Method.PUT);

        } catch (Exception e) {
            Log.d(TAG, "executePostRequest: " + e.getMessage());
        }

    }

    public static void executeDeleteRequest(Item item, String url, Context context, VolleyCallback volleyCallback) {
        try {
            executeRequest(item, url, context, volleyCallback, Request.Method.DELETE);

        } catch (Exception e) {
            Log.d(TAG, "executePostRequest: " + e.getMessage());
        }

    }

    private static void executeRequest(Item item, String url, Context context, VolleyCallback volleyCallback, int method) {
        Log.d(TAG, "executeRequest: " + url);
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", item.getId());
            jsonObject.put("item", item.getName());
            jsonObject.put("isInCart", item.getIsInCart());
            jsonObject.put("isOnShoppingList", item.getCheckedState());


            final String requestBody = jsonObject.toString();
            Log.d(TAG, "executeRequest: " + requestBody);


            JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                public byte[] getBody() {
                    Log.i(TAG, "getBody: " + jsonObject.toString());
                    return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                }

            };
            //Important line
            queue.add(request);

        } catch (Exception e) {
            Log.d(TAG, "executeRequest: " + e.getMessage());
        }
    }


}
