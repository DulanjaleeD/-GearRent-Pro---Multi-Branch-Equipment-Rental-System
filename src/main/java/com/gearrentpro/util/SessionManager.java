package com.gearrentpro.util;

import com.gearrentpro.entity.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean hasRole(String roleName) {
        return currentUser != null && roleName.equals(currentUser.getRoleName());
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public boolean isBranchManager() {
        return currentUser != null && currentUser.isBranchManager();
    }

    public boolean isStaff() {
        return currentUser != null && currentUser.isStaff();
    }

    public Integer getUserBranchId() {
        return currentUser != null ? currentUser.getBranchId() : null;
    }
}
