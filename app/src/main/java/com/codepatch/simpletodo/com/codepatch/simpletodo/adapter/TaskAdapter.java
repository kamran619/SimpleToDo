package com.codepatch.simpletodo.com.codepatch.simpletodo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.codepatch.simpletodo.R;
import com.codepatch.simpletodo.model.Task;
import com.codepatch.simpletodo.model.Task.Priority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by kpirwani on 1/25/16.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0 ,tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        TextView tvTaskName = (TextView) convertView.findViewById(R.id.tvTask);
        TextView tvTaskDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTaskPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        CheckBox tvTaskComplete = (CheckBox) convertView.findViewById(R.id.cbComplete);

        Task task = getItem(position);
        tvTaskName.setText(task.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String date = formatter.format(task.getDate());
        tvTaskDate.setText(date);
        Priority currentPriority = task.getPriority();
        switch (currentPriority) {
            case LOW:
                tvTaskPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPriorityLow));
                break;
            case MEDIUM:
                tvTaskPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPriorityMedium));
                break;
            case HIGH:
                tvTaskPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPriorityHigh));
                break;
            case URGENT:
                tvTaskPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPriorityUrgent));
                break;
        }
        tvTaskPriority.setText(task.getPriority().getDescription());
        tvTaskComplete.setChecked(task.isComplete());
        tvTaskComplete.setTag(position);

        tvTaskComplete.setOnClickListener(mOnClickListener);
        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;
            boolean isChecked = checkBox.isChecked();
            int position = (int) checkBox.getTag();
            Task task = getItem(position);
            task.setIsComplete(isChecked);
        }
    };

}
