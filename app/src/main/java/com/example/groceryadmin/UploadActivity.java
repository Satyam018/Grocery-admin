package com.example.groceryadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;

public class UploadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG ="TAG" ;
    ImageView imageView;
    EditText productname,price,offer;
    TextView addphoto;
    Button addproduct;
    Spinner spinners;
    Uri filepath;
    Bitmap bitmap;
    String categorys;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        spinners=(Spinner)findViewById(R.id.spinner1);
        addproduct=(Button)findViewById(R.id.addproducts);
        addphoto=(TextView)findViewById(R.id.addproduct);
        imageView=(ImageView)findViewById(R.id.product);
        productname=(EditText)findViewById(R.id.productname);
        price=(EditText)findViewById(R.id.price);
        offer=(EditText)findViewById(R.id.offer);


        ArrayAdapter<CharSequence> arrayAdapters=ArrayAdapter.createFromResource(UploadActivity.this,R.array.category, android.R.layout.simple_spinner_item);
        arrayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.setAdapter(arrayAdapters);
        spinners.setOnItemSelectedListener(this);

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(UploadActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Please select a Folder"),1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinfo();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);

                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorys=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void uploadtofirebase(String productname,String productprice,String categorys,String offers){
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final StorageReference ref = storageRef.child(System.currentTimeMillis()+"");
      StorageTask uploadTask = ref.putFile(filepath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("products");
                    HashMap<String ,Object> hashMap=new HashMap<>();
                    hashMap.put("name",productname);
                    hashMap.put("price",productprice);
                    hashMap.put("category",categorys);
                    hashMap.put("offers",offers);
                    hashMap.put("imgurl",downloadUri.toString());
                    Log.e(TAG, "onComplete: "+productname );
                    Log.e(TAG, "onComplete: "+productprice );
                    Log.e(TAG, "onComplete: "+categorys );
                    Log.e(TAG, "onComplete: "+offers );
                    Log.e(TAG, "onComplete: "+downloadUri );
                    reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(UploadActivity.this,"successfully uploaded",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UploadActivity.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(UploadActivity.this,"Failed to upload",Toast.LENGTH_LONG).show();
                        }
                    });



                } else {
                    Toast.makeText(UploadActivity.this,"unable to upload",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private void checkinfo(){
        String produtsname=productname.getText().toString();
        String prices=price.getText().toString();
        String category=categorys;
        String offers=offer.getText().toString();
        if (TextUtils.isEmpty(produtsname)||TextUtils.isEmpty(prices)){
            Toast.makeText(UploadActivity.this,"enter all info",Toast.LENGTH_LONG).show();

        }else if (category.equals("select category")){
            Toast.makeText(UploadActivity.this,"Select a category",Toast.LENGTH_LONG).show();
        }else{
            pd=new ProgressDialog(UploadActivity.this);
            pd.setTitle("Uploading products");
            pd.setMessage("Please Wait");
            pd.show();
            uploadtofirebase(produtsname,prices,category,offers);

        }
    }

}