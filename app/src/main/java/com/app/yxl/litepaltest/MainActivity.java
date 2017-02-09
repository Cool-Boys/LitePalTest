package com.app.yxl.litepaltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCreate = (Button) findViewById(R.id.create_database);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector.getDatabase();
            }
        });
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("The Lost code");
                book.setAuthor("Dan Brown");
                book.setPages(510);
                book.setPrice(19.5);
                book.setPress("Unkonw");
                book.save();
                Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });

        Button updata = (Button) findViewById(R.id.update_data);
        updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setPrice(14.95);
                book.setPress("Anchor");
                book.updateAll("name=? and author=?", "The Lost code", "Dan Brown");
                Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();

            }
        });

        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DataSupport.deleteAll(Book.class, "price<?", "15");
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> list = DataSupport.findAll(Book.class);
                String str = "";
                for (Book book : list) {
                    str += book.getName();
                }
                Toast.makeText(MainActivity.this, "查询到数据" + str, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
