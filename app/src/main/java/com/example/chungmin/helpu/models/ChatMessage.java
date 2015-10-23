package com.example.chungmin.helpu.models;

import org.joda.time.DateTime;

/**
 * Created by Chung Min on 16-10-2015.
 */
public class ChatMessage {
    private int chatMessageId;
    private long id;
    private String message;
    private int userIdFrom;
    private int userIdTo;
    private DateTime createdDate;
    private int isDelete;

    public ChatMessage() {
    }

    public ChatMessage(int chatMessageId, int id, String message, int userIdFrom, int userIdTo, DateTime createdDate, int isDelete) {
        this.chatMessageId = chatMessageId;
        this.id = id;
        this.message = message;
        this.userIdFrom = userIdFrom;
        this.userIdTo = userIdTo;
        this.createdDate = createdDate;
        this.isDelete = isDelete;
    }

    public int getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(int chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(int userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public int getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(int userIdTo) {
        this.userIdTo = userIdTo;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
