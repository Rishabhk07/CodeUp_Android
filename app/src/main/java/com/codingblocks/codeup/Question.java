package com.codingblocks.codeup;

/**
 * Created by naman on 21/6/17.
 */

public class Question {

    private String statement;
    private String input;
    private String output;
    private String stub;
    private String id;

    public String getStub() {
        return stub;
    }

    public String getOutput() {
        return output;
    }

    public String getInput() {
        return input;
    }

    public String getStatement() {
        return statement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
