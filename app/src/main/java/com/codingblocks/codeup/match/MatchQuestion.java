package com.codingblocks.codeup.match;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by naman on 21/6/17.
 */

public class MatchQuestion {

    private String source;
    private String id;
    HashMap<String, Object> timestampCreated;

    public MatchQuestion(String source, String id) {
        this.source = source;
        this.id = id;
        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampNow;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
        this.timestampCreated = timestampCreated;
    }
}
