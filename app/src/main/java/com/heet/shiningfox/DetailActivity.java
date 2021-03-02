package com.heet.shiningfox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements OnAddToCartListner {
RecyclerView recyclerView;

DetailModel detailModel;
    DetailAdapter detailAdapter;

List<ProModel>list=new ArrayList<>();

String mobile="";

    SharedPreferences preferences;
    private Object OnAddToCartListner;
ProModel proModel;
String pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pId= getIntent().getStringExtra("pid");
        preferences = getSharedPreferences("Users", 0);
        mobile = preferences.getString("userMobile", "");
        recyclerView=findViewById(R.id.rv);
        Query query=FirebaseFirestore.getInstance().collection("product").whereEqualTo("pid",pId);


        FirestoreRecyclerOptions rvOption = new FirestoreRecyclerOptions.Builder<ProModel>()
                .setQuery(query,ProModel.class).build();


//        FirestoreRecyclerOptions<> rvOptions = new FirestoreRecyclerOptions.Builder<OrderModel>()
//                .setQuery(query, OrderModel.class).build();

        LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(this);
       detailAdapter=new DetailAdapter(this,rvOption,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detailAdapter);

    }



        


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

    @Override
    public void AddToCart(ProModel model) {
        CartModel cartModel = new CartModel();
        cartModel.setTitle(model.getTitle());
        cartModel.setImage(model.getImage());
        cartModel.setQty(model.getQty());
        //   cartModel.setPid(proModel.getPid());
        cartModel.setDec(model.getDec());

        cartModel.setPrice(model.getPrice());
        cartModel.setSize(model.getSize());
        cartModel.setTotal(String.valueOf(Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQty())));
        FirebaseFirestore.getInstance().collection("USERS")
                .document(mobile)
                .collection("USERCART")
                .add(cartModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Map<String, Object> map = new HashMap<>();
                map.put("cartItemId", documentReference.getId());
                FirebaseFirestore.getInstance().collection("USERS")
                        .document(mobile)
                        .collection("USERCART").document(documentReference.getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
                Toast.makeText(DetailActivity.this, "Added to cart.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        detailAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        detailAdapter.stopListening();
    }
}
