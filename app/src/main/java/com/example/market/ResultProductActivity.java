package com.example.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultProductActivity extends MainActivity {
    List<Product> resultList;
    Result_RVA recyclerAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        setFinishOnTouchOutside(true);

        Intent intent = getIntent();
        resultList = (List<Product>) intent.getSerializableExtra("resultList");

        SetRecyclerViewContent();
        SetOnOrderClick();

        Result_RVA.OnItemClickListener listener = new Result_RVA.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onButtonClick(int position) {
                resultList.remove(position);
                recyclerAdapter.notifyItemRemoved(position);
            }
        };
        recyclerAdapter.setOnItemClickListener(listener);

        intent = new Intent();
        intent.putExtra("resultList", (Serializable) resultList);
        setResult(RESULT_OK, intent);
    }

    private void SetRecyclerViewContent() {
        if(resultList != null && resultList.size() != 0) {
            recyclerAdapter = new Result_RVA(resultList);
        }
        else {
            List<Product> list = new ArrayList<>();
            list.add(new Product("Товаров не найдено!",0));
            findViewById(R.id.linear_layout).setVisibility(View.GONE);
            recyclerAdapter = new Result_RVA(list);
        }
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
    }
    private void SetOnOrderClick(){
        Button button = findViewById(R.id.order);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ResultProductActivity.this, "Вы оформили заказ " + resultList.size() + " товаров суммой " + CalculateResultValue(resultList), Toast.LENGTH_LONG).show();
                resultList.clear();
                SetRecyclerViewContent();
            }
        });
    }
}
