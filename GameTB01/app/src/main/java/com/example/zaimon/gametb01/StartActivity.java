package com.example.zaimon.gametb01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by ZaiMon on 13/5/2559.
 */
public class StartActivity extends Activity implements View.OnClickListener {
        Button btnStart;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main);

                btnStart = (Button) findViewById(R.id.mbtnStart);
                btnStart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(intent);
                finish();
        }
}
