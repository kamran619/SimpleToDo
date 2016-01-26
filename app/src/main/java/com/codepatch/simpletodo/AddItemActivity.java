package com.codepatch.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepatch.simpletodo.model.Task;
import com.codepatch.simpletodo.model.Task.Priority;
import com.codepatch.simpletodo.utils.Utils;

import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupSpinner();
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spPriority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Priority.allPriorities());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onSaveClicked(View v) {
        EditText etTaskName = (EditText) findViewById(R.id.etTaskName);
        Spinner spPriority = (Spinner) findViewById(R.id.spPriority);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        String itemText = etTaskName.getText().toString();

        if (itemText.length() == 0) {
            Utils.showDialog(getApplicationContext(), "Please provide a task to add to the todo list");
            return;
        }

        String spinnerValue = spPriority.getSelectedItem().toString();
        Priority priority = Task.Priority.createPriorityFromString(spinnerValue);

        Date selectedDate = new Date(datePicker.getCalendarView().getDate());
        Task task = new Task(itemText, selectedDate, priority);

        Intent data = new Intent();
        data.putExtra(MainActivity.TASK, task);
        setResult(MainActivity.ADD_ITEM_ACTIVITY_ADDED, data);
        finish();
    }

}
