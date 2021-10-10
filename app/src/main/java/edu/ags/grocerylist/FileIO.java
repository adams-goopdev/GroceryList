package edu.ags.grocerylist;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileIO {
    public static final String FILENAME = "data.txt";
    public static final String TAG = "FileIO";


    public void writeFile(AppCompatActivity mainActivity, String[] items) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(mainActivity.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            String row = "";

            for (Integer counter = 0; counter < items.length; counter++) {
                row = items[counter];
                if(counter < items.length -1)
                    row += "\r\n";
                writer.write(row);
                Log.d(TAG, "writeFile: " + row);
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<String> readFile(AppCompatActivity mainActivity)
    {

        ArrayList<String> items = new ArrayList<String>();

        try{

            Log.d(TAG, "readFile: ");
            InputStream is = mainActivity.openFileInput(FILENAME);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                items.add(line);
                Log.d(TAG, "readFile: asdfasdf");
            }
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.d(TAG, "readFile: " + e.getMessage());
        }
        Log.d(TAG, "readFile: " + items.size());
        return items;

    }
}
