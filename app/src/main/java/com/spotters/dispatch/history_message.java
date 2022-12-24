package com.spotters.dispatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class history_message extends RecyclerView.Adapter<history_message.AdapterViewHolder> {

    private List<history_show> messagelist;

    public history_message(List<history_show> messagelist, ItemClickListener mItemListener) {
        this.messagelist = messagelist;
        this.mItemListener = mItemListener;
    }

    private ItemClickListener mItemListener;

    @NonNull
    @Override
    public history_message.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design, parent, false);
        return new AdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull history_message.AdapterViewHolder holder, int position) {
        holder.RNA.setText(messagelist.get(position).getPickup_location());
        holder.RNU.setText(messagelist.get(position).getDestination());
        holder.ORDER.setText(messagelist.get(position).getReference());

        holder.RNA.setVisibility(View.VISIBLE);
        holder.RNU.setVisibility(View.VISIBLE);


        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(messagelist.get(position)); //this would get the position of our item in the recycler view
        });

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public interface ItemClickListener{
        void onItemClick(history_show history_show);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView RNA, RNU, ORDER;
        ImageView gif1,gif2;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            RNA = itemView.findViewById(R.id.name);
            RNU = itemView.findViewById(R.id.number);
            ORDER = itemView.findViewById(R.id.ID);
        }
    }
}
