package com.example.datainsertproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Order_page extends AppCompatActivity implements ItemClickOrderListener {

    private ArrayList<OrderModel> orderModelArrayList;
    private RecyclerView recyclerView;
    private OrderAdapter OrderAdapter;
     String userId;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        initview();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        orderModelArrayList = new ArrayList<OrderModel>();

        OrderAdapter = new OrderAdapter(orderModelArrayList, this, Order_page.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(OrderAdapter);

        Intent intent = getIntent();

         userId = intent.getStringExtra("KEY_USERID");

        databaseReference
                .child(AppConstant.FIREBASE_Order)
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderModelArrayList.clear();

                        for (DataSnapshot ordermodels : dataSnapshot.getChildren()) {
                            OrderModel orderModel = ordermodels.getValue(OrderModel.class);
                            orderModel.setPushKey(ordermodels.getKey());
                            orderModelArrayList.add(orderModel);
                        }

                        OrderAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void initview() {
        recyclerView = findViewById(R.id.rv_order_layout);
    }

    @Override
    public void onItemClick(OrderModel orderModel) {
        final Intent intent = new Intent(Order_page.this, Done_Order_page.class);

        intent.putExtra("KEY_ORDERID", orderModel.getPushKey());
        intent.putExtra("KEY_USER", userId);
        intent.putExtra("TOTAL", orderModel.getOrderTotal());


        startActivity(intent);
    }
}
