package com.heetv.shiningfox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.heetv.shiningfox.R;

import java.text.SimpleDateFormat;

public class OrderAdapter extends FirestoreRecyclerAdapter<OrderModel, OrderAdapter.OrderViewHolder> {
    Context context;
    public OrderAdapter(FragmentActivity activity, FirestoreRecyclerOptions<OrderModel> rvOptions, OrderFragment orderFragment) {
        super(rvOptions);
        this.context = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        holder.textView.setText(model.getTotal());
        holder.date.setText(sdf.format(model.getTimestamp()));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetaliActivity.class);
                intent.putExtra("Model", model);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oder_list_view, parent, false);
        return new OrderViewHolder(view);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textView, date;
        LinearLayout linearLayout;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Order_date);
            textView = itemView.findViewById(R.id.Order_total);
            linearLayout = itemView.findViewById(R.id.Order_click);
        }
    }
}
