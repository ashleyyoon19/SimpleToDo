package com.example.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etAct;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etAct=findViewById(R.id.etAct);
        buttonSave=findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit item");

        etAct.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etAct.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}