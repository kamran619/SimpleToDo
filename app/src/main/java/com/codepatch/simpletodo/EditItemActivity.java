package com.codepatch.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

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
        String task = getIntent().getExtras().getString(MainActivity.TEXT);
        EditText editText = (EditText) findViewById(R.id.etItem);
        editText.setText(task);
        editText.setSelection(editText.getText().length());
    }

    public void onSaveClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.etItem);
        String editedText = editText.getText().toString();
        Intent data = new Intent();
        data.putExtra(MainActivity.TEXT, editedText);
        setResult(MainActivity.EDIT_ACTIVITY_TEXT_CHANGED, data);
        finish();
    }

}
