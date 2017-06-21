package com.codingblocks.codeup.match;

/**
 * Created by naman on 21/6/17.
 */

public class User {


    private String id;
    private int total=-1;
    private Match.Question q1;
    private Match.Question q2;
    private Match.Question q3;

    public User(String id, Match.Question q1, Match.Question q2, Match.Question q3) {
        this.id =id;
        this.q1 = q1;
        this.q2= q2;
        this.q3 = q3;

    }
}

