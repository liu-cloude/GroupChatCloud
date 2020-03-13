package com.xingwang.groupchatcloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xingwang.groupchat.GroupListActivity;

public class MainActivity extends AppCompatActivity {
    protected TextView tv_group_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_group_list=findViewById(R.id.tv_group_list);

        tv_group_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupListActivity.getIntent(MainActivity.this);
            }
        });
    }
}
