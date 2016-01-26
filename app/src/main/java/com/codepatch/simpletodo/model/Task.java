package com.codepatch.simpletodo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kpirwani on 1/25/16.
 */
@Table(name = "Tasks")
public class Task extends OpenModel implements Parcelable, Comparable {
    @Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE, notNull = true)
    protected String name;

    @Column(name = "date", notNull = true)
    protected Date date;

    @Column(name = "priority")
    protected Priority priority;

    @Column(name = "complete")
    protected boolean isComplete;

    @Override
    public int compareTo(Object another) {
        int t1Priority = this.priority.getValue();
        Task t2 = (Task) another;
        int t2Priority = t2.getPriority().getValue();
        if (t1Priority < t2Priority) {
            return 1;
        } else if (t1Priority == t2Priority) {
            return 0;
        } else {
            return -1;
        }
    }

    public enum Priority {
        LOW(0, "Low"),
        MEDIUM(1, "Medium"),
        HIGH(2, "High"),
        URGENT(3, "Urgent");

        private int priority;
        private String name;

        private Priority(int priority, String name) {
            this.priority = priority;
            this.name = name;
        }

        public int getValue() {
            return priority;
        }

        public String getDescription() {
            return name;
        }

        public static Priority createPriorityFromInt(int priority) {
            for (Priority p : Priority.values()) {
                if (p.getValue() == priority) {
                    return p;
                }
            }
            return null;
        }

        public static Priority createPriorityFromString(String description) {
            for (Priority p : Priority.values()) {
                if (p.getDescription().equalsIgnoreCase(description)) {
                    return p;
                }
            }
            return null;
        }

        public static List<String> allPriorities() {
            ArrayList<String> list = new ArrayList<>();
            for (Priority p : Priority.values()) {
                list.add(p.getDescription());
            }
            return list;
        }

    }

    public Task() {
        super();
    }

    public Task(Parcel in) {
        super();
        readFromParcel(in);
    }


    public Task(String name, Date date, Priority priority) {
        super();
        this.name = name;
        this.date = date;
        this.priority = priority;
        this.isComplete = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(getId());
        out.writeString(this.name);
        out.writeSerializable(this.date);
        out.writeSerializable(this.priority);
        out.writeByte((byte)(this.isComplete ? 1 : 0));
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public void readFromParcel(Parcel in) {
        setAaId((Long)in.readValue(null));
        this.name = in.readString();
        this.date = (Date) in.readSerializable();
        this.priority = (Priority) in.readSerializable();
        this.isComplete = in.readByte() != 0;
    }

}
