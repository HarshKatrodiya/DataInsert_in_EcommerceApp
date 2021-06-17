package com.example.datainsertproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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

    private Button button;
    private Button update_product;

    private ImageView imageView;
    private Button imageselectbtn;
    private Button submitalldetailsbtn;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Uri selectedFileIntent;

    private ProgressDialog progressDialog;
    String categoryString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(MainActivity.this);

        initview();
    }

    private void initview() {

        imageView = findViewById(R.id.image_view);
        productname = findViewById(R.id.product_name);
        productprice = findViewById(R.id.product_price);
        productrealprice = findViewById(R.id.product_real_price);
        productdiscount = findViewById(R.id.product_discount);
        Alldiscription = findViewById(R.id.product_description_all);
        SpecificationInthepacket = findViewById(R.id.specification_In_the_Packet_et);
        Specificationcolor = findViewById(R.id.specification_color_et);
        Specificationtype = findViewById(R.id.specification_type_et);
        Specificationweight = findViewById(R.id.specification_weight_et);
        SpecificationRibboncolor = findViewById(R.id.specification_Ribbon_color_et);
        Specificationflowerqty = findViewById(R.id.specification_flower_qty_et);
        moreinfoGenericname = findViewById(R.id.moreinfo_Generic_name_et);
        spinner = findViewById(R.id.Spineer);


        button = findViewById(R.id.go_to_orderpage);
        button.setOnClickListener(this);

        update_product = findViewById(R.id.Product_update_btn);
        update_product.setOnClickListener(this);


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Category, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);


        imageselectbtn = findViewById(R.id.select_image_btn);
        submitalldetailsbtn = findViewById(R.id.submit_btn);

        imageselectbtn.setOnClickListener(this);
        submitalldetailsbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_image_btn:
                selectimage();
                break;
            case R.id.submit_btn:
                allsubmitdetails();
                break;
            case R.id.Product_update_btn:
                UpdateProduct();
                break;
            case R.id.go_to_orderpage:
                final Intent intent = new Intent(this, user_order.class);
                startActivity(intent);
                break;
        }
    }

    private void UpdateProduct() {
        Intent intent = new Intent(MainActivity.this,ProductUpdate.class);
        startActivity(intent);
    }

    private void allsubmitdetails() {

        progressDialog.setTitle("Image Upload");
        progressDialog.show();

        if (selectedFileIntent != null) {

            final StorageReference sRef = storageReference.child("product/" + UUID.randomUUID().toString());

            sRef.putFile(selectedFileIntent)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    insertDataInToDb(uri);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
    }

    private void insertDataInToDb(Uri uri) {

        progressDialog.setMessage("Data inserting...");
        databaseReference.child("Products").push()
                .setValue(new Productmodel(uri.toString(),
                                productname.getText().toString(),
                                productprice.getText().toString(),
                                productrealprice.getText().toString(),
                                productdiscount.getText().toString(),
                                Alldiscription.getText().toString(),
                                SpecificationInthepacket.getText().toString(),
                                Specificationcolor.getText().toString(),
                                Specificationtype.getText().toString(),
                                Specificationweight.getText().toString(),
                                SpecificationRibboncolor.getText().toString(),
                                Specificationflowerqty.getText().toString(),
                                moreinfoGenericname.getText().toString(),categoryString),
                        new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                if (databaseError != null) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Error" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG_CODE
                && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                File file = new File(data.getData().getPathSegments().toString());
                selectedFileIntent = data.getData();

                InputStream stream = null;
                try {

                    stream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                imageselectbtn.setText(file.getName().substring(0, file.getName().length() - 1));
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
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
