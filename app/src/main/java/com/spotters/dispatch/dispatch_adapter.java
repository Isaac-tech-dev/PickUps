package com.spotters.dispatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class dispatch_adapter extends RecyclerView.Adapter<dispatch_adapter.AdapterViewHolder>{

    private List<show1> driverlist;
    private ItemClickListener mItemListener;

    public dispatch_adapter(List<show1> driverlist, ItemClickListener itemClickListener){
        this.driverlist = driverlist;
        this.mItemListener = itemClickListener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dispatch_design, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dispatch_adapter.AdapterViewHolder holder, int position) {
        holder.name.setText(driverlist.get(position).getName());
        //holder.l_name.setText(driverlist.get(position).getLastname());
        holder.amount.setText(driverlist.get(position).getAmount());
        holder.pickup.setText(driverlist.get(position).getPickup());
        holder.destination.setText(driverlist.get(position).getDestination());

        holder.Btn.setOnClickListener(view -> {
            mItemListener.onItemClick(driverlist.get(position)); //this would get the position of our item in the recycler view
        });
    }

    @Override
    public int getItemCount() {
        return driverlist.size();
    }

    public interface ItemClickListener{
        void onItemClick(show1 show1);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name, l_name,pickup,destination, amount;
        Button Btn;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rider);
            //l_name = itemView.findViewById(R.id.lastname);
            pickup = itemView.findViewById(R.id.pickup);
            destination = itemView.findViewById(R.id.dropoff);
            amount = itemView.findViewById(R.id.amount);
            Btn = itemView.findViewById(R.id.select);
        }
    }
}
