package com.heet.shiningfox;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.Context.MODE_PRIVATE;

public class CartAdapter  extends FirestoreRecyclerAdapter<CartModel,CartAdapter.ViewHolder> {
    FragmentActivity cartFragment;
    OnQtyUpdate qty;
    int cartTotal = 0;
    SharedPreferences preferences;
    Context context;
    String mobile = "";
    public CartAdapter(FragmentActivity activity, FirestoreRecyclerOptions<CartModel> rvOptions,  OnQtyUpdate qty) {
        super(rvOptions);
        this.context=activity;
        this.qty = qty;
        preferences = context.getSharedPreferences("Users", 0);
        mobile = preferences.getString("userMobile", "");
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CartModel model) {
        holder.cart_name.setText(model.getTitle());
        holder.cart_price.setText(" ₹ " + model.getPrice());
      //  preferences=cartFragment.getSharedPreferences("Users", Context.MODE_PRIVATE);

        // holder.product_kg_gm.setText(" Per " + list.get(position).getProduct_kg_gm());
            holder.size.setText(model.getSize());
     //   holder.cart_total_price.setText(" ₹ " + (Integer.parseInt(model.getQty()) *Integer.parseInt(model.getPrice())));
        //  holder.cart_total_price.setText(model.getTotal());
        holder.cart_number.setText(model.getQty());
        holder.cart_total_price.setText("₹" + (Integer.parseInt(model.getQty()) * Integer.parseInt(model.getPrice()) ) );
        Glide.with(context).load(model.getImage()).into(holder.cart_image);

        holder.cart_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cart_update.setVisibility(View.GONE);
                holder.cart_remove.setVisibility(View.GONE);
                holder.cart_number.setVisibility(View.GONE);
                holder.cart_add.setVisibility(View.GONE);
                holder.cart_edit.setVisibility(View.VISIBLE);
                qty.getQty(holder.cart_number.getText().toString(),model);
                notifyDataSetChanged();
            }
        });
        holder.cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("USERS").document(mobile)
                        .collection("USERCART")
                        .whereEqualTo("cartItemId",model.getCartItemId())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value!=null && !value.isEmpty()){
                                    value.getDocuments().get(0).getReference().delete();
                                }
                            }
                        });
            }
        });

        holder.cart_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cart_edit.setVisibility(View.GONE);
                holder.cart_remove.setVisibility(View.VISIBLE);
                holder.cart_number.setVisibility(View.VISIBLE);
                holder.cart_add.setVisibility(View.VISIBLE);
                holder.cart_update.setVisibility(View.VISIBLE);
            }
        });

        holder.cart_remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(String.valueOf(holder.cart_number.getText()));

                if (count > 1) {
                    count--;
                    holder.cart_number.setText(String.valueOf(count));
                    holder.cart_total_price.setText(" ₹ " + (count * Integer.parseInt(model.getPrice())));
                }
            }
        });

        holder.cart_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(String.valueOf(holder.cart_number.getText()));

                count++;
                holder.cart_number.setText(String.valueOf(count));
                holder.cart_total_price.setText(" ₹ " + (count * Integer.parseInt(model.getPrice())));
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_view, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cart_name, cart_price, cart_kg_gm, cart_total, cart_total_price, cart_number,size;
        ImageView cart_image;
        ImageButton cart_remove, cart_add, cart_edit, cart_delete,cart_update;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_total = itemView.findViewById(R.id.cart_total);
            cart_total_price = itemView.findViewById(R.id.cart_total_price);
            cart_image = itemView.findViewById(R.id.cart_image);
            cart_remove = itemView.findViewById(R.id.cart_remove);
            cart_add = itemView.findViewById(R.id.cart_add);
            cart_number = itemView.findViewById(R.id.cart_number);
            cart_edit = itemView.findViewById(R.id.cart_edit);
            cart_delete = itemView.findViewById(R.id.cart_delete);
            cart_update = itemView.findViewById(R.id.cart_update);
            size=itemView.findViewById(R.id.cart_size1);
        }
    }
}
