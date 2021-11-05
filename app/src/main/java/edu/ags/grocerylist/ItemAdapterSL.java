package edu.ags.grocerylist;

import android.content.Context;
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

public class ItemAdapterSL extends RecyclerView.Adapter {

    private static final String TAG = "myDebug";

    private ArrayList<Item> itemData;
    private Context parentContext;


    public class ItemViewHolderSL extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private CheckBox checkBoxCart;

        public ItemViewHolderSL(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.txtName);
            checkBoxCart = itemView.findViewById(R.id.cbInCart);

        }

        public TextView getTextViewName() { return textViewName; }
        public CheckBox getCheckBoxCart() { return checkBoxCart; }

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



    if (item.CheckedState == 1) {
        itemViewHolder.getTextViewName().setText(item.getName());
       // itemViewHolder.checkBoxCart.setVisibility(View.VISIBLE);
        Log.d(TAG, "onBindViewHolder: " + item.CheckedState);
        if(item.IsInCart == 1){
            itemViewHolder.getCheckBoxCart().setChecked(true);

        }
        else
        {
            //itemViewHolder.getTextViewName().setText("");
           // itemViewHolder.checkBoxCart.setVisibility(View.INVISIBLE);
        }
    }


        itemViewHolder.getCheckBoxCart().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               // ItemDataSource ds = new ItemDataSource(parentContext);
               // ds.open();

                if(b == true)
                item.setIsInCart(1);

                if(b == false)
                    item.setIsInCart(0);
                //ds.update(item);

            }
        });

    }

    @Override
    public int getItemCount() {

        return itemData.size();
    }





}


