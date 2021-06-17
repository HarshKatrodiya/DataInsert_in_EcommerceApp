package com.example.datainsertproduct;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class edit_product_page extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int PICK_IMG_CODE = 100;
    private EditText productname;
    private EditText productprice;
    private EditText productrealprice;
    private EditText productdiscount;
    private EditText Alldiscription;
    private EditText SpecificationInthepacket;
    private EditText Specificationcolor;
    private EditText Specificationtype;
    private EditText Specificationweight;
    private EditText SpecificationRibboncolor;
    private EditText Specificationflowerqty;
    private EditText moreinfoGenericname;
    private Spinner spinner;

    String categoryString;

    private ImageView imageView;
    private Button imageselectbtn;
    private Button submitalldetailsbtn;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initview();

        final Intent intent = getIntent();
        String productname1 = intent.getStringExtra("PRODUCTNAME");
        String productprice1 = intent.getStringExtra("PRODUCTPRICE");
        String productrealprice1 = intent.getStringExtra("PRODUCTREALPRICE");
        String productdiscount1 = intent.getStringExtra("PRODUCTDISCOUNT");
        String productimage1 = intent.getStringExtra("PRODUCTIMG");

        String alldiscription1 = intent.getStringExtra("ALLDISCRIPTION");
        String specificationinthepacket1 = intent.getStringExtra("SPECIFICATIONINTHEPACKET");
        String specificationcolor1 = intent.getStringExtra("SPECIFICATIONCOLOR");
        String specificationtype1 = intent.getStringExtra("SPECIFICATIONTYPE");
        String specificationweight1 = intent.getStringExtra("SPECIFICATIONWEIGHT");
        String specificationribboncolor1 = intent.getStringExtra("SPECIFICATIONRIBBONCOLOR");
        String specificationflowerqty1 = intent.getStringExtra("SPECIFICATIONFLOWERQTY");
        String moreinfogenericname1 = intent.getStringExtra("MOREINFOGENERICNAME");
       // Toast.makeText(this, productid, Toast.LENGTH_SHORT).show();


        productname.setText(productname1);
        productprice.setText(productprice1);
        productrealprice.setText(productrealprice1);
        productdiscount.setText(productdiscount1);
        Alldiscription.setText(alldiscription1);
        SpecificationInthepacket.setText(specificationinthepacket1);
        Specificationcolor.setText(specificationcolor1);
        Specificationtype.setText(specificationtype1);
        Specificationweight.setText(specificationweight1);
        SpecificationRibboncolor.setText(specificationribboncolor1);
        Specificationflowerqty.setText(specificationflowerqty1);
        moreinfoGenericname.setText(moreinfogenericname1);
        Glide.with(edit_product_page.this)
                .load(productimage1)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .into(imageView);


    }

    private void initview() {

        imageView = findViewById(R.id.image_view1);
        productname = findViewById(R.id.product_name1);
        productprice = findViewById(R.id.product_price1);
        productrealprice = findViewById(R.id.product_real_price1);
        productdiscount = findViewById(R.id.product_discount1);
        Alldiscription = findViewById(R.id.product_description_all1);
        SpecificationInthepacket = findViewById(R.id.specification_In_the_Packet_et1);
        Specificationcolor = findViewById(R.id.specification_color_et1);
        Specificationtype = findViewById(R.id.specification_type_et1);
        Specificationweight = findViewById(R.id.specification_weight_et1);
        SpecificationRibboncolor = findViewById(R.id.specification_Ribbon_color_et1);
        Specificationflowerqty = findViewById(R.id.specification_flower_qty_et1);
        moreinfoGenericname = findViewById(R.id.moreinfo_Generic_name_et1);
        spinner = findViewById(R.id.Spineer1);


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Category, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        imageselectbtn = findViewById(R.id.select_image_btn1);
        submitalldetailsbtn = findViewById(R.id.submit_btn1);

        imageselectbtn.setOnClickListener(this);
        submitalldetailsbtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_image_btn1:
                selectimage();
                break;
            case R.id.submit_btn1:
                allsubmitdetails();
                break;
        }

    }

    private void allsubmitdetails() {

        String newproductname = productname.getText().toString();

        Productmodel productmodel = new Productmodel();
        productmodel.setProductname(newproductname);

        String mykey = databaseReference.child("Products").push().getKey();
        Toast.makeText(this, mykey, Toast.LENGTH_SHORT).show();
    }

    private void selectimage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categoryString = adapterView.getSelectedItem().toString();
        Toast.makeText(this, "" + categoryString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
