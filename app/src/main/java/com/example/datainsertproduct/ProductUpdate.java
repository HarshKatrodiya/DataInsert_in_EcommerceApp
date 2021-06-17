package com.example.datainsertproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.UpdateAppearance;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductUpdate extends AppCompatActivity implements ItemClickListener {

    public RecyclerView recyclerView1;
    private ArrayList<Productmodel> arrayList;

    private ProgressDialog progressDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Homeadapterclass adapterclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_update);
        getSupportActionBar().setTitle("Update Your Product");
        initview();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(ProductUpdate.this);
        progressDialog.setTitle("Data Retrive");
        progressDialog.setMessage("Data fetching......");
        progressDialog.show();

        arrayList = new ArrayList<Productmodel>();

        adapterclass = new Homeadapterclass(arrayList, this,ProductUpdate.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(adapterclass);

        databaseReference.child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot productModel : dataSnapshot.getChildren()) {
//                            if(arrayList.size()>0)
//                            {
//                                arrayList.clear();
//                            }
                            Productmodel pm = productModel.getValue(Productmodel.class);
                            arrayList.add(pm);
                        }

                        adapterclass.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


    }

    private void initview() {
        recyclerView1 = findViewById(R.id.rv_1);
    }

    @Override
    public void onItemClick(Productmodel productmodel) {

        String s = databaseReference.child("Products").push().getKey();
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();

        final Intent gotoproduct_screenintent = new Intent(this, edit_product_page.class);
        gotoproduct_screenintent.putExtra("PRODUCTNAME", productmodel.getProductname());
        gotoproduct_screenintent.putExtra("PRODUCTPRICE", productmodel.getProductprice());
        gotoproduct_screenintent.putExtra("PRODUCTREALPRICE", productmodel.getProductrealprice());
        gotoproduct_screenintent.putExtra("PRODUCTDISCOUNT", productmodel.getProductDiscount());
        gotoproduct_screenintent.putExtra("PRODUCTIMG", productmodel.getImageurl());
        gotoproduct_screenintent.putExtra("ALLDISCRIPTION", productmodel.getAlldescription());
        gotoproduct_screenintent.putExtra("SPECIFICATIONINTHEPACKET", productmodel.getSpecificationInthepacket());
        gotoproduct_screenintent.putExtra("SPECIFICATIONCOLOR", productmodel.getSpecificaioncolor());
        gotoproduct_screenintent.putExtra("SPECIFICATIONTYPE", productmodel.getSpecificaionType());
        gotoproduct_screenintent.putExtra("SPECIFICATIONWEIGHT", productmodel.getSpecificaionWeight());
        gotoproduct_screenintent.putExtra("SPECIFICATIONRIBBONCOLOR", productmodel.getSpecificaionRibboncolor());
        gotoproduct_screenintent.putExtra("SPECIFICATIONFLOWERQTY", productmodel.getSpecificaionflowerqty());
        gotoproduct_screenintent.putExtra("MOREINFOGENERICNAME", productmodel.getMoreinfoGenericname());

        startActivity(gotoproduct_screenintent);

    }
}
