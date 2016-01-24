package com.codepatch.simpletodo.persistance;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.io.FileUtils.writeLines;

/**
 * Created by kpirwani on 1/23/16.
 */
public class PersistenceCoordinator<E> {

    private Context context;
    private String fileName;
    private ArrayList<E> entryList;

    public PersistenceCoordinator(Context c, String f) {
        context = c;
        fileName = f;
    }

    protected String getFileName() {
        return fileName;
    }

    protected Context getContext() {
        return context;
    }

    public ArrayList<E> readItems() {
        File filesDir = getContext().getFilesDir();
        File dataFile = new File(filesDir, fileName);
        try {
            entryList = new ArrayList<E>((Collection<? extends E>) readLines(dataFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entryList;
    }

    public boolean writeItems(ArrayList<E> entries) {
        File filesDir = getContext().getFilesDir();
        File dataFile = new File(filesDir, fileName);
        boolean success = false;
        try {
            writeLines(dataFile, entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
