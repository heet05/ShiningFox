package com.heetv.shiningfox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.heetv.shiningfox.R;

import java.util.List;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
Context context;
List<ProModel>modelList;
FirebaseFirestore firestore;


    public ProductAdapter(FragmentActivity activity, List<ProModel> proModels, FirebaseFirestore firestore) {
    this.context=activity;
    this.firestore=firestore;
    this.modelList=proModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(modelList.get(position).getTitle());
        holder.price.setText(modelList.get(position).getPrice());
        Glide.with(context).load(modelList.get(position).getImage()).into(holder.imageView);
        holder.dec.setText(modelList.get(position).getDec());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailActivity.class);
                intent.putExtra("pid",modelList.get(position).getPid());
                context.startActivities(new Intent[]{intent});


            }
        });
    }



    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,dec,price;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title=itemView.findViewById(R.id.item_title);
            dec=itemView.findViewById(R.id.item_dec);
            price=itemView.findViewById(R.id.item_Price);
            imageView=itemView.findViewById(R.id.item_image);
            cardView=itemView.findViewById(R.id.card_view1);
        }
    }
}
