package com.iconic.enrollment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.register_text) TextView mRegisterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mRegisterText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterText){
            Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(i);
            finish();
        }
    }
}
