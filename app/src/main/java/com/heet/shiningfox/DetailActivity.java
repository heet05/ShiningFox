package com.heet.shiningfox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.heet.shiningfox.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    Spinner spinner,spinner1;
    ImageView imageView;
    TextView title;
    TextView price;
    TextView dec;
    String pId;
    Button button;
detailModel detailModel;

    String[] size = new String[]{"S", "M", "L", "Xl"};
    String[] Qty = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
String mobile="";
ProModel proModel;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spinner = findViewById(R.id.spinner);
        spinner1 = findViewById(R.id.spinner1);
        imageView = findViewById(R.id.image_view);
        title = findViewById(R.id.title1);
        price = findViewById(R.id.price1);
        dec = findViewById(R.id.dec);
        button = findViewById(R.id.btn);
        pId = getIntent().getStringExtra("pid");
        preferences = getSharedPreferences("Users", 0);
        mobile = preferences.getString("userMobile", "");
        List<String> Sizelist = new ArrayList<>(Arrays.asList(size));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Sizelist);
        spinner.setAdapter(spinnerArrayAdapter);
        List<String> Qtylist = new ArrayList<>(Arrays.asList(Qty));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Qtylist);
        spinner1.setAdapter(arrayAdapter);
        FirebaseFirestore.getInstance().collection("product").whereEqualTo("pid", pId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    ProModel proModel = value.getDocuments().get(0).toObject(ProModel.class);
                    title.setText(proModel.getTitle());
                    Glide.with(DetailActivity.this).load(proModel.getImage()).into(imageView);
                    price.setText(proModel.getPrice());
                    dec.setText(proModel.getDec());

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailModel Model=new detailModel();
                AddToCart(Model);
            }
        });

    }

    private void AddToCart(com.heet.shiningfox.detailModel detailModel) {
        CartModel cartModel = new CartModel();
        cartModel.setTitle(detailModel.getTitle());
        cartModel.setImage(detailModel.getImage());
        cartModel.setQty(spinner1.getSelectedItem().toString());
        //   cartModel.setPid(proModel.getPid());
        cartModel.setDec(detailModel.getDec());

        cartModel.setPrice(detailModel.getPrice());
        cartModel.setSize(spinner.getSelectedItem().toString());
        cartModel.setTotal(String.valueOf(Integer.parseInt(price.getText().toString()) * Integer.parseInt(spinner1.getSelectedItem().toString())));
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
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}
