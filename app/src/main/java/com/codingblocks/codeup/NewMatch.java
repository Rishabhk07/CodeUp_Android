package com.codingblocks.codeup;

/**
 * Created by naman on 21/6/17.
 */

public class NewMatch {

    private String matchid;
    private String user1id;
    private String user2id;

    public NewMatch(String matchid, String user1id, String user2id){
        this.matchid = matchid;
        this.user1id = user1id;
        this.user2id = user2id;
    }

    public String getMatchid() {
        return matchid;
    }

    public String getUser1id() {
        return user1id;
    }

    public String getUser2id() {
        return user2id;
    }
}
