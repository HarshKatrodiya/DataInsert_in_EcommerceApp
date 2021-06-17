package com.example.datainsertproduct;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_order extends AppCompatActivity implements UserItemClickLister {

    private RecyclerView recyclerView;
    private ArrayList<UserOrderModel> userOrderModelArrayList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        recyclerView = findViewById(R.id.rv);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        userOrderModelArrayList = new ArrayList<UserOrderModel>();


        final user_order_Adapter user_order_adapter = new user_order_Adapter(userOrderModelArrayList, user_order.this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(user_order_adapter);

        databaseReference.child(AppConstant.FIREBASE_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.e("DATDATDA", dataSnapshot.getValue() + "");

                        for (DataSnapshot userModel : dataSnapshot.getChildren()) {
                            UserOrderModel userOrderModel = userModel.getValue(UserOrderModel.class);
                            userOrderModel.setPushkey(userModel.getKey());
                            userOrderModelArrayList.add(userOrderModel);
                        }
                        user_order_adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onItemClick(UserOrderModel userOrderModel, View view) {


        Intent intent = new Intent(user_order.this, Order_page.class);

        intent.putExtra("KEY_USERID", userOrderModel.getPushkey());

        startActivity(intent);
    }
}
