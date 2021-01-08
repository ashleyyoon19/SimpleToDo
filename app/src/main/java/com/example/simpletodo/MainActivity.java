package com.example.simpletodo;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.nio.charset.Charset;
import java.util.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT="item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE=20;

    List<String> items;

    Button button5;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button5=findViewById(R.id.button5);
        etItem=findViewById(R.id.etItem);
        rvItems=findViewById(R.id.rvitems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener=new ItemsAdapter.OnLongClickListener() {
            public void onItemLongClicked(int pos){
                items.remove(pos);
                itemsAdapter.notifyItemRemoved(pos);
                Toast.makeText(getApplicationContext(), "Item was successfully removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener=new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int pos) {
                Log.d("MainActivity", "Single click at position "+pos);
                Intent i=new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, items.get(pos));
                i.putExtra(KEY_ITEM_POSITION, pos);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter=new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String toDoItem=etItem.getText().toString();
                items.add(toDoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was successfully added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==EDIT_TEXT_CODE){
            String itemText=data.getStringExtra(KEY_ITEM_TEXT);
            int pos=data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(pos, itemText);
            itemsAdapter.notifyItemChanged(pos);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item successfully updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items=new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}