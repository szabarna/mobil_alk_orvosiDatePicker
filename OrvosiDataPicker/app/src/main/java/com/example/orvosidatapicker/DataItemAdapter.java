package com.example.orvosidatapicker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<DataItem> mDataItemsData;
    private ArrayList<DataItem> mDataItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    DataItemAdapter(Context context, ArrayList<DataItem> dataItems) {
        this.mDataItemsData = dataItems;
        this.mDataItemsDataAll = dataItems;
        this.mContext = context;
    }

                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
                }

                @Override
                public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
                    DataItem currentItem = mDataItemsData.get(position);

                    holder.bindTo(currentItem);

                    if(holder.getAbsoluteAdapterPosition() > lastPosition) {
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
                        holder.itemView.startAnimation(animation);
                        lastPosition = holder.getAbsoluteAdapterPosition();
                    }
                }

                @Override
                public int getItemCount() {   return mDataItemsData.size();      }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<DataItem> filteredItems = new ArrayList<>();
            FilterResults results = new FilterResults();


            if(charSequence == null || charSequence.length() == 0) {
                results.count = mDataItemsDataAll.size();
                results.values = mDataItemsDataAll;
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                    for(DataItem dataItem : mDataItemsDataAll) {
                        if(dataItem.getName().toLowerCase().contains(filterPattern)) {
                            filteredItems.add(dataItem);
                        }
                    }

                    results.count = filteredItems.size();
                    results.values = filteredItems;
                 }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mDataItemsData = (ArrayList) results.values;
            notifyDataSetChanged();

        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mItemJobType;
            private TextView mItemName;
            private RatingBar mItemRating;

           public ViewHolder(@NonNull View itemView) {
               super(itemView);

               mItemJobType = itemView.findViewById(R.id.itemJobType);
               mItemName = itemView.findViewById(R.id.itemName);

               mItemRating = itemView.findViewById(R.id.itemRating);

               itemView.findViewById(R.id.itemButton).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Log.i("Activity", "New Appointment added!");
                       ((DatePickerActivity)mContext).updateAlertIcon();

                   }
               });

              }

               public void bindTo(DataItem currentItem) {
                   mItemJobType.setText(currentItem.getType());
                   mItemName.setText(currentItem.getName());
                   mItemRating.setRating(currentItem.getRating());




               }
           };
};

