package com.javahelps.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static DatabaseHandler mInstance = null;
    private static final String DATABASE_NAME = "todoList";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_ACTIVE = "active";

    private Context mCxt;

    public static DatabaseHandler getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DatabaseHandler(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_tasks_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_NAME, KEY_TIME, KEY_ACTIVE);
        db.execSQL(create_tasks_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_tasks_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_tasks_table);

        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getTaskName());
        values.put(KEY_TIME, task.getTaskTime());
        values.put(KEY_ACTIVE, String.valueOf(task.isActive()));

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Task getTask(int taskID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(taskID) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Task task = new Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2), Boolean.parseBoolean(cursor.getString(3)));
        return task;
    }
    public List<Task> getAllTasks() {
        List<Task>  taskList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Task task = new Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2), Boolean.parseBoolean(cursor.getString(3)));
            taskList.add(task);
            cursor.moveToNext();
        }
        return taskList;
    }
    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getTaskName());
        values.put(KEY_TIME, task.getTaskTime());
        values.put(KEY_ACTIVE, String.valueOf(!task.isActive()));

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(task.getId()) });
        db.close();
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public long countDone() {
        SQLiteDatabase db = getReadableDatabase();
        long count=DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                "active=?", new String[] {"true"});
        return  count;
    }
    public long countUndone() {
        SQLiteDatabase db = getReadableDatabase();
        long count=DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                "active=?", new String[] {"false"});
        return  count;
    }
}
