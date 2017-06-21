package com.codingblocks.codeup.match;

import com.codingblocks.codeup.Question;

/**
 * Created by naman on 21/6/17.
 */

public class User {


    private String id;
    private int total=-1;
    private Question q1;
    private Question q2;
    private Question q3;

    public User(String id, Question q1, Question q2, Question q3) {
        this.id =id;
        this.q1 = q1;
        this.q2= q2;
        this.q3 = q3;

    }
}

