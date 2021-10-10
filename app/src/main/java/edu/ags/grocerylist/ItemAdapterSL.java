package edu.ags.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapterSL extends RecyclerView.Adapter {

    private static final String TAG = "myItemAdapter";

    private ArrayList<Item> itemData;
    private Context parentContext;


    public class ItemViewHolderSL extends RecyclerView.ViewHolder {

        private TextView textViewName;


        public ItemViewHolderSL(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.txtName);


            Log.d(TAG, "ItemViewHolder: ");

        }

        public TextView getTextViewName() { return textViewName; }

    }

    public ItemAdapterSL(ArrayList<Item> arrayList, Context context) {
        itemData = arrayList;
        parentContext = context;
        Log.d(TAG, "ItemAdapter: " + arrayList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_itemsl,parent,false);
        return new ItemViewHolderSL(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolderSL itemViewHolder = (ItemViewHolderSL) holder;
        Item item = itemData.get(position);

        itemViewHolder.getTextViewName().setText(item.Name);


        Log.d(TAG, "onBindViewHolder: " + item.Name);
    }

    @Override
    public int getItemCount() {

        return itemData.size();
    }
}


