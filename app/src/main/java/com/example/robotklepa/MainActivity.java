package com.example.robotklepa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final static String FILE_NAME = "contentList.txt";
    private ArrayList<Content> contents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText contentField = findViewById(R.id.contentInput);
        Button addBtn = findViewById(R.id.addButton);
        Button clearBtn = findViewById(R.id.clearButton);
        setData();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(contentField);
                setData();
                contentField.setText(null);
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
    }

    private void addData(@NonNull EditText contentField) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            String contentName = contentField.getText().toString() + "\n";
            fos.write(contentName.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setData() {
        FileInputStream fin = null;
        updateContentList();
        try {
            fin = openFileInput(FILE_NAME);
            contents.clear();
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String content = new String(bytes);
            String[] contentList = content.split("\n");
            for (String i : contentList) {
                contents.add(new Content(i));
            }

        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clearData() {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            contents.clear();
            updateContentList();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateContentList() {
        RecyclerView recyclerView = findViewById(R.id.contentHolder);
        ContentAdapter adapter = new ContentAdapter(this, contents);
        recyclerView.setAdapter(adapter);
    }

    public void randomize(View v) {
        Random random = new Random();
        TextView contentResult = findViewById(R.id.contentResult);
        int choice = random.nextInt(contents.size());
        contentResult.setText(contents.get(choice).getName());
    }
}