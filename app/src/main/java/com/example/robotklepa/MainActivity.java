package com.example.robotklepa;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private final static String FILE_URL = "urlList.txt";
    private ArrayList<Content> contents;

    {
        contents = new ArrayList<>();
    }

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

        EditText contentNameField = findViewById(R.id.contentNameInput);
        EditText contentURLField = findViewById(R.id.contentURLInput);
        Button addBtn = findViewById(R.id.addButton);
        if (!contents.isEmpty())
            setData();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentNameField.getText().toString().isEmpty() && contentURLField.getText().toString().isEmpty()) {
                    showErrorMessage("No content to add");
                } else if (contentNameField.getText().toString().isEmpty() && !contentURLField.getText().toString().isEmpty()) {
                    showErrorMessage("No name for content");
                } else if (!contentNameField.getText().toString().isEmpty() && contentURLField.getText().toString().isEmpty()) {
                    showErrorMessage("No link for content to add");
                } else {
                    addData(contentNameField, contentURLField);
                    setData();
                    clearFields();
                }
            }
        });
    }

    private void addData(EditText contentField, EditText urlField) {
        FileOutputStream fosName = null;
        FileOutputStream fosURL = null;
        try {
            fosName = openFileOutput(FILE_NAME, MODE_APPEND);
            fosURL = openFileOutput(FILE_URL, MODE_APPEND);
            String contentName = contentField.getText().toString() + "\n";
            String contentURL = urlField.getText().toString() + "\n";
            fosName.write(contentName.getBytes());
            fosURL.write(contentURL.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        } finally {
            try {
                if (fosName != null)
                    fosName.close();
                if (fosURL != null)
                    fosURL.close();
            } catch (IOException e) {
                showErrorMessage(e.getMessage());
            }
        }
    }

    private void setData() {
        FileInputStream finName = null;
        FileInputStream finURL = null;
        try {
            finName = openFileInput(FILE_NAME);
            finURL = openFileInput(FILE_URL);
            contents.clear();
            byte[] bytes = new byte[finName.available()];
            finName.read(bytes);
            String names = new String(bytes);
            bytes = new byte[finURL.available()];
            finURL.read(bytes);
            String url = new String(bytes);
            String[] urlList = url.split("\n");
            String[] namesList = names.split("\n");
            for (int i = 0; i < namesList.length; ++i) {
                contents.add(new Content(i + 1 + ". ", namesList[i], urlList[i]));
            }
            updateContentList();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        } finally {
            try {
                if (finName != null)
                    finName.close();
                if (finURL != null)
                    finURL.close();
            } catch (IOException e) {
                showErrorMessage(e.getMessage());
            }
        }
    }

    public void clearData(View v) {
        FileOutputStream fosName = null;
        FileOutputStream fosURL = null;
        try {
            fosName = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fosURL = openFileOutput(FILE_URL, MODE_PRIVATE);
            contents.clear();
            updateContentList();
            clearFields();
            hideKeyboard();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        } finally {
            try {
                if (fosName != null)
                    fosName.close();
                if (fosURL != null)
                    fosURL.close();
            } catch (IOException e) {
                showErrorMessage(e.getMessage());
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
        TextView contentResultName = findViewById(R.id.contentResultName);
        TextView contentResultURL = findViewById(R.id.contentResultURL);
        try {
            int choice = random.nextInt(contents.size());
            contentResultName.setText(contents.get(choice).getName());
            contentResultURL.setText(contents.get(choice).getUrl());
            hideKeyboard();
        } catch (IllegalArgumentException e) {
            showErrorMessage("No content to choose");
        }
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void clearFields() {
        EditText name = findViewById(R.id.contentNameInput);
        EditText url = findViewById(R.id.contentURLInput);
        TextView resultName = findViewById(R.id.contentResultName);
        TextView resultURL = findViewById(R.id.contentResultURL);
        url.setText(null);
        name.setText(null);
        resultName.setText(null);
        resultURL.setText(null);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}