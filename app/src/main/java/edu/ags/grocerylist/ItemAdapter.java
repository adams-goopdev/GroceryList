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
        Log.d(TAG, "ItemAdapter: " + arrayList.size());
    }

    public void setOnClickListener(View.OnClickListener itemClickListener) {
        onClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Item item = itemData.get(position);

            itemViewHolder.getTextViewName().setText(item.Name);
            itemViewHolder.getCheckBoxAdd().setChecked(item.CheckedState);


        Log.d(TAG, "onBindViewHolder: " + item.Name);
    }

    @Override
    public int getItemCount() {

        return itemData.size();
    }
}


