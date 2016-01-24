package com.codepatch.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepatch.simpletodo.persistance.PersistenceCoordinator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    private int editingItemIndex;
    private PersistenceCoordinator<String> persistenceCoordinator;

    private final String ITEMS_FILE_NAME = "todo.txt";
    public static final int EDIT_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_ACTIVITY_TEXT_CHANGED = 200;
    public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        performInitialSetup();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveItems();
    }

    private void performInitialSetup() {
        lvItems = (ListView)findViewById(R.id.lvItems);
        persistenceCoordinator = new PersistenceCoordinator<>(getApplicationContext(), ITEMS_FILE_NAME);
        populateItemsFromStorage();
        addItemsIfEmpty();
        setupAdapter();
        setupListViewListener();
    }

    private void populateItemsFromStorage() {
        items = persistenceCoordinator.readItems();
    }

    private void addItemsIfEmpty() {
        if (items == null) {
            items = new ArrayList<String>();
            items.add("Clean my room");
            items.add("Bring my cats inside");
            items.add("Make valentines day present");
            items.add("Sign up for the credit card");
            items.add("Reply to JLK and Hunters emails");
            items.add("Picnic with Bhaiyya");
        }
    }

    private void setupAdapter() {
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    private void saveItems() {
        persistenceCoordinator.writeItems(items);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                onItemClicked(pos);
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        if (itemText.length() == 0) {
            showErrorDialog();
            return;
        }

        itemsAdapter.add(itemText);
        etNewItem.setText("");
        saveItems();
    }

    private void showErrorDialog() {
        Context context = getApplicationContext();
        CharSequence text = "Please provide a task to add to the todo list";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onItemClicked(int pos) {
        String itemText = itemsAdapter.getItem(pos);
        launchEditView(itemText, pos);
    }

    public void launchEditView(String itemText, int pos) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(TEXT, itemText);
        editingItemIndex = pos;
        startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EDIT_ACTIVITY_TEXT_CHANGED && requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            String newText = data.getExtras().getString(TEXT);
            items.set(editingItemIndex, newText);
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        }
    }

}
