package edu.ags.grocerylist;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "myItemAdapter";
    public static final String VEHICLETRACKERAPI = "https://vehicletrackerapi.azurewebsites.net/api/GroceryList/";
    Item item;
    private ArrayList<Item> itemData;
    private Context parentContext;
    private View.OnClickListener onClickListener;


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private CheckBox checkBoxAdd;
        private Button btnDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.txtName);
            checkBoxAdd = itemView.findViewById(R.id.cbML);
            btnDelete = itemView.findViewById(R.id.btnDelete);

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
        public Button getBtnDelete(){return btnDelete;}

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

            itemViewHolder.getTextViewName().setText(item.getName());

            if(item.CheckedState == 1)
            itemViewHolder.getCheckBoxAdd().setChecked(true);

            //Log.d(TAG, "onBindViewHolder: " + item.Name);

        itemViewHolder.getCheckBoxAdd().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                Log.d(TAG, "onCheckedChanged: Checkbox is checked " + item.getId());

                if (b == true)
                    item.setCheckedState(1);
                else
                    item.setCheckedState(0);

                Log.d(TAG, "onCheckedChanged: Item is " + item.getName() + " " + item.getId() + " " + item.getCheckedState());


                try {

                        RestClient.executePutRequest(item,
                                VEHICLETRACKERAPI + item.getId(),
                                parentContext,
                                new VolleyCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<Item> result) {
                                        Log.d(TAG, "onSuccess: Post" + result);
                                    }
                                });

                } catch (Exception e) {
                    Log.d(TAG, "saveToAPI: " + e.getMessage());
                }

            }
        });


        itemViewHolder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.content.SharedPreferences preferences = parentContext.getSharedPreferences("MyPrefs", MODE_PRIVATE);

                String user = preferences.getString("User","") + "/";


                RestClient.executeDeleteRequest(item, VEHICLETRACKERAPI + item.getId(), parentContext, new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Item> result) {

                        Log.d(TAG, "Delete Items");
                    }
                });

                itemData.remove(position);
                notifyDataSetChanged();
            }




        });



    }


    @Override
    public int getItemCount() {

        return itemData.size();
    }




}


