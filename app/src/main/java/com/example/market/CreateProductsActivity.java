package com.example.market;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateProductsActivity extends MainActivity {
    AlertDialog dialog;
    List<Product> productList;
    Result_RVA recyclerAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_products);
        setFinishOnTouchOutside(true);
        SetRecyclerViewContent();
        SetDialogOnButtonClick();

        Result_RVA.OnItemClickListener listener = new Result_RVA.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CallUpdateDialog(position);
            }

            @Override
            public void onButtonClick(int position) {
                database.delete(productList.get(position));
                productList.remove(position);
                recyclerAdapter.notifyItemRemoved(position);
            }
        };
        recyclerAdapter.setOnItemClickListener(listener);
        Intent intent = new Intent();
        intent.putExtra("productList", (Serializable) productList);
        setResult(RESULT_OK, intent);
    }

    private void SetDialogOnButtonClick() {
        Button insert = findViewById(R.id.btn_insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallInsertDialog();
            }
        });
    }

    private void CallInsertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProductsActivity.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_product, null);
        Button confirm = v.findViewById(R.id.btn_confirm);
        Button cancel = v.findViewById(R.id.btn_cancel);
        EditText name = v.findViewById(R.id.et_productName);
        EditText value = v.findViewById(R.id.et_productValue);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !value.getText().toString().isEmpty()){
                    Product product = new Product(name.getText().toString(), Integer.parseInt(value.getText().toString()));
                    productList.add(product);
                    database.insert(product);
                }
                recyclerAdapter.notifyItemInserted(productList.size()+1);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void CallUpdateDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProductsActivity.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_product, null);
        Button confirm = v.findViewById(R.id.btn_confirm);
        Button cancel = v.findViewById(R.id.btn_cancel);
        EditText name = v.findViewById(R.id.et_productName);
        EditText value = v.findViewById(R.id.et_productValue);
        name.setText(productList.get(position).getName());
        value.setText(Integer.toString(productList.get(position).getValue()));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !value.getText().toString().isEmpty()){
                    Product product = productList.get(position);
                    product.setName(name.getText().toString());
                    product.setValue(Integer.parseInt(value.getText().toString()));
                    database.update(product);
                    recyclerAdapter.notifyItemChanged(position);
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        builder.setView(v);
        dialog = builder.create();
        dialog.show();

    }

    private void SetRecyclerViewContent() {
        productList = SelectRecyclerViewItemsValues();
        if(productList != null && productList.size() != 0) {
            recyclerAdapter = new Result_RVA(productList);
        }
        else {
            List<Product> list = new ArrayList<>();
            list.add(new Product("Товаров не найдено!",0));
            recyclerAdapter = new Result_RVA(list);
        }
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
    }
    private List<Product> SelectRecyclerViewItemsValues() {
        List<Product> list = new ArrayList<>();
        Cursor cursor = database.getWritableDatabase().rawQuery("select *,"+ProductsTable.TABLE_NAME+".rowid as rowID from " + ProductsTable.TABLE_NAME,null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                int index_id = cursor.getColumnIndex("rowID");
                int index_productName = cursor.getColumnIndex(ProductsTable.COLUMN_NAME);
                int index_productValue = cursor.getColumnIndex(ProductsTable.COLUMN_VALUE);

                int id = cursor.getInt(index_id);
                String productName = cursor.getString(index_productName);
                int productValue = cursor.getInt(index_productValue);
                list.add(new Product(id, productName, productValue));
            }
        }
        else {
            Toast.makeText(this, "Данные ProductsTable.TABLE_NAME не найдены", Toast.LENGTH_LONG);
        }
        cursor.close();
        return list;
    }
}
