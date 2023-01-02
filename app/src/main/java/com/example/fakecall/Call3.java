package com.example.fakecall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.fakecall.Adapter.GridAdapter;

import java.util.ArrayList;
import java.util.List;

public class Call3 extends AppCompatActivity {
    List<Model> list = new ArrayList<>() ;
    GridAdapter gridAdapter ;
    GridView gridView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call3);
        gridView = findViewById(R.id.gridview);
        addLiss();
        gridAdapter = new GridAdapter(this,list );
        gridView.setAdapter(gridAdapter);

    }
    public void addLiss()
    {
        list.add(new Model(R.drawable.add_ , "opppo")) ;
        list.add(new Model(R.drawable.bg , "samsung")) ;
        list.add(new Model(R.drawable.background , "iphone")) ;
        list.add(new Model(R.drawable.gallery_btn_2, "vivo")) ;
    }
}