package com.socialnetwork.users.utils;

import com.socialnetwork.users.domain.User;

public class UserBuilder {

    private String name;
    private String email;

    public  UserBuilder name(String name){
        this.name = name;
        return this;
    }

    public  UserBuilder email(String email){
        this.email = email;
        return this;
    }

    public User build(){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
