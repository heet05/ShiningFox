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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.heetv.shiningfox.R;

public class Check_out_Adapter extends FirestoreRecyclerAdapter<CheckModel,Check_out_Adapter.CheckViewHolder> {
    Context context;
    Check_Out_Activity check_out_activity;
    public Check_out_Adapter(Check_Out_Activity check_out_activity, FirestoreRecyclerOptions<CheckModel> rvOptions, Check_Out_Activity check_out_activity1) {
        super(rvOptions);
        this.context=context;
        this.check_out_activity=check_out_activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckViewHolder holder, int position, @NonNull CheckModel model) {
        holder.textView1.setText(model.getTitle());
        Glide.with(check_out_activity).load(model.getImage()).into(holder.imageView);
        holder.textView2.setText(" ₹ " + model.getTotal());
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item1,parent,false);
        return new CheckViewHolder(view);
    }

    public class CheckViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2;
        public CheckViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.check_image);
            textView1=itemView.findViewById(R.id.check_name);
            textView2=itemView.findViewById(R.id.Check_price);
        }
    }
}
