package com.example.groceryadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryadmin.Bottomdialogorderinfo;
import com.example.groceryadmin.R;
import com.example.groceryadmin.model.createproductmodel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Createproductadapter extends RecyclerView.Adapter<Createproductadapter.Viewholder> {
    Context context;
    List<createproductmodel> createproductmodelList;

    public Createproductadapter(Context context, List<createproductmodel> createproductmodelList) {
        this.context = context;
        this.createproductmodelList = createproductmodelList;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderedproductsingleorw,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        createproductmodel temp=createproductmodelList.get(position);
            holder.productname.setText(temp.getProductname());
            holder.quantity.setText(temp.getQuantity());
        Glide.with(context).load(temp.getImgurl()).into(holder.imgproduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bottomdialogorderinfo bottomdialogorderinfos=new Bottomdialogorderinfo(context,temp);
                bottomdialogorderinfos.show(((FragmentActivity)context).getSupportFragmentManager(),"TAG");
            }
        });

    }

    @Override
    public int getItemCount() {
        return createproductmodelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        TextView productname,quantity;
        ImageView imgproduct;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            productname=itemView.findViewById(R.id.orderproductname);
            quantity=itemView.findViewById(R.id.orderproductquantity);
            imgproduct=itemView.findViewById(R.id.orderedimage);
        }
    }
}
