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

import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    private Task mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTask = getIntent().getExtras().getParcelable(MainActivity.TASK);

        populateTextViewWithEntry();
        setupSpinner();
        setupDate();
    }

    private void populateTextViewWithEntry() {
        EditText editText = (EditText) findViewById(R.id.etEditTaskName);
        editText.setText(mTask.getName());
        editText.setSelection(editText.getText().length());
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spEditPriority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Priority.allPriorities());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(mTask.getPriority().getValue());
    }

    private void setupDate() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.editDatePicker);
        Date selectedDate = mTask.getDate();
//        datePicker.updateDate(selectedDate.getYear() + 1900, selectedDate.getMonth(), selectedDate.getDay());
    }

    public void onEditClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.etEditTaskName);
        String editedText = editText.getText().toString();
        mTask.setName(editedText);

        Spinner editSpinner = (Spinner) findViewById(R.id.spEditPriority);

        String spinnerValue = editSpinner.getSelectedItem().toString();
        Task.Priority priority = Task.Priority.createPriorityFromString(spinnerValue);
        mTask.setPriority(priority);

        DatePicker datePicker = (DatePicker) findViewById(R.id.editDatePicker);
        Date selectedDate = new Date(datePicker.getCalendarView().getDate());
        mTask.setDate(selectedDate);

        Intent data = new Intent();
        data.putExtra(MainActivity.TASK, mTask);
        setResult(MainActivity.EDIT_ACTIVITY_TEXT_CHANGED, data);
        finish();
    }

}
