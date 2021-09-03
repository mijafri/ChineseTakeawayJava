package com.jafritech.chinesetakeawayjava;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jafritech.chinesetakeawayjava.helper.Helper;

import java.util.ArrayList;

public class RecyclerMenuAdapter extends RecyclerView.Adapter<RecyclerMenuAdapter.MyViewClass> {

    final ArrayList<MenuDataSource> menuList;
    final Context context;
    final Helper helper = new Helper();

    public RecyclerMenuAdapter(ArrayList<MenuDataSource> menu, Context context) {
        this.menuList = menu;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout_adapter, parent, false);

        return new MyViewClass(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerMenuAdapter.MyViewClass holder, int position) {

        holder.menuItem.setText(menuList.get(position).getName());
        holder.itemPrice.setText(helper.formatPrice(menuList.get(position).getPrice())) ;
        holder.imageView.setBackgroundResource(R.drawable.ctlogo);

        holder.itemView.setOnClickListener(v -> {
            MenuDataSource menu = menuList.get(position);
            Intent intentItem = new Intent(context, ItemActivity.class);
            intentItem.putExtra("NAME", menu.getName());
            intentItem.putExtra("PRICE", menu.getPrice());
            intentItem.putExtra("IMG", menu.getImg());
            context.startActivity(intentItem);
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MyViewClass extends RecyclerView.ViewHolder {

        final TextView menuItem;
        final TextView itemPrice;
        final ImageView imageView;
        public MyViewClass(View itemView) {
            super(itemView);

            menuItem = itemView.findViewById(R.id.fooditem_layout_tv);
            itemPrice = itemView.findViewById(R.id.price_layout_tv);
            imageView = itemView.findViewById(R.id.image_layout_img);
        }


    }
}
