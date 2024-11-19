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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // файлы сохранения данных
    private final static String FILE_NAME = "contentList.txt";
    private final static String FILE_URL = "urlList.txt";

    // объекты для класса MainActivity
    ArrayList<Content> contents = new ArrayList<>();
    ArrayList<String> contentHeadList = new ArrayList<>();
    ArrayList<Content> chosenContent = new ArrayList<>();
    ListView contentList;
    ArrayAdapter<String> adapter;

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

        // задаем референсы для отображения списка
        contentList = findViewById(R.id.contentHolder);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, contentHeadList);
        contentList.setAdapter(adapter);
        setData();

        // добавляем для списка слушатель
        contentList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем нажатый элемент
                if (contentList.isItemChecked(position))
                    chosenContent.add(contents.get(position));
                else
                    chosenContent.remove(contents.get(position));
            }
        });
    }

    public void addData(View v) {
        FileOutputStream fosName = null;
        FileOutputStream fosURL = null;
        EditText contentNameField = findViewById(R.id.contentNameInput);
        EditText urlField = findViewById(R.id.contentURLInput);
        if (contentNameField.getText().toString().isEmpty() && urlField.getText().toString().isEmpty()) {
            showErrorMessage("No content to add");
        } else if (contentNameField.getText().toString().isEmpty() && !urlField.getText().toString().isEmpty()) {
            showErrorMessage("No name for content");
        } else if (!contentNameField.getText().toString().isEmpty() && urlField.getText().toString().isEmpty()) {
            showErrorMessage("No link for content to add");
        } else {
            try {
                fosName = openFileOutput(FILE_NAME, MODE_APPEND);
                fosURL = openFileOutput(FILE_URL, MODE_APPEND);
                String contentName = contentNameField.getText().toString() + "\n";
                String contentURL = urlField.getText().toString() + "\n";
                fosName.write(contentName.getBytes());
                fosURL.write(contentURL.getBytes());
                clearFields();
            } catch (IOException e) {
                showErrorMessage(e.getMessage());
            } finally {
                try {
                    if (fosName != null)
                        fosName.close();
                    if (fosURL != null)
                        fosURL.close();
                    setData();
                } catch (IOException e) {
                    showErrorMessage(e.getMessage());
                }
            }
        }
    }

    public void setData() {
        FileInputStream finName = null;
        FileInputStream finURL = null;
        try {
            finName = openFileInput(FILE_NAME);
            finURL = openFileInput(FILE_URL);
            contents.clear();
            contentHeadList.clear();
            byte[] bytes = new byte[finName.available()];
            finName.read(bytes);
            String names = new String(bytes);
            bytes = new byte[finURL.available()];
            finURL.read(bytes);
            String url = new String(bytes);
            String[] urlList = url.split("\n");
            String[] namesList = names.split("\n");
            for (int i = 0; i < namesList.length; ++i) {
                if (!namesList[i].isEmpty())
                    contents.add(new Content(i + 1 + ". ", namesList[i], urlList[i]));
            }
            setContentHeadList(contentHeadList);
            if (!contents.isEmpty())
                adapter.notifyDataSetChanged();
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
            chosenContent.clear();
            clearFields();
            contentHeadList.clear();
            adapter.notifyDataSetChanged();
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

    private void setContentHeadList(List<String> contentList) {
        for (Content content : contents)
            contentList.add(content.getNumber() + content.getName());
    }

    public void randomize(View v) {
        Random random = new Random();
        ArrayList<Content> choiceArr = new ArrayList<>();
        TextView contentResultName = findViewById(R.id.contentResultName);
        TextView contentResultURL = findViewById(R.id.contentResultURL);
        contentResultName.setText(null);
        contentResultURL.setText(null);
        try {
            if (chosenContent.isEmpty()) {
                choiceArr.addAll(contents);
            } else {
                choiceArr.addAll(chosenContent);
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