package com.example.rmi.guide_tnt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    View.OnClickListener onClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };


}
