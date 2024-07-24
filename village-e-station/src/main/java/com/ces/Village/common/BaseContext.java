package com.ces.Village.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {
    private static final ThreadLocal<CurrentUser> threadLocal = new ThreadLocal<>();
    /**
     * 设置当前登录的用户
     */
    public static void setCurrentUser(CurrentUser currentUser) {
        threadLocal.set(currentUser);
    }

    /**
     * 获取当前登录的用户
     */
    public static CurrentUser getCurrentUser() {
        return threadLocal.get();
    }
}