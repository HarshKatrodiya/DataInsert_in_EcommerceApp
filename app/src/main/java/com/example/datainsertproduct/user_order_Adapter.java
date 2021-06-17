package com.example.datainsertproduct;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class user_order_Adapter extends RecyclerView.Adapter<user_order_Adapter.MyviewHolder> {

    private ArrayList<UserOrderModel> userOrderModelArrayList;
    private Context context;
    private UserItemClickLister userItemClickLister;

    public user_order_Adapter(ArrayList<UserOrderModel> userOrderModelArrayList, Context context, UserItemClickLister userItemClickLister) {
        this.userOrderModelArrayList = userOrderModelArrayList;
        this.context = context;
        this.userItemClickLister = userItemClickLister;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new MyviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int i) {

        UserOrderModel userorderModel = userOrderModelArrayList.get(i);
        myviewHolder.setData(userorderModel, i);

    }

    @Override
    public int getItemCount() {

        Log.e("IZESI",userOrderModelArrayList.size()+"");

        return userOrderModelArrayList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView useremail;
        private TextView useraddress;
        private TextView userid;
        UserOrderModel userOrderModel;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            useremail = itemView.findViewById(R.id.user_email);
            useraddress = itemView.findViewById(R.id.user_address);
            userid = itemView.findViewById(R.id.user_id);

            itemView.setOnClickListener(this);
        }

        public void setData(UserOrderModel data, int i) {
            this.userOrderModel = data;

            useremail.setText(userOrderModel.getEmail());
            useraddress.setText(userOrderModel.getAddress());
            userid.setText(userOrderModel.getContact());

            Log.e("useruser",useremail.getText()+"");
        }

        @Override
        public void onClick(View view) {
            if (userItemClickLister != null) {
                userItemClickLister.onItemClick(userOrderModel,view);
            }
        }
    }
}
