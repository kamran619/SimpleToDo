package com.codepatch.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepatch.simpletodo.com.codepatch.simpletodo.adapter.TaskAdapter;
import com.codepatch.simpletodo.model.Task;
import com.codepatch.simpletodo.persistance.PersistenceCoordinator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> items;
    private TaskAdapter itemsAdapter;
    private ListView lvItems;

    private int editingItemIndex;

    public static final int ADD_ITEM_ACTIVITY_REQUEST_CODE = 0;
    public static final int ADD_ITEM_ACTIVITY_ADDED = 199;

    public static final int EDIT_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_ACTIVITY_TEXT_CHANGED = 200;
    public static final String TASK = "task";

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
    public void onStop() {
        super.onStop();
        saveItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveItems();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        saveItems();
    }

    private void performInitialSetup() {
        lvItems = (ListView)findViewById(R.id.lvItems);
        populateItemsFromStorage();
        addItemsIfEmpty();
        setupAdapter();
        setupListViewListener();
    }

    private void populateItemsFromStorage() {
        items = (ArrayList<Task>) PersistenceCoordinator.selectEntries(Task.class, "priority DESC", null, null);
    }

    private void addItemsIfEmpty() {
        if (items == null) {
            items = new ArrayList<Task>();
            items.add(createTaskFromName("Clean my room"));
            items.add(createTaskFromName("Bring my cats inside"));
            items.add(createTaskFromName("Make valentines day present"));
            items.add(createTaskFromName("Sign up for the credit card"));
            items.add(createTaskFromName("Reply to JLK and Hunters emails"));
            items.add(createTaskFromName("Picnic with Bhaiyya"));
        }
    }

    private Task createTaskFromName(String name) {
        int minimumBound = Task.Priority.LOW.getValue();
        int maximumBound = Task.Priority.URGENT.getValue();
        //value between minimum bound and maximum bound(inclusive)
        int randomPriority = new Random().nextInt((maximumBound - minimumBound) + 1) + minimumBound;
        Task task = new Task(name, new Date(), Task.Priority.createPriorityFromInt(randomPriority));
        return task;
    }

    private void setupAdapter() {
        itemsAdapter = new TaskAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
    }

    private void saveItems() {
        PersistenceCoordinator.saveEntries(items);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                Task task = items.get(pos);
                items.remove(pos);
                task.delete();
                itemsAdapter.notifyDataSetChanged();
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
        launchAddItemView();
    }

    public void onItemClicked(int pos) {
        Task task = itemsAdapter.getItem(pos);
        launchEditView(task, pos);
    }

    public void launchAddItemView() {
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(intent, ADD_ITEM_ACTIVITY_REQUEST_CODE);
    }

    public void launchEditView(Task task, int pos) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(TASK, task);
        editingItemIndex = pos;
        startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EDIT_ACTIVITY_TEXT_CHANGED && requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            Task editedTask = data.getExtras().getParcelable(TASK);
            items.set(editingItemIndex, editedTask);
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        } else if (resultCode == ADD_ITEM_ACTIVITY_ADDED && requestCode == ADD_ITEM_ACTIVITY_REQUEST_CODE) {
            Task newTask = data.getExtras().getParcelable(TASK);
            items.add(newTask);
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        }
    }

}
