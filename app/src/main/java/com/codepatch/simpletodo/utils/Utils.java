package com.codepatch.simpletodo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kpirwani on 1/25/16.
 */
public class Utils {

    public static void showDialog(Context context, CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
