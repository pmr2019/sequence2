package com.example.todo.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "waitingRequests")
public class WaitingRequest implements Serializable {
    @PrimaryKey  @NonNull private String url;

    public WaitingRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WaitingRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
