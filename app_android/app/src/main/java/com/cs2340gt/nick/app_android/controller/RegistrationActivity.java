package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cs2340gt.nick.app_android.R;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);

        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), EditAccountInfoActivity.class);
                startActivity(intent);
            }
        });
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateUpTo(new Intent(getBaseContext(), LoggedInActivity.class));
            }
        });
    }


}
