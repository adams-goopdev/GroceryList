package edu.ags.grocerylist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "myItemAdapter";

    private ArrayList<Item> itemData;
    private Context parentContext;
    private View.OnClickListener onClickListener;


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private CheckBox checkBoxAdd;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.txtName);
            checkBoxAdd = itemView.findViewById(R.id.cbML);

            Log.d(TAG, "ItemViewHolder: ");

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);


        }

        public TextView getTextViewName() {
            return textViewName;
        }
        public CheckBox getCheckBoxAdd() {
            return checkBoxAdd;
        }

    }


    public ItemAdapter(ArrayList<Item> arrayList, Context context) {
        itemData = arrayList;
        parentContext = context;
        Log.d(TAG, "ItemAdapter: " + arrayList.size() + context);
    }

    public void setOnClickListener(View.OnClickListener itemClickListener) {
        onClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: " + parent.getContext());
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Item item = itemData.get(position);

            itemViewHolder.getTextViewName().setText(item.Name);

            if(item.CheckedState == 1)
            itemViewHolder.getCheckBoxAdd().setChecked(true);

            //Log.d(TAG, "onBindViewHolder: " + item.Name);

        itemViewHolder.getCheckBoxAdd().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ItemDataSource ds = new ItemDataSource(parentContext);
               ds.open();

                Log.d(TAG, "onCheckedChanged: Checkbox is checked" + item.getId());

                if (b == true)
                    item.setCheckedState(1);
                else
                    item.setCheckedState(0);


                ds.update(item);



               //WriteToTextFile();

            }
        });
    }

    private void WriteToTextFile() {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String [itemData.size()];
        for (Item t : itemData) data[counter++] = t.toString();
        fileIO.writeFile((AppCompatActivity) parentContext,data);
    }

    @Override
    public int getItemCount() {

        return itemData.size();
    }


    private void deleteItem(int position, int id) {
        // Remove it from the teamData
        itemData.remove(position);

        // Write the file.
        // WriteToTextFile();

        ItemDataSource ds = new ItemDataSource(parentContext);
        try{
            ds.open();
            boolean result = ds.delete(id);
            Log.d(TAG, "deleteItem: Delete Team: " + id);
        }
        catch(Exception ex)
        {
            Log.d(TAG, "deleteItem: " + ex.getMessage());
        }
        // Rebind
        notifyDataSetChanged();
    }


}


