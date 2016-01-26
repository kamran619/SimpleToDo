package com.codepatch.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.codepatch.simpletodo.model.Task;

public class EditItemActivity extends AppCompatActivity {

    private Task mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateTextViewWithEntry();
    }

    private void populateTextViewWithEntry() {
        mTask = getIntent().getExtras().getParcelable(MainActivity.TASK);
        EditText editText = (EditText) findViewById(R.id.etItem);
        editText.setText(mTask.getName());
        editText.setSelection(editText.getText().length());
    }

    public void onSaveClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.etItem);
        String editedText = editText.getText().toString();
        mTask.setName(editedText);
        Intent data = new Intent();
        data.putExtra(MainActivity.TASK, mTask);
        setResult(MainActivity.EDIT_ACTIVITY_TEXT_CHANGED, data);
        finish();
    }

}
