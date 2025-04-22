package com.chen.picture.utils;

import com.chen.picture.model.entity.User;

public class UserHolder {
    private static final ThreadLocal<User> threadlocal = new ThreadLocal<>();

    public static User getUser() {
        return threadlocal.get();
    }
    public static void setUser(User user) {
        threadlocal.set(user);
    }
    public static void removeUser() {
        threadlocal.remove();
    }

}
