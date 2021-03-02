package com.heet.shiningfox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailAdapter extends FirestoreRecyclerAdapter<ProModel, DetailAdapter.ViewHolder> {

Context context;

FirestoreRecyclerOptions<ProModel> rvOptions;
OnAddToCartListner listner;
    String[] Qty = new String[]{"1","2","3","4","5","6","7","8","9","10"};
    String[]size=new String[]{"S","M","L","Xl"};
    public DetailAdapter(DetailActivity detailActivity, FirestoreRecyclerOptions<ProModel> rvOptions,OnAddToCartListner listner) {
        super(rvOptions);

        this.context =detailActivity;
        this.rvOptions = rvOptions;
        this.listner=listner;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ProModel model) {
        List<String> Sizelist = new ArrayList<>(Arrays.asList(size));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, Sizelist);
        holder.spinner.setAdapter(spinnerArrayAdapter);
        List<String> Qtylist = new ArrayList<>(Arrays.asList(Qty));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, Qtylist);
       holder.spinner1.setAdapter(arrayAdapter);

       holder.title.setText(model.getTitle());
       holder.dec.setText(model.getDec());
       holder.price.setText(model.getPrice());
        Glide.with(context).load(model.getImage()).into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.AddToCart(model,holder.spinner.getSelectedItem(),holder.spinner1.getSelectedItem());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.detail_list,parent,false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView title,dec,price;
      ImageView imageView;
      Spinner spinner,spinner1;
      Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title1);
            dec=itemView.findViewById(R.id.dec);
            price=itemView.findViewById(R.id.price1);
            spinner=itemView.findViewById(R.id.spinner);
            spinner1=itemView.findViewById(R.id.spinner1);
            button=itemView.findViewById(R.id.btn1);
            imageView=itemView.findViewById(R.id.image_view);

        }
    }
}
