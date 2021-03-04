package com.heet.shiningfox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

public class OrderDetaliActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SharedPreferences pref;
    OrderDetaliAdapter adapter;
    String total = "";
    OrderModel Model;
    String mobile = "";
    List<OrderModel> list;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detali);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.Order_detail_rv);
        textView = findViewById(R.id.Order_detail_total);

        Model = (OrderModel) getIntent().getSerializableExtra("Model");
        pref = getSharedPreferences("Users", 0);

        mobile = pref.getString("userMobile", "");
        textView.setText(Model.getTotal());

        adapter = new OrderDetaliAdapter(this, Model.getModelList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}