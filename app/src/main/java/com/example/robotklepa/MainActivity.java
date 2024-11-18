package com.example.robotklepa;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // файлы сохранения данных
    private final static String FILE_NAME = "contentList.txt";
    private final static String FILE_URL = "urlList.txt";

    // набор данных, которые свяжем со списком
    Content[] contents = {
            new Content("1. ", "www", "пп"),
            new Content("2. ", "ссс", "www"),
            new Content("3. ", "агуша", "www"),
            new Content("4. ", "ссс", "www"),
            new Content("5. ", "ссс", "www"),
            new Content("6. ", "ссс", "www"),
            new Content("7. ", "ссс", "www"),
            new Content("8. ", "ссс", "www"),
            new Content("9. ", "ссс", "www"),
            new Content("10. ", "ссс", "www")
    };

    ArrayList<String> contentHeadList;
    ArrayList<String> contentURLList;

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

        // задаем набор данных для отображения
        this.contentHeadList = setContentHeadList();;
        this.contentURLList = setContentURLList();;

        // получаем элемент ListView
        ListView contentList = findViewById(R.id.contentHolder);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, this.contentHeadList);

        // устанавливаем для списка адаптер
        contentList.setAdapter(adapter);
    }

    private ArrayList<String> setContentHeadList() {
        ArrayList<String> contentList = new ArrayList<>();
        for (int i = 0; i < contents.length; ++i)
            contentList.add(contents[i].getNumber() + contents[i].getName());
        return contentList;
    }

    private ArrayList<String> setContentURLList() {
        ArrayList<String> contentList = new ArrayList<>();
        for (int i = 0; i < contents.length; ++i)
            contentList.add(contents[i].getUrl());
        return contentList;
    }

    public void randomize(View v) {
        Random random = new Random();
        TextView contentResultName = findViewById(R.id.contentResultName);
        TextView contentResultURL = findViewById(R.id.contentResultURL);
        try {
            int choice = random.nextInt(contents.length);
            contentResultName.setText(contents[choice].getName());
            contentResultURL.setText(contents[choice].getUrl());
            hideKeyboard();
        } catch (IllegalArgumentException e) {
            showErrorMessage("No content to choose");
        }
    }

    private void hideKeyboard(){
        View view;
        view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}