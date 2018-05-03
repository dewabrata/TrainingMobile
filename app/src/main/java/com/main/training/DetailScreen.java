package com.main.training;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailScreen extends AppCompatActivity {

    public static int RESULT_CODE =69;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        setTitle("Detail Screen");
        ((Button)findViewById(R.id.btnFinish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("data","Ini dari detail screen");
                setResult(RESULT_CODE,intent);
                finish();
            }
        });
    }
}
