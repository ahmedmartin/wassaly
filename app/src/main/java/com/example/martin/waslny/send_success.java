package com.example.martin.waslny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class send_success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_success);
    }

    public void make_order(View view) {
        Intent t = new Intent(send_success.this,Main.class);
        t.putExtra("order",new order());
        t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(t);
        finish();
    }
}
