package com.example.groceryadmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.groceryadmin.model.createproductmodel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Bottomdialogorderinfo extends BottomSheetDialogFragment {

    private static final String TAG ="TAG" ;
    private Context context;
    private createproductmodel createproductmodels;


    public Bottomdialogorderinfo(Context context, createproductmodel createproductmodels){
        this.context=context;
        this.createproductmodels=createproductmodels;
        }

    TextView nameoftheporduct,price,quantity,nameoftheperson,address,phoneno;
    ImageView imgproduct;
    Button viewback;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottomdialogorderinfo,container,false);
        nameoftheperson=view.findViewById(R.id.nameoftheperson);
        nameoftheporduct=view.findViewById(R.id.nameofproduct);
        price=view.findViewById(R.id.priceofproduct);
        quantity=view.findViewById(R.id.quantityoftheproduct);
        address=view.findViewById(R.id.addressoftheproduct);
        phoneno=view.findViewById(R.id.phoneonofproduct);
        imgproduct=view.findViewById(R.id.imageofproduct);
        viewback=view.findViewById(R.id.backtomain);

                getdatas();



        viewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }
    private void getdatas(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("orders").child(createproductmodels.getPushid())
                .child(createproductmodels.getUserid()).child(createproductmodels.getProductid());
        Log.e(TAG, "getdatas: "+createproductmodels.getProductid() );
        Log.e(TAG, "getdatas: "+createproductmodels.getPushid() );
        Log.e(TAG, "getdatas: "+createproductmodels.getUserid() );
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String personname=snapshot.child("personname").getValue().toString();
                String quantities=snapshot.child("quantity").getValue().toString();
                String phonenos=snapshot.child("phoneno").getValue().toString();
                String houseno=snapshot.child("houseno").getValue().toString();
                String housename=snapshot.child("housename").getValue().toString();
                String address1=snapshot.child("address").getValue().toString();
                String city=snapshot.child("city").getValue().toString();

                String mainadress="House no: "+houseno+" House name: "+housename+" Location: "+address1+" City: "+city;
                nameoftheperson.setText(personname);
                quantity.setText(quantities);
                phoneno.setText(phonenos);
                address.setText(mainadress);

                    productinfo(Integer.parseInt(quantities));
                    
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void productinfo(int quantites){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("products").child(createproductmodels.getProductid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String imgurls=snapshot.child("imgurl").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String prices=snapshot.child("price").getValue().toString();
                int pricesof1=Integer.parseInt(prices);
                int pricesofall=pricesof1*quantites;

                Glide.with(context).load(imgurls).into(imgproduct);
                nameoftheporduct.setText(name);
                price.setText("Rs."+String.valueOf(pricesofall));


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
