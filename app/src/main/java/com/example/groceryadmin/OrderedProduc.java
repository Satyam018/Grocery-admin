package com.example.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.groceryadmin.Adapter.Createproductadapter;
import com.example.groceryadmin.model.createproductmodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderedProduc extends AppCompatActivity {
    private static final String TAG = "TAG";
    RecyclerView recyclerView;
    Createproductadapter createproductadapters;
    List<createproductmodel> createproductmodelList;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_produc);
        recyclerView=(RecyclerView)findViewById(R.id.orderrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createproductmodelList=new ArrayList<>();
        createproductadapters=new Createproductadapter(OrderedProduc.this,createproductmodelList);
        recyclerView.setAdapter(createproductadapters);

        getdataa();

    }
    private void getdataa(){
        pd=new ProgressDialog(OrderedProduc.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
        pd.show();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                createproductmodelList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String refid=dataSnapshot.getKey();

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String userid=dataSnapshot1.getKey();
                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            String productid=dataSnapshot2.getKey();
                            String quantity=dataSnapshot2.child("quantity").getValue().toString();

                            detailinfo(productid,userid,quantity,refid);

                        }
                    }
                }


//                createproductadapters.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void detailinfo(String productid,String userid,String qunatity,String pushid){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("products").child(productid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String name=snapshot.child("name").getValue().toString();
                String imgurl=snapshot.child("imgurl").getValue().toString();

                createproductmodelList.add(new createproductmodel(productid,userid,name,qunatity,imgurl,pushid));
                Log.e(TAG, "onDataChange: "+createproductmodelList.size() );
                pd.dismiss();
                createproductadapters.notifyDataSetChanged();
                if (createproductmodelList.size()==0){
                    Toast.makeText(OrderedProduc.this,"No ordered product",Toast.LENGTH_LONG).show();
                }


            }



            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


}