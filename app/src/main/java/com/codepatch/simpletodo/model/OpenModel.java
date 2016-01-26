package com.codepatch.simpletodo.model;

import com.activeandroid.Model;

import java.lang.reflect.Field;

/**
 * Created by kpirwani on 1/25/16.
 */
public class OpenModel extends Model {
    public void setAaId(Long id) {
        try {
            Field idField = Model.class.getDeclaredField("mId");
            idField.setAccessible(true);
            idField.set(this, id);
        } catch (Exception e) {
            throw new RuntimeException("Reflection failed to get the Active Android ID", e);
        }
    }
}