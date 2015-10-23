package com.example.chungmin.helpu.sqlite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chungmin.helpu.models.ChatMessage;

import HelpUGenericUtilities.DateTimeUtils;

/**
 * Created by Chung Min on 10/16/2015.
 */
public class DBMessageController extends SQLiteOpenHelper {

    // All Static variables
    private static final String LOGCAT = null;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "helpU.db";

    // Contacts table name
    private static final String TABLE_MESSAGES = "messages";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USER_ID_FROM = "userIdFrom";
    private static final String KEY_USER_ID_TO = "userIdTo";
    private static final String KEY_CREATED_DATE = "createdDate";
    private String deleteAllQuery;

    public DBMessageController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d(LOGCAT, "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE " + TABLE_MESSAGES + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_MESSAGE + " TEXT, "
                + KEY_USER_ID_FROM + " INTEGER, "
                + KEY_USER_ID_TO + " INTEGER, "
                + KEY_CREATED_DATE + " DATETIME ) ";
        database.execSQL(query);

        Log.d(LOGCAT, TABLE_MESSAGES + " Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + TABLE_MESSAGES;
        database.execSQL(query);
        onCreate(database);

        Log.d(LOGCAT, TABLE_MESSAGES + " Dropped");
    }

    public long insert(ChatMessage chatMessage) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, chatMessage.getMessage());              //message
        values.put(KEY_USER_ID_FROM, chatMessage.getUserIdFrom());      //userIdFrom
        values.put(KEY_USER_ID_TO, chatMessage.getUserIdTo());          //userIdTo
        values.put(KEY_CREATED_DATE, getDateTime());                    //createdDate

        long rowId = database.insert(TABLE_MESSAGES, null, values);
        database.close();

        Log.d(LOGCAT, TABLE_MESSAGES + " Inserted");

        return rowId;
    }

    public int update(ChatMessage chatMessage) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, chatMessage.getMessage());              //message
        values.put(KEY_USER_ID_FROM, chatMessage.getUserIdFrom());      //userIdFrom
        values.put(KEY_USER_ID_TO, chatMessage.getUserIdTo());          //userIdTo
        values.put(KEY_CREATED_DATE, getDateTime());                    //createdDate

        Log.d(LOGCAT, TABLE_MESSAGES + " Updated");

        return database.update(TABLE_MESSAGES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(chatMessage.getId())});

    }

    public void delete(ChatMessage chatMessage) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_MESSAGES, KEY_ID + " = ?",
                new String[]{String.valueOf(chatMessage.getId())});
        database.close();

        Log.d(LOGCAT, TABLE_MESSAGES + " Deleted");
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteAllQuery = "DELETE FROM " + TABLE_MESSAGES;
        database.execSQL(deleteAllQuery);
        database.close();

        Log.d(LOGCAT, TABLE_MESSAGES + " Deleted All");
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        Log.d(LOGCAT, TABLE_MESSAGES + " Get Count");

        // return count
        return cursor.getCount();
    }

    public List<ChatMessage> getAllMessages(int mUserIdFrom, int mUserIdTo) {
        List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES + " " +
                "WHERE ( " + KEY_USER_ID_FROM + " = " + String.valueOf(mUserIdFrom) +
                "      AND " + KEY_USER_ID_TO + " = " + String.valueOf(mUserIdTo) + " ) " +
                "OR ( " + KEY_USER_ID_FROM + " = " + String.valueOf(mUserIdTo) +
                "      AND " + KEY_USER_ID_TO + " = " + String.valueOf(mUserIdFrom) + " ) ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                chatMessage.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
                chatMessage.setUserIdFrom(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID_FROM)));
                chatMessage.setUserIdTo(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID_TO)));
                chatMessage.setCreatedDate(DateTimeUtils.parseTo(cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE))));

                // Adding contact to list
                chatMessageList.add(chatMessage);
            } while (cursor.moveToNext());
        }

        Log.d(LOGCAT, TABLE_MESSAGES + " Get Message List");

        // return contact list
        return chatMessageList;
    }

    ChatMessage getById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES, new String[]{KEY_ID,
                        KEY_MESSAGE, KEY_USER_ID_FROM, KEY_USER_ID_TO, KEY_CREATED_DATE}, KEY_ID + " = ? ",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        chatMessage.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
        chatMessage.setUserIdFrom(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID_FROM)));
        chatMessage.setUserIdTo(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID_TO)));
        chatMessage.setCreatedDate(DateTimeUtils.parseTo(cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE))));

        Log.d(LOGCAT, TABLE_MESSAGES + " Get By Message Id");

        // return contact
        return chatMessage;
    }

    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
