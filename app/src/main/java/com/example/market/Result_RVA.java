package com.example.market;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Result_RVA extends RecyclerView.Adapter<Result_RVA.ViewHolder> {
    private List<Product> productList;
    private OnItemClickListener listener;

    public Result_RVA(List<Product> productList){
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = productList.get(position);
        holder.Header.setText(item.getName());
        if(item.getValue() <= 0){
            holder.Description.setText("");
            holder.Click.setVisibility(View.GONE);
        }
        else {
            holder.Description.setText(new StringBuilder().append(item.getValue()).toString());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Header;
        TextView Description;
        Button Click;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            Header = itemView.findViewById(R.id.header);
            Description = itemView.findViewById(R.id.description);
            Click = itemView.findViewById(R.id.click);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null && RecyclerView.NO_POSITION != getAdapterPosition()){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
            Click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null && RecyclerView.NO_POSITION != getAdapterPosition()){
                        listener.onButtonClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    interface OnItemClickListener{
        void onItemClick(int position);
        void onButtonClick(int position);
    }
}
