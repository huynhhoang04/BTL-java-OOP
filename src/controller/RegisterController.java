package controller;

import model.User;

public class RegisterController {
    UserDataAccess dao = new UserDataAccess();

    public User registerUser(String username, String password, String email){
        User registerUser = new User(username, password, email);
        return dao.register(registerUser);
    }
}
