package controller;

import model.User;

public class LoginController {
    UserDataAccess dao = new UserDataAccess();
    public User handleLogin(String username, String password){
        User loginUser = new User(username, password);
        return dao.login(loginUser);
    }
}
