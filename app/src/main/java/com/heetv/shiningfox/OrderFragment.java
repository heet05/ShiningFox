package com.heetv.shiningfox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.heetv.shiningfox.R;

public class OrderFragment extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences pref;
    OrderAdapter adapter;
    String total="";
    String mobile = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.fragment_order,container,false);
        recyclerView=view.findViewById(R.id.oder_rv);
        pref = getContext().getSharedPreferences("Users", 0);
        mobile = pref.getString("userMobile", "");


        Query query = FirebaseFirestore.getInstance().collection("USERS")
                .document(mobile).collection("ORDERS");
        FirestoreRecyclerOptions<OrderModel> rvOptions=new FirestoreRecyclerOptions.Builder<OrderModel>().setQuery(query,OrderModel.class).build();
//        FirestoreRecyclerOptions<OrderModel> rvOptions = new FirestoreRecyclerOptions.Builder<OrderModel>()
//                .setQuery(query, OrderModel.class).build();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrderAdapter(getActivity(), rvOptions, this);
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
