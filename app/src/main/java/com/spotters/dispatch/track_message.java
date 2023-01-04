package com.spotters.dispatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class track_message extends RecyclerView.Adapter<track_message.AdapterViewHolder> {

    private List<track_show> messagelist;

    public track_message(List<track_show> messagelist, ItemClickListener mItemListener) {
        this.messagelist = messagelist;
        this.mItemListener = mItemListener;
    }

    private ItemClickListener mItemListener;


    @NonNull
    @Override
    public track_message.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_design2, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull track_message.AdapterViewHolder holder, int position) {
        holder.RNA.setText(messagelist.get(position).getRider_name());
        holder.RNU.setText(messagelist.get(position).getRider_phone());
        holder.ORDER.setText(messagelist.get(position).getReference());
        holder.SAD.setText(messagelist.get(position).getSender_address());
        holder.RAD.setText(messagelist.get(position).getReceiver_address());
        holder.AM.setText(messagelist.get(position).getAmount());

        holder.RNA.setVisibility(View.VISIBLE);
        holder.RNU.setVisibility(View.VISIBLE);
        holder.CALL.setVisibility(View.VISIBLE);

        //holder.gif1.setVisibility(View.GONE);
        //holder.gif2.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(messagelist.get(position)); //this would get the position of our item in the recycler view
        });

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public interface ItemClickListener{
        void onItemClick(track_show show2);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView RNA, RNU, ORDER, SAD, RAD, AM;
        ImageView CALL,gif2;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            RNA = itemView.findViewById(R.id.rider_name);
            RNU = itemView.findViewById(R.id.rider_number);
            ORDER = itemView.findViewById(R.id.ID);
            SAD = itemView.findViewById(R.id.sender_address);
            RAD = itemView.findViewById(R.id.receiver_address);
            AM = itemView.findViewById(R.id.price);
            CALL = itemView.findViewById(R.id.call);

            //gif1 = itemView.findViewById(R.id.gif1);
            //gif2 = itemView.findViewById(R.id.gif2);

        }
    }
}
