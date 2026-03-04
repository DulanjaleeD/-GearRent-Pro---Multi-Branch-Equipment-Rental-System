package com.gearrentpro.service;

import com.gearrentpro.dao.UserDAO;
import com.gearrentpro.entity.User;
import com.gearrentpro.util.PasswordUtil;
import com.gearrentpro.util.SessionManager;

import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO;
    private final SessionManager sessionManager;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.sessionManager = SessionManager.getInstance();
    }

    public boolean login(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);

        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            sessionManager.setCurrentUser(user);
            return true;
        }
        return false;
    }

    public void logout() {
        sessionManager.logout();
    }

    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }
}
