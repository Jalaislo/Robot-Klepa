package com.example.robotklepa;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // файлы сохранения данных
    private final static String FILE_NAME = "contentList.txt";
    private final static String FILE_URL = "urlList.txt";

    // набор данных, которые свяжем со списком
    Content[] contents = {
            new Content("1. ", "www", "пп"),
            new Content("2. ", "ссс", "www"),
            new Content("3. ", "агуша", "www")
    };

    ArrayList<String> contentHeadList = new ArrayList<>();
    ArrayList<Content> choosedContent = new ArrayList<>();

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
        setContentHeadList(contentHeadList);

        // получаем элемент ListView
        ListView contentList = findViewById(R.id.contentHolder);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, contentHeadList);

        // устанавливаем для списка адаптер
        contentList.setAdapter(adapter);

        // добавляем для списка слушатель
        contentList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем нажатый элемент
                if (contentList.isItemChecked(position))
                    choosedContent.add(contents[position]);
                else
                    choosedContent.remove(contents[position]);
            }
        });
    }

    private void setContentHeadList(List<String> contentList) {
        for (Content content : contents)
            contentList.add(content.getNumber() + content.getName() + "\n" + content.getUrl());
    }

    public void randomize(View v) {
        Random random = new Random();
        ArrayList<Content> choiceArr = new ArrayList<>();
        TextView contentResultName = findViewById(R.id.contentResultName);
        TextView contentResultURL = findViewById(R.id.contentResultURL);
        contentResultName.setText(null);
        contentResultURL.setText(null);
        try {
            if (choosedContent.isEmpty()) {
                for (Content i : contents)
                    choiceArr.add(i);
            } else {
                for (Content i : choosedContent)
                    choiceArr.add(i);
            }
            int choice = random.nextInt(choiceArr.size());
            contentResultName.setText(choiceArr.get(choice).getName());
            contentResultURL.setText(choiceArr.get(choice).getUrl());
            choiceArr.clear();
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