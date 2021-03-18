package com.heetv.shiningfox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heetv.shiningfox.R;

import java.util.List;

public class OrderDetaliAdapter extends RecyclerView.Adapter<OrderDetaliAdapter.ViewHolder> {
    List<CheckModel> list;
    Context context;

    public OrderDetaliAdapter(OrderDetaliActivity orderDetaliActivity, List<CheckModel> modelList) {
        this.list = modelList;
        this.context = orderDetaliActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_main_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.total.setText(list.get(position).getTotal());
        Glide.with(context).load(list.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,total;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Order_Mdetail_title);
            total = itemView.findViewById(R.id.Order_Mdetail_total);
            imageView = itemView.findViewById(R.id.Order_Mdetail_image);
        }
    }
}
