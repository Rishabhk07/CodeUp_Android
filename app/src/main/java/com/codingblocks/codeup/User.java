package com.codingblocks.codeup;

/**
 * Created by the-dagger on 21/06/17.
 */

public class User {
    private String id;
    private String name;
    private String email;
    private String image;

    public User(String id, String name, String email, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
