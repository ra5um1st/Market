package com.example.market;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected DataBase database;

    private ActivityResultLauncher<Intent> ResultProducts;
    private ActivityResultLauncher<Intent> CreateProducts;
    private Basic_RVA.OnItemClickListener onItemClickListener;
    private List<Product> productList;
    private List<Product> resultList;
    private Basic_RVA recyclerAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultList = new ArrayList<>();
        database = new DataBase(this);

        ResultProducts = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                resultList = (List<Product>)intent.getSerializableExtra("resultList");
                SetResultValue(resultList, findViewById(R.id.tv_result));
            }
        });
        CreateProducts = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                productList = (List<Product>) intent.getSerializableExtra("productList");
                resultList = new ArrayList<>();
                SetResultValue(resultList, findViewById(R.id.tv_result));
                recyclerAdapter = new Basic_RVA(productList);
                recyclerAdapter.setOnItemClickListener(onItemClickListener);
                recyclerView.setAdapter(recyclerAdapter);
            }
        });

        onItemClickListener = new Basic_RVA.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onButtonClick(int position) {
                resultList.add(productList.get(position));
                SetResultValue(resultList, findViewById(R.id.tv_result));
            }
        };

        findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResultProductActivity.class);
                intent.putExtra("resultList", (Serializable) resultList);
                ResultProducts.launch(intent);
            }
        });

        setRecyclerViewContent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert:
                try {
                    CreateProducts.launch(new Intent(MainActivity.this, CreateProductsActivity.class));
                }
                catch(Exception e){
                    Toast.makeText(this,"Не удалось открыть меню добавления товаров.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setRecyclerViewContent() {
        productList = new ArrayList<>();
        recyclerAdapter = new Basic_RVA(productList);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(onItemClickListener);
        SelectRecyclerViewItemsValues();
    }

    private void SelectRecyclerViewItemsValues() {
        Cursor cursor = database.getWritableDatabase().rawQuery("select *, "+ProductsTable.TABLE_NAME+".rowid as rowID from " + ProductsTable.TABLE_NAME,null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                int index_id = cursor.getColumnIndex("rowID");
                int index_productName = cursor.getColumnIndex(ProductsTable.COLUMN_NAME);
                int index_productValue = cursor.getColumnIndex(ProductsTable.COLUMN_VALUE);

                int id = cursor.getInt(index_id);
                String productName = cursor.getString(index_productName);
                int productValue = cursor.getInt(index_productValue);
                productList.add(new Product(id, productName, productValue));
            }
        }
        else {
            Toast.makeText(this, "Данные ProductsTable.TABLE_NAME не найдены", Toast.LENGTH_LONG);
        }
        cursor.close();
    }

    private void SetResultValue(List<Product> list, TextView textView){
        int result = CalculateResultValue(list);
        textView.setText(new StringBuilder().append(result).toString() + " р");
    }
    protected int CalculateResultValue(List<Product> list){
        int result = 0;
        for (Product product: list) {
            result += product.getValue();
        }
        return result;
    }
}