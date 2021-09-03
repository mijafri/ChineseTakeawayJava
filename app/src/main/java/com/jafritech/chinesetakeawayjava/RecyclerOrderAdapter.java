package com.jafritech.chinesetakeawayjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jafritech.chinesetakeawayjava.helper.Helper;

import java.util.ArrayList;

public class RecyclerOrderAdapter extends RecyclerView.Adapter<RecyclerOrderAdapter.MyViewClass> {

    final ArrayList<ItemsListDataSource> orderList;
    final Helper helper = new Helper();

    public RecyclerOrderAdapter(ArrayList<ItemsListDataSource> order, Context context) {
        this.orderList = order;
    }

    @NonNull
    @Override
    public RecyclerOrderAdapter.MyViewClass onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout_adapter, parent, false);

        return new MyViewClass(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerOrderAdapter.MyViewClass holder, int position) {

        holder.tvSno.setText(String.valueOf(orderList.get(position).getItemSno()));
        holder.tvName.setText(orderList.get(position).getItemName());
        holder.tvQty.setText(String.valueOf(orderList.get(position).getItemQty()));
        holder.tvPrice.setText(helper.formatPrice(orderList.get(position).getItemPrice()));

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class MyViewClass extends RecyclerView.ViewHolder {

        final TextView tvSno;
        final TextView tvName;
        final TextView tvQty;
        final TextView tvPrice;
        public MyViewClass(View itemView) {
            super(itemView);

            tvSno = itemView.findViewById(R.id.sn_order_adapter_tv);
            tvName = itemView.findViewById(R.id.item_order_adapter_tv);
            tvQty = itemView.findViewById(R.id.qty_order_adapter_tv);
            tvPrice = itemView.findViewById(R.id.price_order_adapter_tv);
        }


    }
}